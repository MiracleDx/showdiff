package com.datadiff.show.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * DataInfo 页面展示信息
 * Created by: dongxiang
 * Description:
 * Created in: 2019-09-27 17:39
 * Modified by:
 */
@Data
@Accessors(chain = true)
public class DataInfo {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * url
	 */
	private String url;

	/**
	 * 是否改变
	 */
	private String isChange;

	/**
	 * 包含的敏感词
	 */
	private String dataContent;
}
