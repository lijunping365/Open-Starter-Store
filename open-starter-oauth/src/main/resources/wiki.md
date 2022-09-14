# 简单介绍下组件中使用的密码加密工具

依赖 pom 如下：

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

密码加密接口 PasswordEncoder，默认注入的实现类是 BCryptPasswordEncoder

```java
public interface PasswordEncoder {
   String encode(CharSequence rawPassword);

   boolean matches(CharSequence rawPassword, String encodedPassword);

   default boolean upgradeEncoding(String encodedPassword) {
      return false;
   }
}
```

这个接口有三个方法：

- encode 方法接受的参数是原始密码字符串，返回值是经过加密之后的 hash 值，hash 值是不能被逆向解密的。这个方法通常在为系统添加用户，或者用户注册的时候使用。

- matches 方法是用来校验用户输入密码 rawPassword，和加密后的 hash 值 encodedPassword 是否匹配。如果能够匹配返回 true，表示用户输入的密码 rawPassword 是正确的，反之返回 false。也就是说虽然这个 hash 值不能被逆向解密，但是可以判断是否和原始密码匹配。这个方法通常在用户登录的时候进行用户输入密码的正确性校验。

- upgradeEncoding 设计的用意是，判断当前的密码是否需要升级。也就是是否需要重新加密？需要的话返回 true，不需要的话返回 false。默认实现是返回 false。

## 哈希（Hash）与加密（Encrypt）

- 哈希（Hash）是将目标文本转换成**具有相同长度**的、**不可逆**的杂凑字符串（或叫做消息摘要）

- 加密（Encrypt）是将目标文本转换成**具有不同长度**的、**可逆**的密文。

### 两者区别和联系：

哈希算法往往被设计成生成具有相同长度的文本，而加密算法生成的文本长度与明文本身的长度有关。

哈希算法是不可逆的，而加密算法是可逆的。

HASH 算法是一种消息摘要算法，不是一种加密算法，但由于其单向运算，具有一定的不可逆性，成为加密算法中的一个构成部分。

### Hash 典型案例

1. MD5消息摘要算法

MD5消息摘要算法

一种被广泛使用的密码散列函数，可以产生出一个128位（16字节）的散列值（hash value），用于确保信息传输完整一致。

MD5典型应用

1）MD5的典型应用是对一段信息（Message）产生信息摘要（Message-Digest），以防止被篡改。

2）MD5的典型应用是对一段Message(字节串)产生fingerprint(指纹），以防止被“篡改”。

3）MD5还广泛用于操作系统的登陆认证上，如Unix、各类BSD系统登录密码、数字签名等诸多方面。

注意： 使用md5的方式对文件进行加密，以获取md5值，可以知道该文件的内容是否被修改过， 因为修改过文件内容，再次对该文件进行加密的话，获取的md5值将发生变化。

md5加盐加密: md5(md5(str) + md5(salt))

加盐的目的就是防止相同的字符串加密出来的结果相同，例如，多个人的密码可能相同为了防止加密结果相同所以加盐

2. Bcrypt

BCrypt 是由 Niels Provos 和 David Mazières 设计的密码哈希函数，他是基于 Blowfish 密码而来的，并于1999年在 USENIX 上提出。

除了加盐来抵御 rainbow table 攻击之外，bcrypt 的一个非常重要的特征就是自适应性，可以保证加密的速度在一个特定的范围内，即使计算机的运算能力非常高，可以通过增加迭代次数的方式，使得加密速度变慢，从而可以抵御暴力搜索攻击。

Bcrypt 可以简单理解为它内部自己实现了随机加盐处理。使用 Bcrypt，每次加密后的密文是不一样的。

对一个密码，Bcrypt 每次生成的 hash 都不一样，那么它是如何进行校验的？

1）虽然对同一个密码，每次生成的 hash 不一样，但是 hash 中包含了 salt（hash 产生过程：先随机生成 salt，salt 跟 password 进行 hash）；

2）在下次校验时，从 hash 中取出 salt，salt 跟 password 进行 hash；得到的结果跟保存在 DB 中的 hash 进行比对。

生成的加密字符串格式如下：

```
$2a$[cost]$[22 character salt][31 character hash]
```

比如：原始密码 123456，加密后字符串为

```
$2a$10$3oNlO/vvXV3FPsmimv0x3ePTcwpe/E1xl86TDC0iLKwukWkJoRIyK
\__/\/ \____________________/\_____________________________/
 Alg Cost      Salt                        Hash

```

上面例子中，$2a$ 表示的hash算法的唯一标志。这里表示的是Bcrypt算法。

10 表示的是代价因子，这里是2的10次方，也就是1024轮。

3oNlO/vvXV3FPsmimv0x3e 是16个字节（128bits）的salt经过base64编码得到的22长度的字符。

最后的PTcwpe/E1xl86TDC0iLKwukWkJoRIyK是24个字节（192bits）的hash，经过bash64的编码得到的31长度的字符。

对于同一个原始密码，每次加密之后的hash密码都是不一样的，这正是BCryptPasswordEncoder的强大之处，它不仅不能被破解，想通过常用密码对照表进行大海捞针你都无从下手

BCrypt 产生随机盐（盐的作用就是每次做出来的菜味道都不一样）。这一点很重要，因为这意味着每次encode将产生不同的结果。

### 加密典型案例

1. 对称加密 AES

高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法，是美国联邦政府采用的一种区块加密标准。

这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。经过五年的甄选流程，高级加密标准由美国国家标准与技术研究院（NIST）于2001年11月26日发布于FIPS PUB 197，并在2002年5月26日成为有效的标准。

2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。

AES加密原理

采用对称分组密码体制，密钥的长度最少支持为128、192、256位；加密分组长度128位，如果数据块及密钥长度不足时，会补齐进行加密。

2. 非对称加密 RSA

可使用PKCS#1、PKCS#8格式的公私钥，对数据进行加密解密。

RSA 算法广泛应用与加密与认证两个领域

1）加密（保证数据安全性）

使用公钥加密，需使用私钥解密。

这种广泛应用在保证数据的安全性的方面，用户将自己的公钥广播出去，所有人给该用户发数据时使用该公钥加密，但是只有该用户可以使用自己的私钥解密，保证了数据的安全性。

2）认证（用于身份判断）

使用私钥签名，需使用公钥验证签名。

用户同样将自己的公钥广播出去，给别人发送数据时，使用私钥加密，在这里，我们更乐意称它为签名，然后别人用公钥验证签名，如果解密成功，则可以判断对方的身份。

注意下面方法中的加密解密方法

如果是使用公钥加密则需要私钥进行解密

如果是使用私钥加密则需要公钥进行解密

# 简单介绍下组件中使用的 jwt 认证工具

生成 token

```java
public class JwtTokenStore extends AbstractTokenStore {

    private final OAuthProperties oauthProperties;

    public JwtTokenStore(TokenEnhancer tokenEnhancer, OAuthProperties oauthProperties) {
        super(tokenEnhancer);
        this.oauthProperties = oauthProperties;
    }

    @Override
    public AccessToken doGenerateToken(Authentication authentication) {
        final TokenProperties tokenProperties = oauthProperties.getToken();
        AccessToken token = new AccessToken();
        long now = System.currentTimeMillis();
        Date expiredDate = new Date(now + tokenProperties.getAccessTokenExpiresIn() * 1000);
        String userDetailsStr = JSON.toJSON(authentication.getUserDetails());
        Claims claims = Jwts.claims().setSubject(userDetailsStr);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(tokenProperties.getSecretKeyBytes()), SignatureAlgorithm.HS256)
                .compact();
        token.setExpiredTime(String.valueOf(expiredDate.getTime()));
        token.setAccessToken(accessToken);
        return token;
    }
}
```

校验 token

```java

public class JwtTokenService implements TokenService {

    private final SecurityProperties securityProperties;

    public JwtTokenService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public Authentication readAuthentication(String accessToken) {
        String subject;
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(securityProperties.getSecretKeyBytes()).build().parseClaimsJws(accessToken).getBody();
            subject = claims.getSubject();
        }catch (Exception e){
            throw new SecurityException(SecurityExceptionEnum.UNAUTHORIZED);
        }
        UserDetails userDetails = JSON.parse(subject, UserDetails.class);
        Authentication authentication = new Authentication();
        authentication.setUserDetails(userDetails);
        return authentication;
    }
}
```