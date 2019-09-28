package com.datadiff.show.service;

import ch.qos.logback.classic.Logger;
import com.datadiff.show.common.ChangeStatus;
import com.datadiff.show.common.Data;
import com.datadiff.show.entity.DataInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.dnd.DropTarget;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataDealService 数据处理服务
 * Created by: dongxiang
 * Description:
 * Created in: 2019-09-27 16:14
 * Modified by:
 */
@Slf4j
@Service
public class DataDealService {
	
	@Resource
	private PersistService persistService;

	@Value("${showdiff.save.path}")
	private String savePath;

	/**
	 * 展示数据
	 * @return
	 */
	public List<DataInfo> show() {
		// 获取缓存中的信息
		List<DataInfo> dataInfos = new ArrayList<>(Data.basics.size());
		Data.basics.forEach((k, v) -> dataInfos.add(v));
		return dataInfos;
	}
	
	/**
	 * 比较
	 */
	public List<DataInfo> diff(List<DataInfo> datas) throws IOException {
		log.info("diff start");
		for (DataInfo dataInfo : datas) {
			Integer id = dataInfo.getId();
			// 获取新的html数据
			String html = this.catchHtml(dataInfo.getUrl());
			html = persistService.getLocalHtml(html, savePath, id);
			
			// 读取文件中的信息
			String basic = persistService.readFile(savePath + id);
			different(html, basic, dataInfo);
			// 替换缓存中的信息
			Data.basics.put(id, dataInfo);
			
			// todo 判断敏感字符
		}
		
		log.info("diff finish");
		return show();
	}

	/**
	 * 设置基准数据
	 */
	public List<DataInfo> setBasicDatas(DataInfo dataInfo) throws IOException {
		log.info("set start");
		Integer id = dataInfo.getId();
		// 获取新的html数据
		String html = this.catchHtml(dataInfo.getUrl());
		persistService.saveText(html, savePath + id);
		// 重新设置标志位
		dataInfo.setIsChange(ChangeStatus.NO.getStatus());
		Data.basics.put(id, dataInfo);
		log.info("set finish");
		return show();
	}

	/**
	 * 抓取页面信息
	 * @param resource url信息
	 */
	public String catchHtml(String resource) throws IOException {
		Document doc = Jsoup.connect(resource)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.90 Safari/537.36")
				.get();
		return doc.toString();
	}

	/**
	 * 判断两段文本是否一致
	 */
	public void different(String html, String local, DataInfo dataInfo) {
		// 判断是否被篡改 true未篡改 false篡改  因为本地存储会合并成一行 分行读取需要增加换行符
		boolean result = StringUtils.equals(html, local);
		dataInfo.setIsChange(result ? ChangeStatus.NO.getStatus() : ChangeStatus.YES.getStatus());
	}
}
