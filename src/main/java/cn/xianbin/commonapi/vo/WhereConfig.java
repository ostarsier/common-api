package cn.xianbin.commonapi.vo;

import lombok.Data;

@Data
public class WhereConfig {
    //字段名称
    private String filed;
    //字段类型
    private String type;
    //字段表达式
    private String express;
    //条件匹配规则
    private String match;
}
