package com.xuxd.dispatcher.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: xuxd
 * @date: 2023/5/22 17:31
 **/
public class ConvertUtil {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");


    public static String convert(String template, Map<String, Object> labels) {
        String content = template;

        Matcher m = PATTERN.matcher(template);

        Map<String, String> varMap = new HashMap<>();
        while (m.find()) {
            varMap.put(m.group(1), m.group(0));
        }

        for (Map.Entry<String, String> entry : varMap.entrySet()) {
            String variable = entry.getKey();
            String original = entry.getValue();
            if (labels.containsKey(variable)) {
                Object o = labels.get(variable);
                if (o != null) {
                    String strValue = "";
                    if (o instanceof String) {
                        strValue = (String) o;
                    } else {
                        strValue = o.toString();
                    }
                    content = content.replace(original, strValue);
                }
            }
        }
        return content;
    }
}
