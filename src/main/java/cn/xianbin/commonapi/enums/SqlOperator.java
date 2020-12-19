package cn.xianbin.commonapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum SqlOperator {

    IN("in"),
    EQUAL("="),
    NOT_EQUAL("!="),
    GTE(">="),
    GT(">"),
    LTE("<="),
    LT("<"),
    LIKE("like");

    private String val;

    public static List<String> keys() {
        return Arrays.stream(SqlOperator.values()).map(SqlOperator::getVal).collect(Collectors.toList());
    }

}