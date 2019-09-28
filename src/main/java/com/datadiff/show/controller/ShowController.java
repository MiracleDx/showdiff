package com.datadiff.show.controller;

import com.datadiff.show.entity.DataInfo;
import com.datadiff.show.service.DataDealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	private DataDealService dataDealService;
	
	@GetMapping("/")
	public List<DataInfo> show() {
		return dataDealService.show();
	}
	
	@PostMapping("/diff")
	public List<DataInfo> diff(@RequestBody List<DataInfo> dataInfos) throws IOException {
		return dataDealService.diff(dataInfos);
	}
	
	@PostMapping("/set")
	public List<DataInfo> setData(@RequestBody DataInfo dataInfo) throws IOException {
		return dataDealService.setBasicDatas(dataInfo);
	}
}
