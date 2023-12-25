package com.xunmaw.cms.common.util;

import cn.hutool.http.HtmlUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created on 2022/3/30.
 *
 * @author Nobita
 */
class XssUtilTest {

    @Test
    void clean() {
        System.out.println(HtmlUtil.filter("厉害了😯<script>alert('\\''xxx'\\'')</script>123132span"));
    }
}