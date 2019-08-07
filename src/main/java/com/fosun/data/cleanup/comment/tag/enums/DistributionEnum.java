package com.fosun.data.cleanup.comment.tag.enums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 距离分布枚举
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/1 15:25
 * @description: 距离分布枚举
 * @modified: TODO
 * @version: 1.0
 **/

public enum DistributionEnum {
    ONE_KM(1,"1 Km"),
    THREE_KM(3,"3 Km"),
    FIVE_KM(5,"5 Km"),
    SEVEN_KM(7,"7 Km"),
    TEN_KM(10,"10 Km"),
    FIFTEEN_KM(15,"15 Km"),
    TWEENTY_KM(20,"20 Km"),
    BIGGER_KM(-1,">20 Km");

    private int index;
    private String value;
    DistributionEnum(int index,String value){
        this.index = index;
        this.value = value;
    }
    public int getIndex(){ return index;}
    public String getValue(){ return value;}

    public static String getValueByIndex(int index){
      for(DistributionEnum distributionEnum: DistributionEnum.values()){
        if(distributionEnum.getIndex() == index){
            return distributionEnum.getValue();
        }
      }
       return null;
    }

    static public  void printNames(){
        for(DistributionEnum e : values()){
            System.out.println(e.name());
        }
    }

    public static List<String> getNames(){
        List<String> list = new ArrayList<>();
        for(DistributionEnum e: values()){
            if(e.getIndex() == BIGGER_KM.getIndex()){
                list.add( e.getValue());
            }else{
                list.add("≤" + e.getValue());
            }
        }
        return list;
     }

    /**
     * 根据距离参数值，获取相应的距离索引列表
     * @param bigDecimal 距离参数
     * @return 距离索引列表
     */
    public static List<Integer> parseDist(BigDecimal bigDecimal){
        List<Integer> list = new ArrayList<>();
        if(bigDecimal == null){ list.add(0); return list;}
         for(DistributionEnum e: values()){
             if(e.equals(BIGGER_KM)){
                 if(bigDecimal.compareTo(new BigDecimal(TWEENTY_KM.getIndex()))> 0){
                     list.add(e.getIndex());
                 }
             }else{
                 if( bigDecimal.compareTo(new BigDecimal(e.getIndex())) <= 0){
                     list.add(e.getIndex());
                 }
             }

        }
        return list;
    }


}
