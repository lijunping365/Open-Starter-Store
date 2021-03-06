package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 默认数据格式化
 * @author lijunping on 2022/4/26
 */
@Slf4j
public class DefaultFormatPipeline extends AbstractFormatPipeline {

    @Override
    protected List<Map<String, Object>> doFormat(SpiderRequest request, SpiderResponse response) {
        final Map<String, Object> parseResult = response.getParseResult();
        final List<FieldExtractor> fieldExtractors = request.getExtract();
        if (CollectionUtils.isEmpty(parseResult) || CollectionUtils.isEmpty(fieldExtractors)){
            return null;
        }
        long count = fieldExtractors.stream().filter(FieldExtractor::isMulti).count();
        List<Map<String, Object>> formatResult;
        // 如果所有字段的值解析后都是 List，则转换成 List
        if (fieldExtractors.size() == count){
            formatResult = formatList(parseResult);
        }else {
            formatResult = formatObject(parseResult);
        }
        return formatResult;
    }


}
