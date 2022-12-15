package com.heu.crm.poi;
/**
 * @author LinXueHang
 * @date 2022/12/12 14:05
 */

import com.heu.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/12/12 14:05
 */
public class PraseExcelTest {
    public static void main(String[] args) throws Exception{
        // 根据excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
        FileInputStream is = new FileInputStream("D:\\Master\\Learning\\DongLiJieDian\\SSM\\code\\crm-project\\serverDir\\studentList.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        // 根据wb获取HSSFSheet对象，封装了一页的所有信息
        HSSFSheet sheet = wb.getSheetAt(0);
        // 根据sheet获取HSSFRow对象，封装了一行的所有信息
        HSSFRow row = null;
        // sheet.getLastRowNum()；最后一行的下标
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            // 根据row获取HSSFCell对象，封装一列的所有信息
            HSSFCell cell = null;
            // row.getLastCellNum()；和行的不一样，是最后一列的下表 + 1
            for (int j = 0; j < row.getLastCellNum(); i++) {
                cell = row.getCell(j);
                // 获取列中的数据
                String value = HSSFUtils.getCellValueForStr(cell);
            }
        }
    }

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
