package com.datadiff.show;

import com.datadiff.show.common.ChangeStatus;
import com.datadiff.show.common.Data;
import com.datadiff.show.common.ExcelImportUtil;
import com.datadiff.show.entity.DataInfo;
import com.datadiff.show.entity.ExcelInfo;
import com.datadiff.show.service.DataDealService;
import com.datadiff.show.service.PersistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

@Slf4j
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
		// 判断Excel是否存在
		persistService.checkFile();
		// 跳过https
		checkQuietly();
		//从文件中加载基础信息
		List<ExcelInfo> excelInfos = ExcelImportUtil.getListFromExcel(excelPath, headProps, ExcelInfo.class);
		int length = excelInfos.size();
		
		// 如果存在第一个文件就不加载默认数据
		File file = new File(savePath + File.separator + "0");
		if (file.exists()) {
			// 清除缓存数据
			Data.basics.clear();
			initDataFromFile(excelInfos, length);
			log.info("init data from file");
		} else {
			// 从页面中爬取数据
			initDataFromHttp(excelInfos, length);
			log.info("init data from http");
		}
		Data.basics.forEach((k, v) -> log.info("catch data: {}", v));
	}

	/**
	 * 从文件中加载数据
	 */
	private void initDataFromFile(List<ExcelInfo> excelInfos, int length) throws IOException {
		DataInfo data;
		for (int i = 0; i < length; i++) {
			// 表格数据
			ExcelInfo excelInfo = excelInfos.get(i);
	
			// 缓存数据
			data = new DataInfo();
			data.setId(i);
	
			// 获取应用系统名称
			String name = excelInfo.getName();
			data.setName(name);
	
			// 获取url
			String url = excelInfo.getUrl();
			data.setUrl(url);
	
			// 获取对比文本
			String html = dataDealService.catchHtml(url);
			html = persistService.getLocalHtml(html, savePath, i);
			
			String localContent = persistService.readFile(savePath + File.separator + i);
			
			dataDealService.different(html, localContent, data);
			Data.basics.put(i, data);
		}
	}

	/**
	 *  抓取页面数据生成基准数据文件
	 */
	private void initDataFromHttp(List<ExcelInfo> excelInfos, int length) throws IOException {
		DataInfo data;
		for (int i = 0; i < length; i++) {
			// 表格数据
			ExcelInfo excelInfo = excelInfos.get(i);

			// 缓存数据
			data = new DataInfo();
			data.setId(i);

			// 获取应用系统名称
			String name = excelInfo.getName();
			data.setName(name);

			// 获取url
			String url = excelInfo.getUrl();
			data.setUrl(url);

			// 获取当前页面元素
			String html = dataDealService.catchHtml(url);
			persistService.saveText(html, savePath + File.separator + i);
			
			// 设置标志位
			data.setIsChange(ChangeStatus.NO.getStatus());
			Data.basics.put(i, data);
		}
	}

	/**
	 * 跳过https认证
	 */
	public static void checkQuietly() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname,
									  SSLSession session) {
					return true;
				}
			});
			
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			}}, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
