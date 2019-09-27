package com.datadiff.show.controller;

import com.datadiff.show.entity.DataInfo;
import com.datadiff.show.service.DataDealServie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ShowController
 * Created by: dongxiang
 * Description:
 * Created in: 2019-09-27 16:15
 * Modified by:
 */
@RestController
public class ShowController {
	
	@Resource
	private DataDealServie dataDealServie;
	
	@GetMapping("/")
	public List<DataInfo> show() {
		return dataDealServie.show();
	}
	
	@GetMapping("/diff")
	public List<DataInfo> diff() throws IOException {
		// 测试数据
		List<DataInfo> dataInfos = new ArrayList<>();
		DataInfo dataInfo = new DataInfo();
		dataInfo.setId(0)
				.setUrl("http://www.baidu.com");

		DataInfo dataInfo1 = new DataInfo();
		dataInfo1.setId(1)
				.setUrl("http://club.epicc.com.cn/index");

		DataInfo dataInfo2 = new DataInfo();
		dataInfo2.setId(2)
				.setUrl("https://baike.baidu.com");
		
		dataInfos.add(dataInfo);
		dataInfos.add(dataInfo1);
		dataInfos.add(dataInfo2);
		return dataDealServie.diff(dataInfos);
	}
	
	@GetMapping("/set")
	public void setData() throws IOException {
		// 测试数据
		List<DataInfo> dataInfos = new ArrayList<>();
		DataInfo dataInfo = new DataInfo();
		dataInfo.setId(0)
				.setUrl("http://www.baidu.com");

		DataInfo dataInfo1 = new DataInfo();
		dataInfo1.setId(1)
				.setUrl("https://baike.baidu.com");

		dataInfos.add(dataInfo);
		dataInfos.add(dataInfo1);
		dataDealServie.setBasicDatas(dataInfos);
	}
}
