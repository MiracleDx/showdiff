package com.datadiff.show.service;

import com.datadiff.show.common.ExcelImportUtil;
import com.datadiff.show.entity.ExcelInfo;
import com.datadiff.show.entity.WebUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;


/**
 * PersistService
 * Created by: dongxiang
 * Description:
 * Created in: 2019-09-28 14:47
 * Modified by:
 */
@Slf4j
@Service
public class PersistService {
	
	/**
	 * 保存信息
	 */
	public void saveText(String html, String savePath) {
		try (FileOutputStream stream = new FileOutputStream(savePath, true)) {
			stream.write(html.getBytes());
		} catch (Exception e) {
			log.error("Error occurred when saving entity!", e);
		}
	}

}
