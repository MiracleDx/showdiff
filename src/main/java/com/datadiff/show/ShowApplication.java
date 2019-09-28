package com.datadiff.show;

import com.datadiff.show.common.Data;
import com.datadiff.show.common.ExcelImportUtil;
import com.datadiff.show.entity.ExcelInfo;
import com.datadiff.show.entity.WebUrl;
import com.datadiff.show.service.DataDealService;
import com.datadiff.show.service.PersistService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ShowApplication implements ApplicationRunner {

	@Resource
	private DataDealService dataDealService;
	
	@Resource
	private PersistService persistService;
	
	@Value("${showdiff.read.path}")
	private String excelPath;

	@Value("${showdiff.header.props}")
	private String[] headProps;
	
	@Value("${showdiff.save.path}")
	private String savePath;
	
	public static void main(String[] args) {
		SpringApplication.run(ShowApplication.class, args);
	}

	/**
	 * 初始化加载路径
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		File file1 = new File(excelPath);
		System.out.println(file1.exists());
		InputStream in = new FileInputStream(file1);
		// 如果存在第一个文件就不加载默认数据
		File file= new File("D:" + File.pathSeparator + "0");
		if (file.exists()) {
			// todo 从文件中加载数据
			
		} else {
			//从文件中加载基础信息
			List<ExcelInfo> excelInfos = ExcelImportUtil.getListFromExcel(excelPath, headProps, ExcelInfo.class);
			
			int length = excelInfos.size();

			WebUrl webUrl;
			List<WebUrl> webUrises = new ArrayList<>(length);
			// 抓取页面数据生成基准数据文件
			for (int i = 0; i < length; i++) {
				// 表格数据
				ExcelInfo excelInfo = excelInfos.get(i);
				
				// 缓存数据
				webUrl = new WebUrl();
				webUrl.setId(i);
				
				// 获取应用系统名称
				String name = excelInfo.getName();
				webUrl.setName(name);
				
				// 获取url
				String url = excelInfo.getUrl();
				webUrl.setUrl(url);
				
				// 获取当前页面元素
				String html = dataDealService.catchHtml(url);
				persistService.saveText(html, savePath + File.separator + i);
				Data.basics.add(webUrl);
			}
		}
		Data.basics.forEach(System.out::println);
	}
}
