package org.humor.zxc.library.tools;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import org.humor.zxc.library.tools.excel.EasyExcelUtils;
import org.humor.zxc.library.tools.excel.StudentDTO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Date: $DATE
 * Time: $TIME
 *
 * @author $USER
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello world!");

        String pathName = "C:\\Users\\zeng.xu\\Downloads\\export_result (3).csv";

        FileInputStream fileInputStream = new FileInputStream(pathName);
        List<StudentDTO> list =
                EasyExcelUtils.readExcel2(fileInputStream, ExcelTypeEnum.CSV, StudentDTO.class);

        System.out.println(JSON.toJSONString(list));
    }
}