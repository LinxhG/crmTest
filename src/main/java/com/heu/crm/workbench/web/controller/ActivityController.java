package com.heu.crm.workbench.web.controller;
/**
 * @author LinXueHang
 * @date 2022/11/24 19:12
 */

import com.heu.crm.commons.constant.Contants;
import com.heu.crm.commons.entity.ReturnObject;
import com.heu.crm.commons.utils.DateUtils;
import com.heu.crm.commons.utils.HSSFUtils;
import com.heu.crm.commons.utils.UUIDUtils;
import com.heu.crm.settings.entity.User;
import com.heu.crm.settings.service.UserService;
import com.heu.crm.workbench.entity.Activity;
import com.heu.crm.workbench.entity.ActivityRemark;
import com.heu.crm.workbench.service.ActivityRemarkService;
import com.heu.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/24 19:12
 */
@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        // 调用service方法，查询所有员工
        List<User> users = userService.queryAllUsers();
        // 把数据保存到request域中
        request.setAttribute("users", users);
        // 请求转发到市场活动主页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        // 返回信息的对象
        ReturnObject returnObject = new ReturnObject(Contants.RETURN_OBJECT_CODE_FAIL, null, null);
        // 从session中获取创建者
        User currentUser = (User) session.getAttribute(Contants.CURRENT_USER_KEY);
        // 封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDataTime(new Date()));
        activity.setCreateBy(currentUser.getId());
        // 调用service方法，保存创建的市场活动
        try {
            int retCount = activityService.saveCreateActivity(activity);
            if (retCount > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setMessage("系统忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate,
                                                  int pageNo, int pageSize){
        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        // 调用service方法，查询数据
        List<Activity> activityList = activityService.queryActivityByConditionsForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        // 根据查询结果，生成响应信息
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] ids) {
        // 返回信息的对象
        ReturnObject returnObject = new ReturnObject(Contants.RETURN_OBJECT_CODE_FAIL, null, null);
        try {
            // 调用service方法，删除市场活动
            int retCount = activityService.deleteActivityByIds(ids);
            if (retCount > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setMessage("系统忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        // 调用service方法，根据id查询市场活动
        Activity activity = activityService.queryActivityById(id);
        // 根据查询结果，返回响应信息
        return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        // 获取当前用户
        User currentUser = (User) session.getAttribute(Contants.CURRENT_USER_KEY);
        // 前台只有7个参数传回，但是实体类需要9个参数，因此再封装参数
        activity.setEditTime(DateUtils.formatDataTime(new Date()));
        activity.setEditBy(currentUser.getId());
        // 返回信息的对象
        ReturnObject retObj = new ReturnObject(Contants.RETURN_OBJECT_CODE_FAIL, null, null);
        try {
            // 调用service方法，保存更新的市场活动
            int retCount = activityService.saveEditActivity(activity);
            if (retCount > 0) {
                retObj.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                retObj.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retObj.setMessage("系统忙，请稍后重试...");
        }
        // 根据查询结果，返回响应信息
        return retObj;
    }

    /**
     * 下载文件的示例
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws Exception{
        // 1、设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 2、获取输出流
        // PrintWriter out = response.getWriter(); // 以字符为单位，但是输出的是字节流，所以不能用
        // 以字节为单位
        OutputStream outputStream = response.getOutputStream();

        /* 浏览器接收到响应信息之后，默认情况下，直接在显示窗口中打开响应信息；
           即使打不开，也会调用应用程序打开；
           只有实在打不开，才会激活文件下载窗口 */
        // 可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开。
        response.addHeader("Content-Disposition", "attachment;filename=mystudentList.xls");

        // 读取excel文件(InputStream)，把输出到浏览器(OutputStream)
        FileInputStream is = new FileInputStream("D:\\Master\\Learning\\DongLiJieDian\\SSM\\code\\crm-project\\serverDir\\studentList.xls");
        // 建立缓冲区，暂存读取的文件数据
        byte[] buff = new byte[1024];
        // 读取的长度，只要不等于-1，就会一直读下去（-1是状态码）
        int len = 0;
        // 每次读取缓冲区大小的文件，读到缓冲区中
        while ((len = is.read(buff)) != -1) {
            // 读一个缓冲区，往外写一个，从位置0开始，读多少写多少
            outputStream.write(buff, 0, len);
        }
        // 关闭资源，设开启谁关闭
        is.close();
        // outputStream是response new的，response是tomcat new的，所以应该是tomcat去关，tomcat会自动关
        // flush会把自己的数据都刷走
        outputStream.flush();
    }

    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws Exception{
        // 调用service方法，查询所有市场活动
        List<Activity> activityList = activityService.queryAllActivities();
        // 创建excel文件，并且把activityList写入到excel文件中
        // 1、创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 使用HSSFWorkbook对象创建sheet对象，对应HSSFWorkbook对象excel文件中的一页
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        // 使用sheet对象创建HSSFRow对象，对应sheet对象那一页中的一行，行号从0开始，依次增加
        HSSFRow row = sheet.createRow(0);
        // 使用HSSFRow对象创建HSSFCell对象，对应HSSFRow对象那一行中的一列，列号从0开始，依次增加
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建日期");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改日期");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        // 遍历activityList，创建HSSFRow对象，生成所有的数据行
        if (activityList != null && activityList.size() > 0) {
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);
                // 每遍历出一个activity，生成一行
                row = sheet.createRow(i + 1);
                // 每一行创建11列，每一列的数据从activity中获取
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        // 根据wb对象生成excel文件
        /* OutputStream os = new FileOutputStream("D:\\Master\\Learning\\DongLiJieDian\\SSM\\code\\crm-project\\serverDir\\studentList.xls");
           wb.write(os);
        // 关闭资源
        os.close();
        wb.close();
        */

        // 把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=mystudentList.xls");
        /*
        InputStream is = new FileInputStream("D:\\Master\\Learning\\DongLiJieDian\\SSM\\code\\crm-project\\serverDir\\studentList.xls");
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
        }
        is.close();
        */
        wb.write(outputStream);
        wb.close();
        outputStream.flush();
    }

    /**
     * 批量导入市场活动，先配置springMVC的文件上传解析器
     * @param activityFile
     * @return
     */
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session){
        // 当前用户
        User currentUser = (User) session.getAttribute(Contants.CURRENT_USER_KEY);
        // 返回信息对象
        ReturnObject retObject = new ReturnObject(Contants.RETURN_OBJECT_CODE_FAIL, null, null);
        try {
            // 把文件在服务器指定的目录中生成一个同样的文件，路径必须手动创建好，文件会自动创建
            // File file = new File("D:\\Master\\Learning\\DongLiJieDian\\SSM\\code\\crm-project\\serverDir\\" + activityFile.getOriginalFilename());
            // activityFile.transferTo(file);
            // 根据excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
            // InputStream is = new FileInputStream("D:\\Master\\Learning\\DongLiJieDian\\SSM\\code\\crm-project\\serverDir\\studentList.xls");

            InputStream is = activityFile.getInputStream();

            HSSFWorkbook wb = new HSSFWorkbook(is);
            // 根据wb获取HSSFSheet对象，封装了一页的所有信息
            HSSFSheet sheet = wb.getSheetAt(0);
            // 根据sheet获取HSSFRow对象，封装了一行的所有信息
            HSSFRow row = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            // sheet.getLastRowNum()；最后一行的下标
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(currentUser.getId());
                activity.setCreateTime(DateUtils.formatDataTime(new Date()));
                activity.setCreateBy(currentUser.getId());
                // 根据row获取HSSFCell对象，封装一列的所有信息
                HSSFCell cell = null;
                // row.getLastCellNum()；和行的不一样，是最后一列的下表 + 1
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    // 获取列中的数据
                    String value = HSSFUtils.getCellValueForStr(cell);
                    if (j == 0) {
                       activity.setName(value);
                    } else if (j == 1) {
                        activity.setStartDate(value);
                    } else if (j == 2) {
                        activity.setEndDate(value);
                    } else if (j == 3) {
                        activity.setCost(value);
                    } else if (j == 4) {
                        activity.setDescription(value);
                    }
                }
                // 每一行的所有列封装完成后，把activity保存到list中
                activityList.add(activity);
            }
            // 调用service方法，保存市场活动
            int refCount = activityService.saveCreateActivityByList(activityList);
            retObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            retObject.setRetData(refCount);
        } catch (IOException e) {
            e.printStackTrace();
            retObject.setMessage("系统忙，请稍后重试...");
        }
        // 返回响应信息
        return retObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id, HttpServletRequest request){
        // 调用service方法，查询数据
        Activity activity = activityService.queryActivityForDetail(id);
        List<ActivityRemark> remarks = activityRemarkService.queryActivityRemarkForDetailsByActitvityId(id);
        // 把数据保存到request作用域中
        request.setAttribute("activity", activity);
        request.setAttribute("remarks", remarks);
        // 请求转发到市场明细页面
        return "workbench/activity/detail";
    }
}
