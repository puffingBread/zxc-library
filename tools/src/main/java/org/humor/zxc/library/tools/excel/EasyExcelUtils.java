package org.humor.zxc.library.tools.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.RowData;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2023/1/13
 * Time: 14:29
 *
 * @author zeng.xu
 */
@Slf4j
public class EasyExcelUtils {


    private static final int SIZE = 60000;
    private static final int sheetNo = 1;
    private static final int headNo = 1;
    private static final int headLineNo = 0;

    /**
     * 从Excel中读取文件，读取的文件是一个DTO类，该类必须继承BaseRowModel
     * 具体实例参考 ： MemberMarketDto.java
     * 参考：https://github.com/alibaba/easyexcel
     * 字符流必须支持标记，FileInputStream 不支持标记，可以使用BufferedInputStream 代替
     * BufferedInputStream bis = new BufferedInputStream(new FileInputStream(...));
     *
     * @param inputStream 文件输入流
     * @param clazz       继承该类必须继承BaseRowModel的类
     * @return 读取完成的list
     */
    public static <T> List<T> readExcel(final InputStream inputStream, ExcelTypeEnum typeEnum, final Class<T> clazz) {
        if (null == inputStream) {
            throw new NullPointerException("excel文件为空!");
        }

        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
        List<T> resultList = new LinkedList<>();
        EasyExcel.read(inputStream, clazz, new PageReadListener<T>(dataList -> {
            for (T data : dataList) {
                log.info("读取到一条数据{}", JSON.toJSONString(data));
                resultList.add(data);
            }
        })).excelType(typeEnum).sheet().doRead();

        return resultList;
    }

    public static <T> List<T> readExcel2(final InputStream inputStream, ExcelTypeEnum excelType, final Class<T> clazz) {

        List<T> resultList = Lists.newArrayList();
        // 写法2：
        // 匿名内部类 不用额外写一个DemoDataListener
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(inputStream, clazz, new ReadListener<T>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;
            /**
             *临时存储
             */
            private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(T data, AnalysisContext context) {
                cachedDataList.add(data);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                saveData();
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}条数据，开始存储数据库！", cachedDataList.size());
                log.info("存储数据库成功！");
                if (CollectionUtils.isNotEmpty(cachedDataList)) {
                    resultList.addAll(cachedDataList);
                }
            }
        }).excelType(excelType).sheet().doRead();

        return resultList;
    }

    public static <T> List<T> readExcel3(final InputStream inputStream, ExcelTypeEnum excelType, final Class<T> clazz) {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法3：
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(inputStream, clazz, excelListener).sheet().doRead();

        return excelListener.getData();


    }

    public static <T> List<T> readExcel4(final InputStream inputStream, ExcelTypeEnum excelType, final Class<T> clazz) {
        // 写法4
        // 一个文件一个reader
        ExcelListener<T> listener = new ExcelListener<>();
        try (ExcelReader excelReader = EasyExcel.read(inputStream, clazz, listener).build()) {
            // 构建一个sheet 这里可以指定名字或者no
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // 读取一个sheet
            excelReader.read(readSheet);
        }

        return listener.getData();
    }

    public static <T> List<T> readExcel(final String pathName, ExcelTypeEnum typeEnum, final Class<T> clazz) {

        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
        List<T> resultList = new LinkedList<>();
        EasyExcel.read(pathName, clazz, new PageReadListener<T>(dataList -> {
            for (T data : dataList) {
                log.info("读取到一条数据{}", JSON.toJSONString(data));
                resultList.add(data);
            }
        })).excelType(typeEnum).sheet().doRead();

        return resultList;
    }

//    public static <T extends BaseRowModel> List<T> readExcelCheck(final InputStream inputStream, ExcelTypeEnum typeEnum, final Class<? extends BaseRowModel> clazz) {
//        if (null == inputStream) {
//            throw new NullPointerException("excel文件为空!");
//        }
//
//        //解析每行结果在listener中处理
//        AnalysisEventListener listener = new ExcelListener();
//        //读取xls 和 xlxs格式
//        //如果POI版本为3.17，可以如下声明
//        ExcelReader reader = new ExcelReader(inputStream, null, listener);
//        //判断格式，针对POI版本低于3.17
//
//        ExcelTypeEnum excelTypeEnum = valueOf(inputStream);
//        new ReadWorkbook();
//        ExcelReader reader = new ExcelReader(inputStream, typeEnum, null, listener);
//        reader.read(new ReadSheet(sheetNo, headLineNo, clazz));
//
//        return ((ExcelListener) listener).getData();
//    }
//
//
//
//    /**
//     * 需要写入的Excel，有模型映射关系
//     *
//     * @param list 写入Excel中的所有数据，继承于BaseRowModel
//     */
//    public static void writeExcel(final OutputStream stream, List<? extends BaseRowModel> list) {
//        try {
//            ExcelWriter writer = new ExcelWriter(stream, ExcelTypeEnum.XLSX,true);
//            //写第一个sheet,  有模型映射关系
//            Class t = list.get(0).getClass();
//            int no = 1;
//            for (int i = 0; i < list.size(); i += SIZE) {
//                int end = Math.min(i + SIZE, list.size());
//                List<? extends BaseRowModel> subList = list.subList(i, end);
//                Sheet sheet = new Sheet(no, headLineNo, t,"sheet"+no,null);
//                writer.write(subList, sheet);
//                no++;
//            }
//            writer.finish();
//        } catch (Exception e) {
//            log.error("Excel导出异常{}", e.getMessage(), e);
//        } finally {
//            try {
//                stream.close();
//            } catch (IOException e) {
//
//                log.error("Excel导出输出流关闭异常{}", e.getMessage(), e);
//            }
//        }
//    }


}
