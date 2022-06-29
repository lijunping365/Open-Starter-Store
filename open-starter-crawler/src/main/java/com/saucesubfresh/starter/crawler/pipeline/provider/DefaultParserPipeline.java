package com.saucesubfresh.starter.crawler.pipeline.provider;


import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 动态规则解析（自动模式）
 * @author lijunping on 2022/4/15
 */
@Component
public class DefaultParserPipeline extends AbstractParserPipeline {

    @Override
    protected Map<String, Object> doParse(String body, List<FieldExtractor> fieldExtractors) {
        Map<String, Object> dataMap;
        ExpressionType type = ExpressionType.of(fieldExtractors.get(0).getExpressionType());
        if (type == ExpressionType.JsonPath){
            dataMap = parseJson(body, fieldExtractors);
        } else {
            dataMap = parseHtml(body, fieldExtractors);
        }
        return dataMap;
    }

    @Override
    protected List<FieldExtractor> getFieldExtractors(SpiderRequest request) {
        final String extractRuleStr = request.getExtractRule();
        if (StringUtils.isBlank(extractRuleStr)){
            return Collections.emptyList();
        }

        return JSON.parseList(extractRuleStr, FieldExtractor.class);
    }
}
