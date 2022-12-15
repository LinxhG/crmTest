package com.heu.crm.poi;
/**
 * @author LinXueHang
 * @date 2022/12/10 9:24
 */

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 使用apache-poi生成excel文件
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/12/10 9:24
 */
public class CreateExcelTest {
    public static void main(String[] args) throws Exception{
        // 创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 使用HSSFWorkbook对象创建sheet对象，对应HSSFWorkbook对象excel文件中的一页
        HSSFSheet sheet = wb.createSheet("学生列表");
        // 使用sheet对象创建HSSFRow对象，对应sheet对象那一页中的一行，行号从0开始，依次增加
        HSSFRow sheetRow = sheet.createRow(0);
        // 使用HSSFRow对象创建HSSFCell对象，对应HSSFRow对象那一行中的一列，列号从0开始，依次增加
        HSSFCell sheetRowCell = sheetRow.createCell(0);
        sheetRowCell.setCellValue("学号");
        sheetRowCell = sheetRow.createCell(1);
        sheetRowCell.setCellValue("姓名");
        sheetRowCell = sheetRow.createCell(2);
        sheetRowCell.setCellValue("年龄");

        // 生成HSSFCellStyle对象
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        // 使用sheet创建10个HSSFRow对象，对应sheet中的10行
        for (int i = 1; i <= 10; i++) {
            HSSFRow row = sheet.createRow(i);

            sheetRowCell = row.createCell(0);
            sheetRowCell.setCellValue(100 + i);
            sheetRowCell = row.createCell(1);
            sheetRowCell.setCellValue("NAME" + i);

            sheetRowCell = row.createCell(2);
            // 在该列应用style样式
            sheetRowCell.setCellStyle(style);

            sheetRowCell.setCellValue("20" + i);
        }

        // 调用工具函数生成excel文件  目录路径必须提前创建好，文件不存在则会自动创建
        OutputStream os = new FileOutputStream("D:\\Master\\Learning\\DongLiJieDian\\SSM\\code\\crm-project\\serverDir\\studentList.xls");
        wb.write(os);

        // 关闭资源
        os.close();
        wb.close();

        System.out.println("=====================creat ok!");
    }
}
