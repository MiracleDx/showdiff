package com.datadiff.persist.service;

import org.springframework.web.multipart.MultipartFile;

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
}
