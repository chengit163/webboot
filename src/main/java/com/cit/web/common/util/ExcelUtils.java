package com.cit.web.common.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelUtils
{

    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final int SIZE = 100;

    private static XSSFSheet createSheet(XSSFWorkbook wb, String name, Field[] fields)
    {
        XSSFSheet sheet = wb.createSheet(name);
        //
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 居中
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        XSSFFont font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        //
        XSSFRow row = sheet.createRow(0);
        int colIndex = 0;
        for (Field field : fields)
        {
            // api 段信息 Set the width (in units of 1/256th of a character width)
            sheet.setColumnWidth(colIndex, 20 * 256);
            String title = null;
            ApiModelProperty fieldTitle = field.getAnnotation(ApiModelProperty.class);
            if (fieldTitle != null)
            {
                title = fieldTitle.value();
            } else
            {
                title = field.getName();
            }
            //
            Cell cell = row.createCell(colIndex);
            cell.setCellStyle(style);
            cell.setCellValue(title);
            colIndex++;
        }
        return sheet;
    }

    public static <T extends Serializable> void export(HttpServletResponse response, Class<T> clazz)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        //
        String fileName = null;
        ApiModel clazzTitle = clazz.getAnnotation(ApiModel.class);
        if (clazzTitle != null)
        {
            fileName = clazzTitle.value() + "模板.xlsx";
        } else
        {
            fileName = clazz.getSimpleName() + "模板.xlsx";
        }
        try
        {
            response.setContentType("application/vnd.ms-excel;chartset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));

            Field[] fields = clazz.getDeclaredFields();
            createSheet(wb, SDF_DATE.format(new Date()), fields);

            wb.write(response.getOutputStream());
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                wb.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public static <T extends Serializable> void export(HttpServletResponse response, List<T> list, Class<T> clazz)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        //
        String fileName = null;
        ApiModel clazzTitle = clazz.getAnnotation(ApiModel.class);
        if (clazzTitle != null)
        {
            fileName = clazzTitle.value() + SDF_DATE_TIME.format(new Date()) + ".xlsx";
        } else
        {
            fileName = clazz.getSimpleName() + SDF_DATE_TIME.format(new Date()) + ".xlsx";
        }
        try
        {
            response.setContentType("application/vnd.ms-excel;chartset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));

            Field[] fields = clazz.getDeclaredFields();
            if (list != null && !list.isEmpty())
            {
                XSSFCellStyle style = wb.createCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER);// 居中
                XSSFSheet sheet = null;
                int rowIndex = 0, colIndex = 0;
                for (int i = 0; i < list.size(); i++)
                {
                    if (i % SIZE == 0)
                    {
                        int index = i / SIZE;
                        sheet = createSheet(wb, "Sheet" + index, fields);
                        rowIndex = 1;
                    }
                    // 创建一行data
                    T t = list.get(i);
                    XSSFRow row = sheet.createRow(rowIndex);
                    for (Field field : fields)
                    {
                        field.setAccessible(true);
                        Object obj = field.get(t);
                        String value = obj == null ? "" : obj.toString();
                        //
                        Cell cell = row.createCell(colIndex);
                        cell.setCellStyle(style);
                        cell.setCellValue(value);
                        colIndex++;
                    }
                    colIndex = 0;
                    rowIndex++;
                }
            } else
            {
                createSheet(wb, "Sheet", fields);
            }
            wb.write(response.getOutputStream());
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                wb.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
