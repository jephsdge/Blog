package com.jephsdge.server.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {
    public static String encode(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.encode("123"));
    }
}
