package com.fosun.data.cleanup.comment.tag.utils;

import com.fosun.data.cleanup.comment.tag.exception.BizException;
import com.fosun.data.cleanup.comment.tag.tuple.Tuple;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.fosun.data.cleanup.comment.tag.exception.ErrorCode.ARGS_ERROR;

/** 处理计算相关的业务
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/28 17:35
 * @description: 计算相关
 * @modified: TODO
 * @version: 1.0
 **/
@Slf4j
public class CalcUtil {
    private static final int SCALE = 2;   // 精度
    private static final float EPSINON = 0.00000001f; // 浮点的0

    /**
     * 计算两个数的比值。返回值为包含%的字符串
     * @param o1 ： 除数
     * @param o2 ： 被除数
     * @return 包含%的字符串
     * @throws BizException 当参数为非数值类型时，抛出参数异常的错误
     */
    public static String calcRate(Object o1,Object o2) throws BizException {
        Tuple<BigDecimal,BigDecimal> tuple = buildParams(o1,o2);
        if(null == tuple){
            return "-";
        }else{
            return (tuple.t1).multiply(new BigDecimal(100)).divide(tuple.t2,SCALE, RoundingMode.HALF_UP).toString() + "%";
        }

    }

    /**
     * 两个数相除，得到一个精度为2的浮点数
     * @param o1 除数
     * @param o2 被除数
     * @return 相除后的结果。Float 类型
     */
    public static Float calcDiv(Object o1,Object o2){
        Tuple<BigDecimal,BigDecimal> tuple = buildParams(o1,o2);
        if(null == tuple){
            return null;
        }else{
            return (tuple.t1).divide(tuple.t2,SCALE, RoundingMode.HALF_UP).floatValue();
        }
    }
    /**
     * 处理计算公式中的参数
     * @param o1 除数
     * @param o2 被除数
     * @return 转换后的结果
     */
    public static Tuple<BigDecimal,BigDecimal> buildParams(Object o1, Object o2){
        if (null == o1 || null == o2) {
            log.info("参数中有null值。返回null");
            return null;
        }
        if (!(o1 instanceof Number) || !(o2 instanceof Number)) {
            throw new BizException(ARGS_ERROR, String.format("非数值类型无法计算。除数：%s ,被除数：%s", o1.toString(), o2.toString()));
        }
        BigDecimal b1 = parseObjectToBigDecimal(o1,false); //除数
        BigDecimal b2 = parseObjectToBigDecimal(o2,true); //被除数
        if(null == b2 || null == b1) {
            return null;
        }else{
            return new Tuple<>(b1,b2);
        }
    }


    /**
     * 将Object 对象转换为 BigDecimal对象
     * @param obj Object对象
     * @param isDenominator 分母标识
     * @return BigDecimal 对象
     */
    private static BigDecimal parseObjectToBigDecimal(Object obj ,boolean isDenominator) {
        BigDecimal bigDecimal = null;
        if (obj instanceof  Integer) {
            if ((int) obj == 0 && isDenominator) {
                log.error("被除数为0,无法计算");
            } else {
                bigDecimal = new BigDecimal((int) obj);
            }
        } else if (obj instanceof Float) {
            if ((float) obj <= EPSINON  && isDenominator) {
                log.error("被除数为0,无法计算");
            } else {
                bigDecimal = new BigDecimal((float) obj);
            }
        } else if (obj instanceof Double) {
            if ((double) obj <= EPSINON  && isDenominator) {
                log.error("被除数为0,无法计算");
            } else {
                bigDecimal = new BigDecimal((double) obj);
            }
        } else if (obj instanceof Long) {
            if((long) obj == 0  && isDenominator){
                log.error("被除数为0,无法计算");
            } else {
                bigDecimal = new BigDecimal((long) obj);
            }
        }
        return bigDecimal;
    }



    public static void main(String[] args) {
        List<String> list = Arrays.asList("2.34%","-","23.20%","1.23%");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                if(obj2 == null || obj2.equals("-")){
                    return 1;
                }else if(obj1 == null  || obj1.equals("-")){
                    return -1;
                }
                if(obj1.equals(obj2)){
                    return 0;
                }
                return  Float.valueOf(obj1.substring(0,obj1.length()-1)).compareTo( Float.valueOf(obj2.substring(0,obj2.length()-1)));
            }
        });
        list.forEach(System.out::println);
    }
}
