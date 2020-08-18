package org.humor.zxc.library.commons.dao.enums;

import java.util.stream.Stream;

/***
 *  Date: 2019/12/5
 *  Time: 17:37
 *  @author xuzz
 */
public enum LogTypeEnum {
    SQL(1, "sql"),
    NO_SQL(2, "noSql"),
    ;

    private Integer value;

    private String desc;

    LogTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static LogTypeEnum of(Integer value) {
        return Stream.of(values()).filter(n -> n.value.equals(value)).findFirst().orElseThrow(
                IllegalArgumentException::new);
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
