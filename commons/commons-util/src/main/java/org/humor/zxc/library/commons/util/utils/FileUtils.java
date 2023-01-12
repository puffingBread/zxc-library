package org.humor.zxc.library.commons.util.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作
 *
 */
public class FileUtils {
    /**
     * 判断指定url的文件是否存在
     *
     * @param url 网络上的文件的url
     * @return boolean
     * @throws Exception
     */
    public static boolean isFileExist(String url) throws Exception {
        URL serverUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
        //文件存在‘HTTP/1.1 200 OK’ 文件不存在 ‘HTTP/1.1 404 Not Found’
        String message = urlConnection.getHeaderField(0);
        if (!StringUtils.isBlank(message) && message.startsWith("HTTP/1.1 404")) {
            System.out.println("serverUrl aacConvert this downUrl is not found! downUrl:" + url);
            return false;
        }
        return true;
    }

    /**
     * 创建目录
     *
     * @param dir
     * @return
     * @throws Exception
     */
    public static boolean createDir(String dir) {
        try {
            dir = StringUtil.replace(dir, "\\", "/");
            File file = new File(dir);
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                System.out.println("//不存在");
                file.mkdir();
            } else {
                System.out.println("//目录存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取文件内容
     *
     * @param fileName
     * @return
     */
    public static List<String> getContent(String fileName) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader isReader = null;
        List<String> list = null;
        try {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis, "GBK");
            isReader = new BufferedReader(isr);
            // isReader = new BufferedReader(new UnicodeReader(fis,
            // Charset.defaultCharset().name()));
            String str = null;
            if (isReader.ready()) {
                list = new ArrayList<>();
                do {
                    str = isReader.readLine();
                    if (str == null) {
                        break;
                    }
                    str = str.trim();
                    if (str.length() > 0) {
						list.add(str);
					}
                } while (true);
            }
        } catch (IOException e) {
            System.out.println("getContent error:" + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("getContent NullPointerException:" + e.getMessage());
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
                if (null != isr) {
                    isr.close();
                }
                if (null != isReader) {
                    isReader.close();
                }
            } catch (IOException e) {
            }
        }
        return list;
    }


    /**
     * 获取文件内容
     *
     * @param fileName
     * @param charset  字符编码(为null，则使用UTF-8)
     * @return
     */
    public static List<String> getContent(String fileName, String charset) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader isReader = null;
        List<String> list = null;
        try {
            fis = new FileInputStream(fileName);
            if (StringUtils.isBlank(charset)) {
                charset = "UTF-8";
            }
            isr = new InputStreamReader(fis, charset);
            isReader = new BufferedReader(isr);
            String str = null;
            if (isReader.ready()) {
                list = new ArrayList<String>();
                do {
                    str = isReader.readLine();
                    if (str == null) {
                        break;
                    }
                    str = str.trim();
                    if (str.length() > 0) {
						list.add(str);
					}
                } while (true);
            }
        } catch (IOException e) {
            System.out.println("getContent error:" + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("getContent NullPointerException:" + e.getMessage());
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
                if (null != isr) {
                    isr.close();
                }
                if (null != isReader) {
                    isReader.close();
                }
            } catch (IOException e) {
                System.out.println("getContent(),error:" + e);
            }
        }
        return list;
    }

    /**
     * B方法追加文件：使用FileWriter
     *
     * @param fileName 文件名(全路径)
     * @param content  追加的内容
     * @param split    分隔符,默认表示换行; "\n"表示追加行;
     */
    public static void appendContent(String fileName, String content, String split) {
        if (null == split) {
            split = "\n";
        }
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileOutputStream out = new FileOutputStream(fileName, true);
            OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
            writer.write(content + split);  // "\n"表示追加行;
            writer.close();
            out.close();
        } catch (IOException e) {
            System.out.println("appendContent(),error:" + e);
        }
    }

    /**
     * 使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void setContent(String fileName, String content) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
            writer.write(content + "\n");
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("setContent(),error:" + e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
        }
    }

    /**
     * 创建一个空的文件
     *
     * @param dir      文件目录
     * @param fileName 文件名称
     */
    public static boolean createFile(String dir, String fileName) {
        FileUtils.createDir(dir); //如果目录不存在,先创建目录
        String fullFilePath = dir + File.separator + fileName;
        fullFilePath = StringUtil.replace(fullFilePath, "\\", "/");
        File excelFile = new File(fullFilePath);
        if (excelFile.exists()) {
            excelFile.delete();
        }
        try {
            excelFile.createNewFile();
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("createFile()删除文件失败,fullFilePath---->" + fullFilePath);
            return false;
        }
    }

    /**
     * 删除指定的文件
     *
     * @param dir      文件目录
     * @param fileName 文件名称
     * @return 成功：true  失败：false;
     */
    public static boolean deleteFile(String dir, String fileName) {
        String fullFilePath = dir + File.separator + fileName;
        fullFilePath = StringUtil.replace(fullFilePath, "\\", "/");
        File file = new File(fullFilePath);
        try {
            file.delete();
            return true;
        } catch (Exception e1) {
            System.out.println("deleteFile()删除文件失败,fullFilePath---->" + fullFilePath);
            return false;
        }
    }

    /**
     * 重命名指定的文件
     *
     * @param dir1      文件目录1
     * @param fileName1 文件1名称
     * @param dir2      文件目录2
     * @param fileName2 文件2名称
     * @return 成功：true  失败：false;
     */
    public static boolean renameFile(String dir1, String fileName1, String dir2, String fileName2) {
        String fullFilePath1 = dir1 + File.separator + fileName1;
        fullFilePath1 = StringUtil.replace(fullFilePath1, "\\", "/");
        File file1 = new File(fullFilePath1);

        String fullFilePath2 = dir2 + File.separator + fileName2;
        fullFilePath2 = StringUtil.replace(fullFilePath2, "\\", "/");
        File file2 = new File(fullFilePath2);
        try {
            file1.renameTo(file2);
            return true;
        } catch (Exception ex) {
            System.out.println("renameFile()重命名文件失败,fullFilePath1---->" + fullFilePath1);
            System.out.println("renameFile()重命名文件失败,fullFilePath2---->" + fullFilePath2);
            return false;
        }
    }

    /**
     * 读取txt文件内容
     *
     * @param filePath txt文件路径
     * @return byte[] 文件内容字节数组
     * @throws IOException
     */
    public static Byte[] readTxtFile(String filePath) throws IOException {
        InputStream input = new FileInputStream(filePath);
        byte[] b = new byte[1024];
        ArrayList<Byte> lsbytes = new ArrayList<Byte>();

        int n = 0;
        while ((n = input.read(b)) != -1) {
            for (int i = 0; i < n; i++) {
                lsbytes.add(b[i]);
            }
        }
        return (Byte[]) (lsbytes.toArray());
    }

    /**
     * 从url取得文件名
     *
     * @param url 文件url 比如  http://m.mdl.com/2hDjulAT_xEAAAAAADGKJiWpBJk680.apk
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        String name = new Long(System.currentTimeMillis()).toString() + ".xml";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }

    /**
     * 下载远程文件
     *
     * @param url 远程文件url,比如  http://fq1.img.meidaila.net/150729/Fj1b4jn62V9B_DbUK-iNNuvSuiWW.jpg
     * @param dir 本地存放目录 ,比如 F:\\http_download\\
     * @return 返回本地完整的路径名 ,比如 http://fq1.img.meidaila.net/150729/Fj1b4jn62V9B_DbUK-iNNuvSuiWW.jpg
     */
    public static String downloadFromUrl(String url, String dir) {
        System.out.println("downloadFromUrl(),url:" + url);
        dir = StringUtil.replace(dir, "\\", "/");
        if (!dir.endsWith("/")) {
            dir = dir + "/";
        }
        System.out.println("downloadFromUrl(),url:" + url);
        String fileName = getFileNameFromUrl(url);
        System.out.println("downloadFromUrl(),fileName:" + fileName);
        try {
            URL httpurl = new URL(url);
            System.out.println("fileName--->" + fileName);
            File f = new File(dir + fileName);
            if (null != f && f.exists()) {
                //System.out.println("fileName--->" + fileName + ",已存在,删除掉...");
                System.out.println("dir:" + dir + " ,fileName--->" + fileName + ",已存在,删除掉...");
                f.delete();
            }
            f.createNewFile();
            System.out.println("fileName--->" + fileName + ",创建成功!");
//            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            System.out.println("downloadFromUrl() error --->" + e);
            //e.printStackTrace();
            return null;
        }
        if (dir.endsWith(File.separator)) {
            return dir + fileName;
        } else {
            return dir + File.separator + fileName;
        }
    }
    //////////////////////////////////////////////////

    /**
     * 拷贝文件
     *
     * @param oldname
     * @param newname
     * @param overwrite
     * @return
     */
    public static boolean copy(String oldname, String newname, boolean overwrite)
            throws IOException {

        if (!overwrite && new File(newname).exists())
            return false;

        FileInputStream input = new FileInputStream(oldname);
        String newDire = newname.substring(0, newname.lastIndexOf("/"));
        mkdirs(newDire);
        FileOutputStream output = new FileOutputStream(newname);
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = input.read(b)) != -1) {
            output.write(b, 0, len);
        }
        output.flush();
        output.close();
        input.close();
        return true;
    }

    public static String copyFileAndMakeDir(InputStream source, String destDir,
                                            String newFileName) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("can not make dest dir (" + destDir + ")");
            }
        }
        return copyFile(source, destDir, newFileName);
    }

    public static String copyFile(InputStream source, String destDir,
                                  String newFileName) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) {
            throw new IOException("dest dir (" + destDir + ") does not exist");
        }
        if (!dir.isDirectory()) {
            throw new IOException("dest dir (" + destDir + ") is not a folder");
        }
        String destFileFullName = null;
        BufferedOutputStream out = null;
        try {
            destFileFullName = destDir + File.separator + newFileName;
            out = new BufferedOutputStream(new FileOutputStream(
                    destFileFullName));
            byte[] buffer = new byte[8192];
            int bytesRead = 0;
            while ((bytesRead = source.read(buffer, 0, 8192)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

        } finally {
            if (out != null) {
                out.close();
            }
        }
        return destFileFullName;
    }

    /**
     * 删除一个指定的文件
     */
    public static boolean deleteFile(String FileName) {
        boolean bRet = false;
        if (FileName != null && FileName.length() > 0) {
            File filename = new File(FileName);
            if (filename.delete()) {
                bRet = true;
            }
        }
        return bRet;
    }

    /**
     * 删除一个指定的目录并且包括该目录中的子目录和文件
     */
    public static boolean deleteTree(String PathName) {
        File path = new File(PathName);
        String list[] = path.list();
        boolean isok = true;
        if (list != null) {
            int length = list.length;
            for (int x = 0; x < length; x++) {
                String temp = PathName + "/" + list[x];
                File f = new File(temp);
                if (f.isFile()) {
                    isok = f.delete();
                    continue;
                }
                if (f.isDirectory()) {
                    if (isEmpty(f)) {
                        isok = f.delete();
                    } else {
                        isok = deleteTree(temp);
                    }
                }
                if (!isok) {
                    return false;
                }
            }
            path.delete();
        }
        return isok;
    }

    /**
     * 获得一个文件或一个目录中的全部文件的尺寸
     */
    public static long getFilesSize(String FileName) {
        long lRet = 0L;
        File file = new File(FileName);
        if (file.isDirectory()) {
            String list[] = file.list();
            if (list != null) {
                for (int x = 0; x < list.length; x++) {
                    String temp = FileName + "/" + list[x];
                    File f = new File(temp);
                    if (f.isFile()) {
                        lRet += f.length();
                    } else if (f.isDirectory()) {
                        lRet += getFilesSize(temp);
                    }
                }

            }
        } else {
            lRet = file.length();
        }
        return lRet;
    }

    /**
     * 文件或者目录重命名
     */
    public static boolean renameFile(String SourceFileName,
                                     String TargetFileName) {
        boolean bRet = false;
        File fileS = new File(SourceFileName);
        File fileT = new File(TargetFileName);
        if (fileS.exists() && fileS.renameTo(fileT)) {
            bRet = true;
        }
        return bRet;
    }

    /**
     * 创建一个目录
     */
    public static boolean mkDir(String DirectoryName) {
        boolean bRet = false;
        File file = new File(DirectoryName);
        if (!file.exists() && file.mkdirs()) {
            bRet = true;
        }
        return bRet;
    }

    /**
     * 返回指定名称是否为空
     */
    private static boolean isEmpty(File tempdir) {
        if (tempdir.exists() && tempdir.isDirectory()) {
            String list[] = tempdir.list();
            if (list.length == 0) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * GBK编码写入文件
     *
     * @param filename
     * @param str
     * @throws IOException
     */
    public static void writIn(String filename, String str) throws IOException {
        writIn(filename, str, "GBK");
    }

    /**
     * 往一个指定文件里全新写入指定编码字符串
     *
     * @param filename
     * @param str
     * @param strCode  编码,如GBK
     * @throws IOException
     */
    public static void writIn(String filename, String str, String strCode)
            throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            mkDir(filename.substring(0, filename.lastIndexOf("/")));
            fw = new FileWriter(filename); // 建立FileWrite变量,并设定由fw变量变数引用
            bw = new BufferedWriter(fw);
            // 建立BufferedWriter变量,并设定由bw变量变数引用
            // 将字串写入文件
            bw.write(new String(str.getBytes(strCode)));
            bw.flush(); // 将资料更新至文件
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fw != null) {
				fw.close(); // 关闭档案
			}
            if (bw != null) {
				bw.close();
			}
        }
    }

    /**
     * 将信息记录追加到一个文件 常用于记日志
     *
     * @param msg
     * @param file
     * @throws IOException
     */
    public static void log(String msg, String file) throws IOException {
        FileWriter logFile = new FileWriter(file, true);
        PrintWriter logPrintWriter = new PrintWriter(logFile);
        logPrintWriter.println(msg);
        logPrintWriter.flush();
        try {
			logPrintWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String read(String filename) throws IOException {
        File file = new File(filename);
        InputStreamReader read = new InputStreamReader(
                new FileInputStream(file), "GBK");
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        StringBuffer readfile = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            // readfile.append(line + "\r\n");
            readfile.append(line);
        }
        try {
            if (read != null)
                read.close();
            if (reader != null)
                reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readfile.toString().trim();
    }

    /**
     * 读取文件
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String readNoNewLine(String filename) throws IOException {
        InputStreamReader read = null;
        BufferedReader reader = null;
        String line = "";
        StringBuffer readfile = new StringBuffer();
        try {
            File file = new File(filename);
            read = new InputStreamReader(new FileInputStream(file), "GBK");
            reader = new BufferedReader(read);
            while ((line = reader.readLine()) != null) {
                readfile.append(line + "\r");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (read != null) {
					read.close();
				}
                if (reader != null) {
					reader.close();
				}
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return readfile.toString().trim();
    }

    /**
     * 读取相对classpath路径文件
     *
     * @return
     * @throws IOException
     * @author chenyz
     */
    public static String readFile(String filename) throws IOException {
        InputStream in = null;
        ClassPathResource res = new ClassPathResource(filename);
        in = res.getInputStream();
        BufferedReader bin = new BufferedReader(new InputStreamReader(in));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = bin.readLine()) != null) {
            buffer.append(line);
        }
        try {
            if (bin != null)
                bin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (buffer.toString());
    }

    /**
     * 从文件路径得到文件名。
     *
     * @param filePath 文件的路径,可以是相对路径也可以是绝对路径
     * @return 对应的文件名
     * @since 0.4
     */
    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    /**
     * 从文件名得到文件绝对路径。
     *
     * @param fileName 文件名
     * @return 对应的文件路径
     * @since 0.4
     */
    public static String getFilePath(String fileName) {
        File file = new File(fileName);
        return file.getAbsolutePath();
    }

    /**
     * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
     *
     * @param fileName 文件名
     * @return 文件名中的类型部分
     * @since 0.5
     */
    public static String getTypePart(String fileName) {
        int point = fileName.lastIndexOf('.');
        int length = fileName.length();
        if (point == -1 || point == length - 1) {
            return "";
        } else {
            return fileName.substring(point + 1, length);
        }
    }

    /**
     * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
     *
     * @param file 文件
     * @return 文件名中的类型部分
     * @since 0.5
     */
    public static String getFileType(File file) {
        return getTypePart(file.getName());
    }

    /**
     * 列出目录下的文件或子目录
     */
    public static String[] dirfile(String dirname) throws IOException {
        File dir = new File(dirname);
        return dir.list();
    }

    /**
     * 读文件。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean canRead(String remoteAbsPath) throws Exception {
        return getFile(remoteAbsPath).canRead();
    }

    /**
     * 写文件。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean canWrite(String remoteAbsPath) throws Exception {
        return getFile(remoteAbsPath).canWrite();
    }

    /**
     * 删除文件。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean delete(String remoteAbsPath) throws Exception {
        return getFile(remoteAbsPath).delete();
    }

    /**
     * @param remoteAbsPath
     * @throws Exception
     */
    public static void deleteOnExit(String remoteAbsPath) throws Exception {
        getFile(remoteAbsPath).deleteOnExit();
    }

    /**
     * 判断文件是否存在。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean exists(String remoteAbsPath) throws Exception {
        return getFile(remoteAbsPath).exists();
    }

    /**
     * 此处插入方法描述。
     *
     * @param remoteAbsPath java.lang.String
     * @return java.lang.String
     * @throws Exception 异常说明。
     */
    public static String getCanonicalPath(String remoteAbsPath)
            throws Exception, IOException {
        return getFile(remoteAbsPath).getCanonicalPath();
    }

    /**
     * 获得某个文件。
     *
     * @param remoteAbsPath java.lang.String
     * @return java.io.File
     */
    private static File getFile(String remoteAbsPath) {
        return new File(remoteAbsPath);
    }

    /**
     * 获取服务器工作目录。
     *
     * @return java.lang.String
     * @throws IOException 异常说明。
     * @throws Exception   异常说明。
     */
    public static String getServerWorkDir() throws IOException,
            Exception {
        return getFile(".").getCanonicalPath();
    }

    /**
     * 判断是否为目录。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean isDirectory(String remoteAbsPath) throws Exception {
        return getFile(remoteAbsPath).isDirectory();
    }

    /**
     * 判断是否为文件。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean isFile(String remoteAbsPath) throws Exception {
        return getFile(remoteAbsPath).isFile();
    }

    /**
     * 判断是否为隐藏文件。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean isHidden(String remoteAbsPath) throws Exception {
        return getFile(remoteAbsPath).isHidden();
    }

    /**
     * 获得最后修改时间。
     *
     * @param remoteAbsPath java.lang.String
     * @return long
     * @throws Exception 异常说明。
     */
    public static long lastModified(String remoteAbsPath) throws Exception {
        return getFile(remoteAbsPath).lastModified();
    }

    /**
     * 获得文件长度。
     *
     * @param remoteAbsPath java.lang.String
     * @return long
     * @throws Exception 异常说明。
     */
    public static long length(String remoteAbsPath) throws IOException {
        return getFile(remoteAbsPath).length();
    }

    /**
     * 获取目录列表。
     *
     * @param remoteAbsPath java.lang.String
     * @return java.lang.String[]
     * @throws Exception 异常说明。
     */
    public static String[] list(String remoteAbsPath) throws IOException {
        return getFile(remoteAbsPath).list();
    }

    /**
     * 创建目录。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean mkdir(String remoteAbsPath) throws IOException {
        return getFile(remoteAbsPath).mkdir();
    }

    /**
     * 创建目录。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean mkdirs(String remoteAbsPath) throws IOException {
        return getFile(remoteAbsPath).mkdirs();
    }

    /**
     * 修改服务器文件名称。
     *
     * @param origRemoteAbsPath    java.lang.String
     * @param renamedRemoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean renameTo(String origRemoteAbsPath,
                                   String renamedRemoteAbsPath) throws Exception {
        return getFile(origRemoteAbsPath).renameTo(
                getFile(renamedRemoteAbsPath));
    }

    /**
     * 设置最后修改时间。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean setLastModified(String remoteAbsPath, long time)
            throws Exception {
        return getFile(remoteAbsPath).setLastModified(time);
    }

    /**
     * 设置文件只读。
     *
     * @param remoteAbsPath java.lang.String
     * @return boolean
     * @throws Exception 异常说明。
     */
    public static boolean setReadOnly(String remoteAbsPath) throws IOException {
        return getFile(remoteAbsPath).setReadOnly();
    }

    /**
     * 删除目录
     *
     * @param dir
     */
    public static boolean deleteDir(String dir) throws Exception {
        if (dir == null || "".equals(dir)) {
			return false; // 检查参数
		}
        boolean bTotalRlt = true;
        File file = new File(dir);
        File[] fileList = file.listFiles();
        String dirPath = null;
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isFile()) {
                    bTotalRlt = bTotalRlt && fileList[i].delete();
                }
                if (fileList[i].isDirectory()) {
                    dirPath = fileList[i].getPath();
                    bTotalRlt = bTotalRlt && deleteDir(dirPath);
                }
            }
            bTotalRlt = bTotalRlt && file.delete();
        }
        return bTotalRlt;
    }

    /**
     * 递归复制一个目录的文件和子目录到另一个目录
     *
     * @param sourceDir
     * @param desDir
     */
    public static void copyFiles(String sourceDir, String desDir)
            throws Exception {
        if (sourceDir == null || "".equals(sourceDir)) {
			return;
		}
        File file = new File(sourceDir);
        if (file.isDirectory()) {
            File f = new File(desDir);
            if (!f.exists()) {
				f.mkdir();
			}
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
				return;
			}
            for (File file2 : files) {
                copyFiles(file2.toString(), desDir + "/" + file2.getName());
            }
        } else {
            copy(sourceDir, desDir, true);
        }
    }

    /**
     * 压缩多个文件成一个zip文件
     *
     * @param srcfile
     * @param zipfile
     */
    public static void zipFiles(List<File> srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            // ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; srcfile != null && i < srcfile.size(); i++) {
                FileInputStream in = new FileInputStream(srcfile.get(i));
                out.putNextEntry(new ZipEntry(srcfile.get(i).getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            // System.out.println("压缩完成.");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    public static void main(String[] args) throws InterruptedException {
        String url = "http://fq1.img.meidaila.net/150729/Fj1b4jn62V9B_DbUK-iNNuvSuiWW.jpg";
        String dir = "D:/";
        FileUtils.downloadFromUrl(url, dir);

        Thread.sleep(3000);
        String localFilePath = "D:/Fj1b4jn62V9B_DbUK-iNNuvSuiWW.jpg";
        String destFilePath = "D:/test.jpg";
        File downFile = new File(localFilePath);
        File destFile = new File(destFilePath);

        FileUtils.deleteFile("D:", "test.jpg");
        Thread.sleep(3000);
        downFile.renameTo(destFile);
        Thread.sleep(3000);

        /////往文本文件里追加行数据
        String txtDir = "d:/ccc222";
        String txtFile = "words-mdl-major.dic";
        FileUtils.createDir(txtDir);
        FileUtils.createFile(txtDir, txtFile);
        String fileName = txtDir + File.separator + txtFile;
        FileUtils.appendContent(fileName, "美白", null);
        FileUtils.appendContent(fileName, "美白2", "###");
        FileUtils.appendContent(fileName, "玻尿酸", "@@");
        FileUtils.appendContent(fileName, "麒云", "\n");
        FileUtils.appendContent(fileName, "玻尿酸丰唇", "\n");
        FileUtils.appendContent(fileName, "2288", ",");
        System.out.println("写txt文件完成~");
    }
}
