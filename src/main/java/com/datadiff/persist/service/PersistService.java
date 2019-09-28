package com.datadiff.persist.service;

import com.datadiff.persist.entity.ExcelInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 持久化接口
 *
 * @author zhy
 */
public interface PersistService {

    /**
     * 导入表格
     *
     * @param excel 表格
     * @throws Exception 文件异常
     */
    void importExcel(MultipartFile excel) throws Exception;

    /**
     * 读取本地存储文件获取表格信息列表
     *
     * @return 表格信息列表
     */
    List<ExcelInfo> readLocal();
}
