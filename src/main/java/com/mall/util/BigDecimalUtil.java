package com.mall.util;

import java.math.BigDecimal;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/17 19:40
 * @ Description:
 */
public class BigDecimalUtil {


    private BigDecimalUtil() {
    }

    public static BigDecimal add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    public static BigDecimal mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        /* 四舍五入，保留两位小数 */
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }

    /* 计算价格时，不能用int float 要用BigDecimal
    *  用BigDecimal一定要用它的String构造器 */
}
