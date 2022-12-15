package com.heu.crm.commons.utils;
/**
 * @author LinXueHang
 * @date 2022/12/12 14:40
 */

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 关于excel文件操作的工具类
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/12/12 14:40
 */
public class HSSFUtils {
    /**
     * 从指定的HSSFCell对象中获取列的值
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell){
        String ret = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
            ret = cell.getStringCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
            // 只有存在继承关系的情况下，才可以强转
            ret = cell.getNumericCellValue() + "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){
            ret = cell.getBooleanCellValue() + "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
            ret = cell.getCellFormula() + "";
        }
        return ret;
    }
}
