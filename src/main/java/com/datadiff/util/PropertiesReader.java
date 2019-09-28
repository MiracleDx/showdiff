package com.datadiff.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取器
 *
 * @author zhy
 */
@Slf4j
public class PropertiesReader {

    /**
     * 配置文件属性
     */
    private static Properties props;

    static {
        readProperties();
    }

    /**
     * 读取配置文件属性
     */
    synchronized private static void readProperties() {
        props = new Properties();
        try (InputStream in = getInputStream()) {
            props.load(in);
        } catch (Exception e) {
            log.error("Error occurred when loading properties! ", e);
        }
    }

    /**
     * 获取输入流
     *
     * @return 输入流
     */
    private static InputStream getInputStream() {
        return PropertiesReader.class.getClassLoader().
                getResourceAsStream("application.properties");
    }

    /**
     * 根据属性名获取配置文件中的某个属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    public static String get(String key) {
        if (props == null) {
            readProperties();
        }
        return props.getProperty(key);
    }

    /**
     * 根据属性名获取配置文件中的某个属性值，
     * 并将其按照默认的分隔符"."分割成数组
     *
     * @param key 属性名
     * @return 属性数组
     */
    public static String[] getArray(String key) {
        return getArray(key, ",");
    }

    /**
     * 根据属性名获取配置文件中的某个属性值，
     * 并将其按照指定的分隔符分割成数组
     *
     * @param key       属性名
     * @param separator 分隔符
     * @return 属性数组
     */
    public static String[] getArray(String key, String separator) {
        if (props == null) {
            readProperties();
        }
        String property = props.getProperty(key);
        if (StringUtils.isBlank(property) ||
                !property.contains(separator) ||
                StringUtils.isBlank(separator)) {
            return new String[]{};
        }
        return property.split(separator);
    }

}
