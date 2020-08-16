//package com.bread.enlighten.zxc.common.util.utils;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
//import org.apache.poi.hssf.usermodel.HSSFComment;
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFPatriarch;
//import org.apache.poi.hssf.usermodel.HSSFRichTextString;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.Workbook;
//
//
//public class ExcelUtil {
//	// 写入 excel
//	public static void writeExcel(String sheetName, HttpServletResponse response,
//			List<List<Object>> listData) {
//
//		if(listData == null || listData.size() == 0){
//			return;
//		}
//		HSSFWorkbook excel = new HSSFWorkbook();
//		HSSFSheet sheet = excel.createSheet(sheetName);
//		int index = 0;
//
//		for(int i = 0; i < listData.size(); ++i){
//			List<Object> rowData = listData.get(i);
//			if(rowData == null || rowData.size() == 0){
//				continue;
//			}
//
//			HSSFRow row = sheet.createRow(index ++);
//			for(int k = 0; k < rowData.size(); ++k){
//				if(rowData.get(k) == null){
//					continue;
//				}
//				HSSFCell cell = row.createCell(k);
//				cell.setCellValue(rowData.get(k).toString());
//			}
//		}
//
//		try {
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			excel.write(out);
//			byte[] bytes = out.toByteArray();
//			String fileName = sheetName + DateTime.getCurrDate("yyyyMMddHHmmss") + ".xls";
//			response.reset();
//			response.setContentType("application/x-download");
//			response.setHeader("Content-disposition", "attachment;filename="+
//					new String(fileName.getBytes("gb2312"), "ISO8859-1"));
//            response.getOutputStream().write(bytes);
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//
//	/**
//	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
//	 *
//	 * @param title 表格标题名
//	 * @param headers 表格属性列名数组
//	 * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
//	 * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
//	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
//	 */
//	@SuppressWarnings("deprecation")
//	public static void exportExcel(String title, String showCol, String soreCol,String soreColEn , List<Map<String, Object>> dataset, ServletOutputStream  out, String pattern) {
//		String[] soreCols = soreCol.split(",");
//		String[] soreColEns = soreColEn.split(",");
//
//		List<Integer> showIndex = new ArrayList<Integer>();
//		for (int i = 0; i < soreCols.length; i++) {
//			if (showCol.indexOf(soreCols[i]) > -1) {
//				showIndex.add(i);
//			}
//		}
///********************************************************************分割线*******声明表格对象，使用参数title****************************************************************************/
//		// 声明一个工作薄
//		HSSFWorkbook workbook = new HSSFWorkbook();
//		// 生成一个表格
//		HSSFSheet sheet = workbook.createSheet(title);
///********************************************************************分割线********设定样式*************************************************************************/
//		// 设置表格默认列宽度为15个字节
//		sheet.setDefaultColumnWidth((short) 15);
//
//		// 生成一个样式
//		HSSFCellStyle style = workbook.createCellStyle();
//		// 设置这些样式
//		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		font.setColor(HSSFColor.VIOLET.index);
//		font.setFontHeightInPoints((short) 12);
//		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//		// 把字体应用到当前的样式
//		style.setFont(font);
//
//		// 生成并设置另一个样式
//		HSSFCellStyle style2 = workbook.createCellStyle();
//		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//		style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style2.setBorderBottom(CellStyle.BORDER_THIN);
//		style2.setBorderLeft(CellStyle.BORDER_THIN);
//		style2.setBorderRight(CellStyle.BORDER_THIN);
//		style2.setBorderTop(CellStyle.BORDER_THIN);
//		style2.setAlignment(CellStyle.ALIGN_CENTER);
//		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font2 = workbook.createFont();
//		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
//		// 把字体应用到当前的样式
//		style2.setFont(font2);
//
//		// 生成另另一个字体
//		HSSFFont font3 = workbook.createFont();
//		font3.setColor(HSSFColor.BLUE.index);
//
///********************************************************************分割线***配注释功能********************************************************************************/
//
//		// 声明一个画图的顶级管理器
//		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//		// 定义注释的大小和位置,详见文档
//		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
//				0, 0, 0, (short) 4, 2, (short) 6, 5));
//		// 设置注释内容
//		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
//		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//		comment.setAuthor("leno");
//
///********************************************************************分割线****设置表头（标题行），使用参数：soreColEns*******************************************************************************/
//
//		// 产生表格标题行
//		HSSFRow row = sheet.createRow(0);//获取第一行
//		for (short i = 0; i < showIndex.size(); i++) {
//			HSSFCell cell = row.createCell(i);//定位第一行第几个单元格 成为对象
//			cell.setCellStyle(style);//只是样式(第一种样式)
//			HSSFRichTextString text = new HSSFRichTextString(soreColEns[showIndex.get(i)]);//格式化字符串成为poi中的text封装的对象
//			cell.setCellValue(text);//插入字体
//		}
///********************************************************************分割线****设置正文数据，使用参数：dataset*******************************************************************************/
//
//		// 遍历集合数据，产生数据行
//		int index = 0;
//		for (Map<String, Object> vo : dataset) {
//			index++;//从第二行开始
//			row = sheet.createRow(index);//获取行对象
//			for (int colIndex = 0; colIndex < showIndex.size(); colIndex++) {//迭代列
//				String fieldName = soreCols[showIndex.get(colIndex)];
//				Object fieldValue = vo.get(fieldName);
//				if(fieldValue == null){//要求输出列（clos）中匹配不到当前迭代的字段则跳出当前
//					fieldValue = "--";
//				}
//
//				HSSFCell cell = row.createCell(colIndex);//指定到单元格对象
//				cell.setCellStyle(style2);//给单元格设置样式
//
//				// 判断值的类型后进行强制类型转换
//				String textValue = null;
//				if (fieldValue instanceof Date) {
//					Date date = (Date) fieldValue;
//					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//					textValue = sdf.format(date);
//				} else if (fieldValue instanceof byte[]) {
//					// 有图片时，设置行高为60px;
//					row.setHeightInPoints(60);
//					// 设置图片所在列宽度为80px,注意这里单位的一个换算
//					sheet.setColumnWidth(colIndex, (short) (35.7 * 80));
//					// sheet.autoSizeColumn(colIndex);
//					byte[] bsValue = (byte[]) fieldValue;
//					HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
//							1023, 255, (short) 6, index, (short) 6, index);
//					anchor.setAnchorType(2);
//					patriarch.createPicture(anchor, workbook.addPicture(
//							bsValue, Workbook.PICTURE_TYPE_JPEG));
//				// 其它数据类型都当作字符串简单处理
//				} else {
//					textValue = fieldValue.toString();
//				}
//
//				// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
//				if (textValue != null) {
//					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
//					Matcher matcher = p.matcher(textValue);
//					if (matcher.matches()) {
//						// 是数字当作double处理
//						cell.setCellValue(Double.parseDouble(textValue));
//					} else {
//						HSSFRichTextString richString = new HSSFRichTextString(
//								textValue);
//						richString.applyFont(font3);
//						cell.setCellValue(richString);
//					}
//				}
//			}
//		}
//		try {
//			workbook.write(out);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	@SuppressWarnings("deprecation")
//	public static HSSFWorkbook exportExcelAppend(String title, String showCol, String soreCol, String soreColEn, List<Map<String, Object>> dataset, HSSFWorkbook workbook,int index ) {
//		 String pattern = "yyyy-MM-dd hh:mm:ss";
//		String[] soreCols = soreCol.split(",");
//		String[] soreColEns = soreColEn.split(",");
//		Set<String> showColSet = new HashSet<String>();
//		for(String s : showCol.split(",")) {
//			showColSet.add(s);
//		}
//
//		List<Integer> showIndex = new ArrayList<Integer>();
//		for (int i = 0; i < soreCols.length; i++) {
//			if (showColSet.contains(soreCols[i])) {
//				showIndex.add(i);
//			}
//		}
///********************************************************************分割线*******声明表格对象，使用参数title****************************************************************************/
//		// 声明一个工作薄
//		if (workbook == null) {
//			workbook = new HSSFWorkbook();
//		}
//		HSSFSheet sheet = null;
//		if (workbook.getSheet(title) == null) {
//			sheet = workbook.createSheet(title);
//		}else {
//			sheet = workbook.getSheet(title);
//		}
///********************************************************************分割线********设定样式*************************************************************************/
//		// 设置表格默认列宽度为15个字节
//		sheet.setDefaultColumnWidth((short) 15);
//
//		// 生成一个样式
//		HSSFCellStyle style = workbook.createCellStyle();
//		// 设置这些样式
//		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		font.setColor(HSSFColor.VIOLET.index);
//		font.setFontHeightInPoints((short) 12);
//		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//		// 把字体应用到当前的样式
//		style.setFont(font);
//
//		// 生成并设置另一个样式
//		HSSFCellStyle style2 = workbook.createCellStyle();
//		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//		style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style2.setBorderBottom(CellStyle.BORDER_THIN);
//		style2.setBorderLeft(CellStyle.BORDER_THIN);
//		style2.setBorderRight(CellStyle.BORDER_THIN);
//		style2.setBorderTop(CellStyle.BORDER_THIN);
//		style2.setAlignment(CellStyle.ALIGN_CENTER);
//		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font2 = workbook.createFont();
//		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
//		// 把字体应用到当前的样式
//		style2.setFont(font2);
//
//		// 生成另另一个字体
//		HSSFFont font3 = workbook.createFont();
//		font3.setColor(HSSFColor.BLUE.index);
//
///********************************************************************分割线***配注释功能********************************************************************************/
//
//		// 声明一个画图的顶级管理器
//		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//		// 定义注释的大小和位置,详见文档
//		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
//				0, 0, 0, (short) 4, 2, (short) 6, 5));
//		// 设置注释内容
//		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
//		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//		comment.setAuthor("leno");
//
///********************************************************************分割线****设置表头（标题行），使用参数：soreColEns*******************************************************************************/
//		HSSFRow row = null;
//		// 产生表格标题行
//		if (index == 0) {
//			row = sheet.createRow(0);//获取第一行
//			for (short i = 0; i < showIndex.size(); i++) {
//				HSSFCell cell = row.createCell(i);//定位第一行第几个单元格 成为对象
//				cell.setCellStyle(style);//只是样式(第一种样式)
//				HSSFRichTextString text = new HSSFRichTextString(soreColEns[showIndex.get(i)]);//格式化字符串成为poi中的text封装的对象
//				cell.setCellValue(text);//插入字体
//			}
//		}
///********************************************************************分割线****设置正文数据，使用参数：dataset*******************************************************************************/
//
//		// 遍历集合数据，产生数据行
//		for (Map<String, Object> vo : dataset) {
//			index++;//从第二行开始
//			row = sheet.createRow(index);//获取行对象
//			for (int colIndex = 0; colIndex < showIndex.size(); colIndex++) {//迭代列
//				String fieldName = soreCols[showIndex.get(colIndex)];
//				Object fieldValue = vo.get(fieldName);
//				if(fieldValue == null){//要求输出列（clos）中匹配不到当前迭代的字段则跳出当前
//					fieldValue = "--";
//				}
//
//				HSSFCell cell = row.createCell(colIndex);//指定到单元格对象
//				cell.setCellStyle(style2);//给单元格设置样式
//
//				// 判断值的类型后进行强制类型转换
//				String textValue = null;
//				if (fieldValue instanceof Date) {
//					Date date = (Date) fieldValue;
//					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//					textValue = sdf.format(date);
//				} else if (fieldValue instanceof byte[]) {
//					// 有图片时，设置行高为60px;
//					row.setHeightInPoints(60);
//					// 设置图片所在列宽度为80px,注意这里单位的一个换算
//					sheet.setColumnWidth(colIndex, (short) (35.7 * 80));
//					// sheet.autoSizeColumn(colIndex);
//					byte[] bsValue = (byte[]) fieldValue;
//					HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
//							1023, 255, (short) 6, index, (short) 6, index);
//					anchor.setAnchorType(2);
//					patriarch.createPicture(anchor, workbook.addPicture(
//							bsValue, Workbook.PICTURE_TYPE_JPEG));
//				// 其它数据类型都当作字符串简单处理
//				} else {
//					textValue = fieldValue.toString();
//				}
//
//				// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
//				if (textValue != null) {
//					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
//					Matcher matcher = p.matcher(textValue);
//					if (matcher.matches()) {
//						// 是数字当作double处理
//						cell.setCellValue(Double.parseDouble(textValue));
//					} else {
//						HSSFRichTextString richString = new HSSFRichTextString(
//								textValue);
//						richString.applyFont(font3);
//						cell.setCellValue(richString);
//					}
//				}
//			}
//		}
//		return workbook;
//	}
//}
//
//
