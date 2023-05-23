package com.xuxd.dispatcher.utils;

import com.xuxd.dispatcher.beans.AlertStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    public static String utc2Gmt8(String utcDateStr) {
        Instant instant = Instant.parse(utcDateStr);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        // 格式化输出：2022-05-01 08:00:00
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return zonedDateTime.format(formatter);
    }

    public static String statusOf(String status) {
        switch (status) {
            case AlertStatus.FIRING:
                return "触发告警";
            case AlertStatus.RESOLVED:
                return "问题已解决";
        }
        return "触发告警";
    }
}
