/*
 * Copyright © the webmagic project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.starter.crawler.parser.provider;

import com.openbytecode.starter.crawler.parser.BaseElementSelector;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * CSS selector. Based on Jsoup.
 *
 * @see <a href="https://github.com/code4craft/webmagic">...</a>
 */
public class CssSelector extends BaseElementSelector {

    private String selectorText;

    private String attrName = "innerHtml";

    public CssSelector(String selectorText) {
        this.selectorText = selectorText;
    }

    public CssSelector(String selectorText, String attrName) {
        this.selectorText = selectorText;
        this.attrName = attrName;
    }

    @Override
    public String select(Element element) {
        List<Element> elements = selectElements(element);
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return getValue(elements.get(0));
    }

    @Override
    public List<String> selectList(Element doc) {
        List<String> strings = new ArrayList<String>();
        List<Element> elements = selectElements(doc);
        if (!CollectionUtils.isEmpty(elements)) {
            for (Element element : elements) {
                String value = getValue(element);
                if (value != null) {
                    strings.add(value);
                }
            }
        }
        return strings;
    }

    @Override
    public Element selectElement(Element element) {
        Elements elements = element.select(selectorText);
        if (!CollectionUtils.isEmpty(elements)) {
            return elements.get(0);
        }
        return null;
    }

    @Override
    public List<Element> selectElements(Element element) {
        return element.select(selectorText);
    }

    @Override
    public boolean hasAttribute() {
        return attrName != null;
    }

    private String getValue(Element element) {
        if (attrName == null) {
            return element.outerHtml();
        } else if ("innerHtml".equalsIgnoreCase(attrName)) {
            return element.html();
        } else if ("text".equalsIgnoreCase(attrName)) {
            return getText(element);
        } else if ("allText".equalsIgnoreCase(attrName)) {
            return element.text();
        } else {
            return element.attr(attrName);
        }
    }

    protected String getText(Element element) {
        StringBuilder accum = new StringBuilder();
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                accum.append(textNode.text());
            }
        }
        return accum.toString();
    }
}
