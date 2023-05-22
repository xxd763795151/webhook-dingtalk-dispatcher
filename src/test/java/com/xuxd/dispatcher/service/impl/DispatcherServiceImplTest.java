package com.xuxd.dispatcher.service.impl;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: xuxd
 * @date: 2023/5/22 17:11
 **/
class DispatcherServiceImplTest {

    @Test
    void convert() {
        String input = "This is a ${test1}. Please verify ${test2} before ${date}";
        Pattern p = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher m = p.matcher(input);
        while (m.find()) {
            System.out.println("Match: " + m.group(0));
            System.out.println("Variable: " + m.group(1));
        }
    }
}