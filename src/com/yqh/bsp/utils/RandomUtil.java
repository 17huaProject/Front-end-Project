package com.yqh.bsp.utils;

import java.util.Random;


public class RandomUtil {
	public static String genRandomCode(int leng) {
		char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		String code = "";
		for (int i = 0; i < leng; i++) {// 得到随机产生的验证码数字。
			code += String.valueOf(codeSequence[random.nextInt(10)]);
		}
		return code;
	}

	public static String getImageRandomCode() {
		Random random = new Random();
		String code = "";
		for (int i = 0; i < 4; i++)
			code += String.valueOf(random.nextInt(10));
		return code;
	}
	
	/**
     * 随机生成一组int型数据，个数为count,大小范围是0 ~ chooseLength(不包括chooseLength)，且值均不相等
     * 
     * @author 
     * @param count
     * @param chooseLength
     * @return
     */
	public static int[] getRandomNumNotEquals(int count, int chooseLength) {
        int[] num = new int[count];
        Random rd = new Random();
        Boolean flag = true;
        for (int i = 0; i < count; i++) {
            while (flag) {
                flag = false;
                int newNum = rd.nextInt(chooseLength);
                for (int j = 0; j < i; j++) {
                    if (num[j] == newNum) {
                        flag = true;
                        break;
                    }
                }
                num[i] = newNum;
            }
            flag = true;
        }
        return num;
    }
}