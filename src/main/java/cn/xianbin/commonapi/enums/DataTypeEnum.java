package cn.xianbin.commonapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DataTypeEnum {
    MYSQL_STRING("mysql", "'", "字符", ""),
    MYSQL_DATE("mysql", "'", "日期", "%Y-%m-%d"),
    MYSQL_ARRAY("mysql", "'", "数组", ""),
    MYSQL_NUMBER("mysql", "", "数字", "");
    private String jdbcType;
    private String prefix;
    private String val;
    private String format;


    DataTypeEnum(String jdbcType, String prefix, String val, String format) {
        this.jdbcType = jdbcType;
        this.prefix = prefix;
        this.val = val;
        this.format = format;
    }

    @JsonCreator
    public static DataTypeEnum getByCode(String val) {
        for (DataTypeEnum param : DataTypeEnum.values()) {
            if (param.val.equals(val)) {
                return param;
            }
        }
        return null;
    }

    public static DataTypeEnum getInstance(String val) {
        for (DataTypeEnum e : values()) {
            if (val.equals(e.val)) {
                return e;
            }
        }
        return null;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public static List<String> keys() {
        return Arrays.stream(DataTypeEnum.values()).map(DataTypeEnum::getVal).collect(Collectors.toList());
    }
}
