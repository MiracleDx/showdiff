package com.datadiff.show.service;

import com.datadiff.show.common.ChangeStatus;
import com.datadiff.show.common.Data;
import com.datadiff.show.entity.DataInfo;
import com.datadiff.show.entity.WebUrl;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataDealService 数据处理服务
 * Created by: dongxiang
 * Description:
 * Created in: 2019-09-27 16:14
 * Modified by:
 */
@Service
public class DataDealService {

	/**
	 * 展示数据
	 * @return
	 */
	public List<DataInfo> show() {
		int size = Data.basics.size();
		
		List<DataInfo> dataInfos = new ArrayList<>(size);
		
		// 获取基础数据
		Data.basics.forEach(e -> {
			DataInfo dataInfo = new DataInfo();
			dataInfo.setId(e.getId())
					.setUrl(e.getUrl())
					.setIsChange("")
					.setDataContent("");
			dataInfos.add(dataInfo);
		});
		return dataInfos;
	}
	
	/**
	 * 比较
	 */
	public List<DataInfo> diff(List<DataInfo> datas) throws IOException {
		for (DataInfo dataInfo : datas) {
			Integer id = dataInfo.getId();
			// 获取新的html数据
			String html = this.catchHtml(dataInfo.getUrl());

			String basicPath = this.getBasicPath(id);
			// todo 读取文件中的信息 先使用基础的html信息
			String basicHtml = basicPath;
			
			// 判断是否被篡改 true未篡改 false篡改
			boolean result = StringUtils.equals(html, basicHtml);
			dataInfo.setIsChange(result ? ChangeStatus.NO.getStatus() : ChangeStatus.YES.getStatus());
			
			// todo 判断敏感字符   可以先不做
		}
		
		return datas;
	}

	/**
	 * 设置基准数据
	 */
	public void setBasicDatas(List<DataInfo> datas) throws IOException {
		for (DataInfo dataInfo : datas) {
			Integer id = dataInfo.getId();
			// 获取新的html数据
			String html = this.catchHtml(dataInfo.getUrl());
			
			// 获取初始化的基准数据路径
			String basicPath = this.getBasicPath(id);
			
			// todo 替换原文件中的内容  先替换原path做测试
			WebUrl webUrl = Data.basics.get(id);
			webUrl.setUrl(dataInfo.getUrl());
		}
	}

	/**
	 * 抓取页面信息
	 * @param resource url信息
	 */
	public String catchHtml(String resource) throws IOException {
		Document doc = Jsoup.connect(resource)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.90 Safari/537.36").get();
		return doc.toString();
	}

	/**
	 * 获取基准数据路径
	 * @param id 当前uri的id
	 * @return
	 */
	public String getBasicPath(Integer id) {
		WebUrl webUrl = Data.basics.get(id);
		return "";
	}
	
}
