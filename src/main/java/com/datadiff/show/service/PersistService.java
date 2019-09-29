package com.datadiff.show.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;


/**
 * PersistService
 * Created by: dongxiang
 * Description:
 * Created in: 2019-09-28 14:47
 * Modified by:
 */
@Slf4j
@Service
public class PersistService {

	private static final String SEPARATOR = ",";

	@Value("${showdiff.save.path}")
	private String savePath;

	@Value("${showdiff.read.path}")
	private String excelPath;
	
	/**
	 * 保存信息
	 */
	public void saveText(String html, String savePath) {
		// 如果文件就先删除，防止重复写入
		File file = new File(savePath);
		if (file.exists()) {
			file.delete();
		}
		
		try (FileOutputStream stream = new FileOutputStream(savePath, true)) {
			stream.write(html.getBytes());
		} catch (Exception e) {
			log.error("Error occurred when saving entity!", e);
		}
	}

	/**
	 * 读取文本 
	 */
	public String readFile(String filePath) {
		StringBuilder sb = new StringBuilder();
		try (FileReader reader = new FileReader(filePath);
			 BufferedReader br = new BufferedReader(reader)) {
			String line;
			// 一次读入一行数据
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 *  将html转成本地文件读取 
	 */
	public String getLocalHtml(String html, String savePath, Integer id) {
		// 先写入再读取比较  因为有换行对比问题 暂时无法解决
		saveText(html, savePath + File.separator + id + "bak");
		return readFile(savePath + File.separator + id + "bak");
	}

	/**
	 *  不存在就创建并且复制基础数据到指定路径
	 */
	public void checkFile() throws IOException {
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
			checkExcel();
		} else {
			checkExcel();
		}
	}
	
	private void checkExcel() throws IOException {
		File excel = new File(excelPath);
		if (!excel.exists()) {
			InputStream stream = getClass().getClassLoader().getResourceAsStream("excel.xlsx");
			FileUtils.copyInputStreamToFile(stream, excel);
		}
	}
}
