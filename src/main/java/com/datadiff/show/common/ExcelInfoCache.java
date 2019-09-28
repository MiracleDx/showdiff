//package com.datadiff.show.common;
//
//import com.datadiff.show.entity.ExcelInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 表格信息缓存
// *
// * @author zhy
// */
//@Slf4j
//public class ExcelInfoCache {
//
//	/**
//	 * 表格信息缓存
//	 */
//	private static List<ExcelInfo> CACHE;
//
//	static {
//		init();
//	}
//
//	/**
//	 * 初始化
//	 */
//	private static void init() {
//		String path = PropertiesReader.get("showdiff.read.path");
//		String[] headerProps = PropertiesReader.getArray("showdiff.header.props");
//		CACHE = ExcelImportUtil.getListFromExcel(path, headerProps, ExcelInfo.class);
//		log.info("Excel info cache init succeed!");
//	}
//
//	/**
//	 * 获取表格信息缓存
//	 *
//	 * @return 表格信息缓存
//	 */
//	public synchronized static List<ExcelInfo> getCache() {
//		if (CACHE == null) {
//			init();
//		}
//		return CACHE;
//	}
//
//	/**
//	 * 获取url列表
//	 *
//	 * @return url列表
//	 */
//	public synchronized static List<String> getUrls() {
//		if (CACHE == null) {
//			init();
//		}
//		List<String> urls = new ArrayList<>();
//		for (ExcelInfo info : CACHE) {
//			String url = info.getUrl();
//			if (StringUtils.isBlank(url)) {
//				continue;
//			}
//			urls.add(url);
//		}
//		return urls;
//	}
//
//	/**
//	 * 清除表格信息缓存
//	 */
//	public synchronized static void clean() {
//		CACHE = null;
//		log.info("Excel info cache is cleaned up!");
//	}
//}