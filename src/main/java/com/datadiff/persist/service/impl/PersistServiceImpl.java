package com.datadiff.persist.service.impl;

import com.datadiff.persist.entity.ExcelInfo;
import com.datadiff.persist.service.PersistService;
import com.datadiff.util.ExcelImportUtil;
import com.datadiff.util.PropertiesReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 持久化实现
 *
 * @author zhy
 */
@Slf4j
@Service
public class PersistServiceImpl implements PersistService {

    private static final String SAVE_PATH = PropertiesReader.get("showdiff.save.path");

    /**
     * 导入表格
     *
     * @param excel 表格
     */
    @Override
    public void importExcel(MultipartFile excel) {
        String[] headProps = PropertiesReader.getArray("showdiff.read.path");
        List<ExcelInfo> list = ExcelImportUtil.
                getListFromExcel(excel, headProps, ExcelInfo.class);
        saveExcelInfos(list);
    }

    /**
     * 保存表格信息集合
     *
     * @param infos 表格信息集合
     */
    private void saveExcelInfos(Collection<ExcelInfo> infos) {
        try (FileOutputStream stream = new FileOutputStream(SAVE_PATH, true)) {
            for (ExcelInfo entity : infos) {
                saveExcelInfo(entity, stream);
            }
        } catch (Exception e) {
            log.error("Error occurred when saving entities!", e);
        }
    }

    /**
     * 保存表格信息
     *
     * @param info 表格信息
     */
    private void saveExcelInfo(ExcelInfo info) {
        try (FileOutputStream stream = new FileOutputStream(SAVE_PATH, true)) {
            saveExcelInfo(info, stream);
        } catch (Exception e) {
            log.error("Error occurred when saving entity!", e);
        }
    }

    /**
     * 保存表格信息
     *
     * @param info   表格信息
     * @param stream 文件输出流
     */
    private void saveExcelInfo(ExcelInfo info, FileOutputStream stream)
            throws IOException {
        String text = "\r\n" + info.getName() + "|" + info.getUrl();
        stream.write(text.getBytes());
    }

}
