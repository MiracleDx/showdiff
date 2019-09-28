package com.datadiff.persist.controller;

import com.datadiff.persist.service.PersistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 持久化控制器
 *
 * @author zhy
 */
@Slf4j
@RestController("persist")
public class PersistController {

    @Resource
    private PersistService persistService;

    /**
     * 导入表格
     *
     * @param excel 表格
     * @return 结果信息
     */
    @PostMapping("importExcel")
    public String importExcel(MultipartFile excel) {
        try {
            persistService.importExcel(excel);
            return "success";
        } catch (Exception e) {
            log.error("Error occurred when importing excel!", e);
            return "error";
        }
    }
}
