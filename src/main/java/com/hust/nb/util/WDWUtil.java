package com.hust.nb.util;

/**
 * Description:nbqbtt
 * Created by hyJoo on 2019/06/3
 */
public class WDWUtil {
    //判断是否2003
    public static boolean isExcel2003(String filePath){
        if(filePath.endsWith("xls")){
            return true;
        }else {
            return false;
        }
        //return filePath.matches("^.+\\\\.(?i)(xls)$");
    }
    //判断是否03以上
    public static boolean isExcel2007(String filePath){
        if(filePath.endsWith("xlsx")){
            return true;
        }else {
            return false;
        }
        //return filePath.matches("^.+\\\\.(?i)(xlsx)$");
    }
}
