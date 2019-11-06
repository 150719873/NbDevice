package com.hust.nb.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Description:nbqbtt
 * Created by hyJoo on 2019/06/3
 */
@Component
public class ImportExcel {

    private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //日期格式yyyy-mm-dd HH:mm:ss
    private DecimalFormat df = new DecimalFormat("0");             //数字格式，防止长数字成为科学计数法形式，或者int变为double形式

    /** 总行数 */
    private int totalRows = 0;

    /** 总列数 */
    private int totalCells = 0;

    /** 错误信息 */
    private String errorInfo;

    /** 得到总行数 */
    public int getTotalRows(){
        return totalRows;
    }

    /** 得到总列数*/
    public int getTotalCells(){
        return totalCells;
    }

    /** 得到错误信息*/
    public String getErrorInfo(){
        return errorInfo;
    }

    /** 构造方法 */
    public ImportExcel(){
    }

    public String[] blockName = null;

    /**验证是不是Excel文件*/
    public boolean validateExcel(String filePath,File file)
    {
        //检查文件是否存在
        if (file == null || !file.exists())
        {
            errorInfo = "文件不存在";
            return false;
        }

        //检查文件名是否为空或者是否是Excel格式的文件
        if (filePath == null || !(WDWUtil.isExcel2003(filePath) || WDWUtil.isExcel2007(filePath)))
        {
            errorInfo = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    public List<List<String>>[] readSheets(InputStream is, boolean isExcel2003) {
        Workbook workbook = null;

        try{
            if(isExcel2003){
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        List<List<String >>[] res =  new ArrayList[workbook.getNumberOfSheets()];//双层list加一个数组，[i]代表一个双层List，代表一个sheet的所有数据
        blockName = new String[workbook.getNumberOfSheets()];
        for(int i = 0 ; i  < workbook.getNumberOfSheets(); i++){
            res[i] = read(workbook , i);
            blockName[i] = workbook.getSheetName(i);
        }
        return  res;
    }

//    public String getkthSheetName(InputStream is,boolean isExcel2003,int k){
//        Workbook workbook = null;
//        try{
//            if(isExcel2003){
//                workbook = new HSSFWorkbook(is);
//            } else {
//                workbook = new XSSFWorkbook(is);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return workbook.getSheetAt(k).getSheetName();
//    }

    public List<List<String>> read(Workbook workbook, int i){//获取当前workbook第i个sheet的所有数据
        List<List<String>> dataList3 = new ArrayList<List<String>>();
        //sheet的索引从0开始，第一张表是0，i代表有几页
        workbook.getNumberOfSheets();
        Sheet sheet = workbook.getSheetAt(i );
        //得到当前表的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        //得到当前表的列数
        if(totalRows>0 && sheet.getRow(0)!=null){
            this.totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        //两个for循环遍历每一行的每一个Cell
        for(int r = 0;r < sheet.getPhysicalNumberOfRows();r++){
            Row row = sheet.getRow(r);
            if(row == null){
                continue;
            }
            List<String> rowList = new ArrayList<String>();
            for(int c = 0;c<totalCells;c++){
                Cell cell = row.getCell(c);
                String cellValue = null;
                if(cell != null){
                    //判断读取的数据类型
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                cellValue = fmt.format(cell.getDateCellValue()); //日期型
                            } else {
                                cellValue = df.format(cell.getNumericCellValue()); //数字型
                            }
                            break;
                        case XSSFCell.CELL_TYPE_STRING: //文本类型
                            cellValue = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN: //布尔型
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case XSSFCell.CELL_TYPE_BLANK: //空白
                            cellValue = "";
                            break;
                        case XSSFCell.CELL_TYPE_ERROR: //错误
                            cellValue = "错误";
                            break;
                        case XSSFCell.CELL_TYPE_FORMULA: //公式
                            try {
                                cellValue = String.valueOf(cell.getStringCellValue());
                            } catch (IllegalStateException e) {
                                cellValue = String.valueOf(cell.getNumericCellValue());
                            }
                            break;
                        default:
                            cellValue = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
                    }
                }
                rowList.add(cellValue);//保存某行每一个cell的值
            }
            dataList3.add(rowList);
        }
        return dataList3;
    }
}
