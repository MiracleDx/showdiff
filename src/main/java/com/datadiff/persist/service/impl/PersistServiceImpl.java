package com.datadiff.persist.service.impl;

import com.datadiff.persist.entity.ExcelInfo;
import com.datadiff.persist.service.PersistService;
import com.datadiff.util.ExcelImportUtil;
import com.datadiff.util.PropertiesReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
    private static final String SEPARATOR = ",";

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
        String text = "\r\n" + info.getName() + SEPARATOR + info.getUrl();
        stream.write(text.getBytes());
    }

    /**
     * 读取本地存储文件获取表格信息列表
     *
     * @return 表格信息列表
     */
    @Override
    public List<ExcelInfo> readLocal() {
        List<ExcelInfo> infos = new ArrayList<>();
        try (BufferedReader bufferedReader = getBufferedReader()) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (StringUtils.isBlank(line) || !line.contains(SEPARATOR)) {
                    continue;
                }
                ExcelInfo info = getExcelInfoFromLine(line);
                infos.add(info);
            }
        } catch (Exception e) {
            log.error("Error occurred when reading dictionary! ", e);
        }
        return infos;
    }

    /**
     * 获取BufferedReader
     *
     * @return BufferedReader实例
     */
    private BufferedReader getBufferedReader() throws FileNotFoundException {
        String path = PropertiesReader.get("showdiff.save.path");
        File file = new File(path);
        if (!file.isFile() || !file.exists()) {
            throw new FileNotFoundException("File error or do not exists!");
        }
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(file), Charset.defaultCharset());
        return new BufferedReader(reader);
    }

    /**
     * 从行数据中获取表格信息
     *
     * @param line 行数据
     * @return 表格信息
     */
    private ExcelInfo getExcelInfoFromLine(String line) {
        String[] infoArray = line.split(SEPARATOR);
        String name = infoArray[0];
        String url = infoArray[1];
        ExcelInfo info = new ExcelInfo();
        info.setName(name);
        info.setUrl(url);
        return info;
    }

}
