package cn.xianbin.commonapi.enums;

import cn.xianbin.commonapi.exception.SourceException;
import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DataBaseTypeEnum {

    MYSQL("mysql", "mysql", "com.mysql.cj.jdbc.Driver", "`", "`", "'", "'");

    private String feature;
    private String desc;
    private String driver;
    private String keywordPrefix;
    private String keywordSuffix;
    private String aliasPrefix;
    private String aliasSuffix;

    DataBaseTypeEnum(String feature, String desc, String driver, String keywordPrefix, String keywordSuffix, String aliasPrefix, String aliasSuffix) {
        this.feature = feature;
        this.desc = desc;
        this.driver = driver;
        this.keywordPrefix = keywordPrefix;
        this.keywordSuffix = keywordSuffix;
        this.aliasPrefix = aliasPrefix;
        this.aliasSuffix = aliasSuffix;
    }

    public static DataBaseTypeEnum getInstance(String feature) throws SourceException {
        if (StringUtils.isEmpty(feature)) {
            return null;
        }
        for (DataBaseTypeEnum dataTypeEnum : values()) {
            if (feature.equals(dataTypeEnum.feature)) {
                return dataTypeEnum;
            }
        }
        return null;
    }

    @JsonCreator
    public static DataBaseTypeEnum getByCode(String feature) {
        for (DataBaseTypeEnum param : DataBaseTypeEnum.values()) {
            if (param.feature.equals(feature)) {
                return param;
            }
        }
        return null;
    }

    public String getFeature() {
        return feature;
    }

    public String getDesc() {
        return desc;
    }

    public String getDriver() {
        return driver;
    }

    public String getKeywordPrefix() {
        return keywordPrefix;
    }

    public String getKeywordSuffix() {
        return keywordSuffix;
    }

    public String getAliasPrefix() {
        return aliasPrefix;
    }

    public String getAliasSuffix() {
        return aliasSuffix;
    }

    public static List<String> keys() {
        return Arrays.stream(DataBaseTypeEnum.values()).map(DataBaseTypeEnum::getFeature).collect(Collectors.toList());
    }
}
