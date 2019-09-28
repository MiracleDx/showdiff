package com.datadiff.show.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel导入工具
 *
 * @author zhihaoyu
 */
@Slf4j
public class ExcelImportUtil {

	private static final String EXCEL_2003_SUFFIX = ".xls";
	private static final String EXCEL_2007_SUFFIX = ".xlsx";
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public static <T> List<T> getListFromExcel(String path, String[] headerProps,
											   Class<T> targetClass) {
		return getListFromExcel(path, headerProps, targetClass, DEFAULT_DATE_PATTERN);
	}

	/**
	 * 导入excel获取集合
	 *
	 * @param excel       待导入excel对象
	 * @param headerProps 与导入模板的表格表头对应的属性名数组（顺序一致，一一对应）
	 * @param targetClass 要将表格数据封装成的目标类信息
	 */
	public static <T> List<T> getListFromExcel(MultipartFile excel, String[] headerProps,
											   Class<T> targetClass) {
		return getListFromExcel(excel, headerProps, targetClass, DEFAULT_DATE_PATTERN);
	}

	/**
	 * 导入excel获取集合
	 * 注：此方法可以指定日期格式
	 *
	 * @param excel       待导入excel对象
	 * @param headerProps 与导入模板的表格表头对应的属性名数组（顺序一致，一一对应）
	 * @param targetClass 要将表格数据封装成的目标类信息
	 * @param datePattern 日期格式
	 */
	public static <T> List<T> getListFromExcel(MultipartFile excel, String[] headerProps,
											   Class<T> targetClass, String datePattern) {
		List<T> list = new ArrayList<>();
		try (Workbook workbook = getWorkBookByExcel(excel)) {
			list = getListFromWorkBook(workbook, headerProps, targetClass, datePattern);
		} catch (Exception e) {
			log.error("Error occurred when getting list from excel!", e);
		}
		return list;
	}

	public static <T> List<T> getListFromExcel(String path, String[] headerProps,
											   Class<T> targetClass, String datePattern) {
		List<T> list = new ArrayList<>();
		try (Workbook workbook = getWorkBookByPath(path)) {
			list = getListFromWorkBook(workbook, headerProps, targetClass, datePattern);
		} catch (Exception e) {
			log.error("Error occurred when getting list from excel!", e);
		}
		return list;
	}

	/**
	 * 根据要导入的excel的格式获取相应的workbook
	 *
	 * @param excel 要导入的excel
	 * @return 格式不正确时返回null
	 */
	private static Workbook getWorkBookByExcel(MultipartFile excel) throws IOException {
		String suffix = getSuffixByExcel(excel);
		InputStream inputStream = excel.getInputStream();
		return getWorkBook(suffix, inputStream);
	}

	private static Workbook getWorkBookByPath(String path) throws Exception {
		String suffix = getSuffix(path);
		InputStream inputStream = new FileInputStream(new File(path));
		return getWorkBook(suffix, inputStream);
	}

	private static Workbook getWorkBook(String suffix, InputStream ins) throws IOException {
		if (EXCEL_2003_SUFFIX.equals(suffix)) {
			return new HSSFWorkbook(ins);
		}
		if (EXCEL_2007_SUFFIX.equals(suffix)) {
			return new XSSFWorkbook(ins);
		}
		log.warn("The suffix of imported file is not .xls or .xlsx which are required!");
		return null;
	}

	private static <T> List<T> getListFromWorkBook(Workbook workbook, String[] headerProps,
												   Class<T> targetClass, String datePattern) throws Exception {
		List<T> list = new ArrayList<>();
		if (workbook == null) {
			return list;
		}
		Sheet sheet = workbook.getSheetAt(0);
		return getListFromSheet(sheet, headerProps, targetClass, datePattern);
	}

	/**
	 * 从表格中获取数据集合
	 *
	 * @param sheet       表格
	 * @param headerProps 与导入模板的表格表头对应的属性名数组
	 * @param targetClass 要将表格数据封装成的目标类信息
	 * @param datePattern 日期格式
	 */
	private static <T> List<T> getListFromSheet(Sheet sheet, String[] headerProps,
												Class<T> targetClass, String datePattern) throws Exception {
		List<T> dataList = new ArrayList<>();
		T target;
		// 第一行为表头，故从第二行开始导入
		int rowNum = 1;
		while (rowNum <= sheet.getLastRowNum()) {
			Row row = sheet.getRow(rowNum);
			target = targetClass.newInstance();
			setProperties(target, headerProps, row, datePattern);
			dataList.add(target);
			rowNum++;
		}
		return dataList;
	}

	/**
	 * 获取要导入的excel的后缀
	 *
	 * @param excel 要导入的excel
	 */
	private static String getSuffixByExcel(MultipartFile excel) {
		String filename = excel.getOriginalFilename();
		return getSuffix(filename);
	}

	private static String getSuffix(String str) {
		String period = ".";
		if (StringUtils.isBlank(str) || !str.contains(period)) {
			return "";
		}
		int index = str.lastIndexOf(period);
		return str.substring(index);
	}

	/**
	 * 设置对象的多个属性
	 *
	 * @param target      目标对象
	 * @param headerProps 和表格表头对应的属性名数组
	 * @param row         表格行
	 * @param datePattern 日期格式
	 */
	private static <T> void setProperties(T target, String[] headerProps,
										  Row row, String datePattern) throws Exception {
		int cellNum = 0;
		for (String fieldName : headerProps) {
			Cell cell = row.getCell(cellNum);
			String text = cell.getStringCellValue();
			setProperty(target, fieldName, text, datePattern);
			cellNum++;
		}
	}

	/**
	 * 设置对象的某个属性
	 *
	 * @param target      目标对象
	 * @param fieldName   目标属性
	 * @param text        当前cell的文本值
	 * @param datePattern 日期格式
	 */
	private static <T> void setProperty(T target, String fieldName,
										String text, String datePattern) throws Exception {
		Field field = getDeclaredFieldByFieldName(target, fieldName);
		Class fieldClass = field.getType();
		if (fieldClass == String.class) {
			field.set(target, text);
			return;
		}
		if (fieldClass == Date.class) {
			DateFormat df = new SimpleDateFormat(datePattern);
			Date date = df.parse(text);
			field.set(target, date);
			return;
		}
		Method valueOfMethod = fieldClass.getDeclaredMethod("valueOf", String.class);
		Object value = valueOfMethod.invoke(fieldClass, text);
		field.set(target, value);
	}

	/**
	 * 根据域名称获取对象的某个域
	 *
	 * @param target    目标对象
	 * @param fieldName 域名称
	 */
	private static <T> Field getDeclaredFieldByFieldName(T target, String fieldName)
			throws NoSuchFieldException {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field;
	}

}
