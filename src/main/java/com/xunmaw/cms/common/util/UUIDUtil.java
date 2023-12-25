package com.xunmaw.cms.common.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * 生成UUID工具类
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@UtilityClass
public class UUIDUtil {

    private static final int SHORT_LENGTH = 8;

    public static String uuid() {
        String str = UUID.randomUUID().toString();
        return str.replace("-", "");
    }

    public static String getUniqueIdByUUId() {
        //最大支持1-9个集群机器部署
        int machineId = 1;
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCode);
    }

    public static void main(String[] args) {
        System.out.println(getUniqueIdByUUId());
        System.out.println(uuid());
    }


    private static final String[] CHARS = {"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    public static String generateShortUuid() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < SHORT_LENGTH; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(CHARS[x % 0x3E]);
        }
        return shortBuffer.toString();

    }


}
