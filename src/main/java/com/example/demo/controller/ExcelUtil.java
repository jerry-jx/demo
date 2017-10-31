package com.example.demo.controller;

import static com.example.demo.controller.GetDownloadUrlFromS3ByUrl1.getSaveFileUrl;
import static com.example.demo.controller.GetDownloadUrlFromS3ByUrl1.getUploadToS3FileDownloadUrl;

import com.example.demo.entity.User;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


@Slf4j
public class ExcelUtil {

    private static final int EXCEL_NUM_LIMIT = 200000;


    public static void main(String[] args) {
        String existingBucketName = "aws-mgt-storage";
        String keyName = "reports/";
        // private static final String filePath = "D:/soft/PMD-Intellij.zip";//https://download-cf.jetbrains.com/idea/ideaIU-2017.2.5.exe
        String accessKey = "AKIAJ5HKYWQNUZEINUGQ";
        String secrectKey = "VgeMknpoOQe1D50PQFufGm8TEPu7rhMYo0j4J8DT";
        int expirationPeriod = 7;
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setId((long) 1);
        user.setUserId((long) 11);
        user.setPwd("111");
        list.add(user);
        User user1 = new User();
        user1.setId((long) 2);
        user1.setUserId((long) 22);
        user1.setPwd("222");
        list.add(user1);

        URL url = getS3ExcelFileUrl(accessKey,  secrectKey,  existingBucketName,    expirationPeriod, "bbb", list, User.class);
    }
    /**
     * @param accessKey
     * @param secrectKey
     * @param existingBucketName
     * @param expirationPeriod   设置下载路径有效期（天）
     * @param fileName             文件名称（不包含后缀）
     * @param list
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> URL getS3ExcelFileUrl(String accessKey, String secrectKey, String existingBucketName,
        int expirationPeriod, String fileName, List<T> list, Class<T> cls) {
        SXSSFWorkbook workBook = assembleExcel(list, cls);
        File tempFileAllUrl = null;
        try {
            tempFileAllUrl = getSaveFileUrl(workBook, fileName, ".xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
        URL url = getUploadToS3FileDownloadUrl(accessKey, secrectKey, existingBucketName, "reports/", tempFileAllUrl, expirationPeriod);
        return url;
    }


    /**
     * 获取excel本地下载路径
     *
     * @param response
     * @param fileName
     * @param list
     * @param cls
     * @param <T>
     */
    public static <T> void writeExcel(HttpServletResponse response, String fileName, List<T> list, Class<T> cls) {
        SXSSFWorkbook workBook = assembleExcel(list, cls);
        responseStream(workBook, response, fileName);
    }


    /**
     * 通用导出方法
     *
     * @param list
     * @param cls
     * @param <T>
     * @return
     */
    private static <T> SXSSFWorkbook assembleExcel(List<T> list, Class<T> cls) {
        // 1.创建一个workbook，对应一个Excel文件
        SXSSFWorkbook workBook = new SXSSFWorkbook();
        int page = list.size() % EXCEL_NUM_LIMIT == 0 ? list.size() / EXCEL_NUM_LIMIT : list.size() / EXCEL_NUM_LIMIT + 1;
        log.info("sheet数量为：{}", page);
        for (int m = 0; m < page; m++) {

            Field[] fields = cls.getDeclaredFields();

            ArrayList<String> headList = new ArrayList<>();

            for (Field f : fields) {
                ExcelField field = f.getAnnotation(ExcelField.class);
                if (field != null) {
                    headList.add(field.title());
                }
            }

            CellStyle style = getCellStyle(workBook);
            Sheet sheet = workBook.createSheet();
            workBook.setSheetName(m, "sheet" + String.valueOf(m + 1));
            Header header = sheet.getHeader();
            header.setCenter("sheet");

            // 设置Excel表的第一行即表头
            Row row = sheet.createRow(0);
            for (int i = 0; i < headList.size(); i++) {
                Cell headCell = row.createCell(i);
                headCell.setCellType(CellType.STRING);
                headCell.setCellStyle(style);//设置表头样式
                headCell.setCellValue(String.valueOf(headList.get(i)));
                sheet.setColumnWidth(i, 15 * 256);
            }

            int rowIndex = 1;
            log.info("开始创建sheet{}", m);
            int start = (EXCEL_NUM_LIMIT * m);
            int end = list.size() - start >= EXCEL_NUM_LIMIT ? start + EXCEL_NUM_LIMIT : list.size();
            log.info("开始{},结束{}", start, end);
            for (int i = start; i < end; i++) {
                Row rowData = sheet.createRow(rowIndex);//创建数据行
                T q = list.get(i);
                Field[] ff = q.getClass().getDeclaredFields();
                int j = 0;
                for (Field f : ff) {
                    ExcelField field = f.getAnnotation(ExcelField.class);
                    if (field == null) {
                        continue;
                    }
                    f.setAccessible(true);
                    Object obj = null;
                    try {
                        obj = f.get(q);
                    } catch (IllegalAccessException e) {
                        log.error("", e);
                    }
                    Cell cell = rowData.createCell(j);
                    cell.setCellType(CellType.STRING);
                    // 当数字时
                    if (obj instanceof Integer) {
                        cell.setCellValue((Integer) obj);
                    }
                    // 当Long时
                    if (obj instanceof Long) {
                        cell.setCellValue((Long) obj);
                    }
                    // 当为字符串时
                    if (obj instanceof String) {
                        cell.setCellValue((String) obj);
                    }
                    // 当为布尔时
                    if (obj instanceof Boolean) {
                        cell.setCellValue((Boolean) obj);
                    }
                    // 当为时间时
                    if (obj instanceof Date) {
                        cell.setCellValue(getFormatDate((Date) obj));
                    }
                    // 当为时间时
                    if (obj instanceof Calendar) {
                        cell.setCellValue((Calendar) obj);
                    }
                    // 当为小数时
                    if (obj instanceof Double) {
                        cell.setCellValue((Double) obj);
                    }
                    // 当为BigDecimal
                    if (obj instanceof BigDecimal) {
                        cell.setCellValue(Double.parseDouble(obj.toString()));
                    }
                    // 当ZonedDateTime
                    if (obj instanceof ZonedDateTime) {
                        cell.setCellValue(((ZonedDateTime) obj).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                    }
                    j++;
                }
                rowIndex++;
            }
        }
        return workBook;
    }

    /**
     * 返回文件流
     *
     * @param workBook
     * @param response
     * @param fileName
     */
    private static void responseStream(SXSSFWorkbook workBook, HttpServletResponse response, String fileName) {
        OutputStream outputStream = null;
        try {
            response.setContentType("application/vnd.ms-excel; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes("UTF-8"), "ISO-8859-1"));
            response.setCharacterEncoding("utf-8");
            //根据传进来的file对象创建可写入的Excel工作薄
            outputStream = response.getOutputStream();
            log.info("数据导出Excel成功！");
            workBook.write(outputStream);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }


    /**
     * 设置表头样式
     *
     * @param wb
     * @return
     */
    public static CellStyle getCellStyle(SXSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);//设置字体大小
        font.setBold(true);//加粗
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());// 设置背景色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);//让单元格居中
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION);// 左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        style.setWrapText(true);//设置自动换行
        style.setFont(font);
        return style;
    }


    public static String getFormatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }



}
