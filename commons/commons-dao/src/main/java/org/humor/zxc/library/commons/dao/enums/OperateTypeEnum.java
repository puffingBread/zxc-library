package org.humor.zxc.library.commons.dao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.stream.Stream;

/***
 *  Date: 2019/8/28
 *  Time: 12:26
 *  @author xuzz
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  OperateTypeEnum implements IEnum<Integer> {

    /**
     * 硬删除
     */
    DELETE(-1, "删除"),
    /***
    *  软删除
    */
    REMOVE(0,"移除"),
    /**
     * 新增
     */
    INSERT(1,"新增"),
    /**
     * 修改
     */
    UPDATE(2,"修改"),
    ;

    private Integer value;

    private String desc;

    OperateTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OperateTypeEnum of(Integer value) {
        return Stream.of(values()).filter(n -> n.value.equals(value)).findFirst().orElseThrow(
                IllegalArgumentException::new);
    }

}
