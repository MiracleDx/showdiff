package com.datadiff;

import com.datadiff.global.ExcelInfoCache;
import com.datadiff.show.common.Data;
import com.datadiff.show.entity.WebUrl;
import com.datadiff.show.service.DataDealServie;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ShowApplication implements ApplicationRunner {

	@Resource
	private DataDealServie dataDealServie;

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
		//todo 从文件中加载uri路径
//		List<String> uris = Arrays
//				.asList("http://www.baidu.com",
//						"http://club.epicc.com.cn/index",
//						"https://baike.baidu.com");
        List<String> uris = ExcelInfoCache.getUrls();
		int length = uris.size();

		WebUrl webUrl;
		List<WebUrl> webUrises = new ArrayList<>(length);
		// 抓取页面数据生成基准数据文件
		for (int i = 0; i < length; i++) {
			webUrl = new WebUrl();
			webUrl.setId(i);
			String uri = uris.get(i);
			webUrl.setUrl(uri);
			// 获取当前页面元素
			String html = dataDealServie.catchHtml(uri);
			// todo 存储路径 先写成html已做测试
			String path = html;
			webUrl.setPath(path);
			Data.basics.add(webUrl);
		}
	}
}
