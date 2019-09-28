package com.datadiff.show.common;

import com.datadiff.show.entity.DataInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Data 数据类
 * Created by: dongxiang
 * Description:
 * Created in: 2019-09-27 21:28
 * Modified by:
 */
public class Data {

	/**
	 * 元数据
	 */
	public static Map<Integer, DataInfo> basics = new ConcurrentHashMap<>();
}
