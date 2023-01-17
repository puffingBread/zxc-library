package org.humor.zxc.library.tools.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Date: 2023/1/13
 * Time: 15:51
 *
 * @author zeng.xu
 */
@Data
public class StudentDTO {

    @ExcelProperty(value = "学生id", index = 0)
    private Integer stuId;

    @ExcelProperty(value = "更新时间", index = 1)
    private Date updateTime;
}
