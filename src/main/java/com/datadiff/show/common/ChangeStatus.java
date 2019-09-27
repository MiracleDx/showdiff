package com.datadiff.show.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ChangeStatus 是否被篡改枚举类
 * Created by: dongxiang
 * Description:
 * Created in: 2019-09-27 21:46
 * Modified by:
 */
@Getter
@AllArgsConstructor
public enum ChangeStatus {
	
	YES("是"),
	NO("否");

	private String status;
}
