package cn.darkjrong.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.darkjrong.core.lang.constants.Base64Constant;
import cn.darkjrong.core.lang.constants.FileConstant;
import cn.darkjrong.core.lang.constants.NetworkProtocol;
import cn.darkjrong.core.multipart.CustomMultipartFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;

/**
 * 文件操作工具类
 *
 * @author Rong.Jia
 * @date 2020/04/27 15:21
 */
@Slf4j
public class FileUtils {

    /**
     * URL 转 base64
     *
     * @param requestUrl URL
     * @return String 图片base64
     * @throws IOException 文件写出异常
     * @author Rong.Jia
     * @date 2019/01/11 11:20
     */
    public static String urlToBase64(String requestUrl) throws IOException {

        URL url = new URL(requestUrl);
        URLConnection connection = url.openConnection();
        InputStream stream = connection.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        IoUtil.copy(stream, out);
        byte[] data = out.toByteArray();
        IoUtil.close(stream);
        IoUtil.close(out);

        return Base64.encode(data);

    }


    /**
     * url 转File
     *
     * @param dirPath  存储路径
     * @param fileName 文件名
     * @return File
     * @throws IOException
     * @date 2018/09/29 14:00
     * @author Rong.Jia
     */
    public static File urlToFile(String requestUrl, String dirPath, String fileName) throws IOException {

        URL url = new URL(requestUrl);
        URLConnection connection = url.openConnection();
        InputStream stream = connection.getInputStream();

        if (!StrUtil.endWith(dirPath, StrUtil.SLASH)) {
            dirPath = dirPath + StrUtil.SLASH;
        }

        Path path = Paths.get(dirPath + fileName);

        Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);

        return path.toFile();

    }

    /**
     * base64 转 file
     *
     * @param base64  base64
     * @param dirPath 文件存储地址
     * @return File 文件
     * @throws IOException
     */
    public static File base64ToFile(String base64, String dirPath) throws IOException {

        base64 = base64.replaceAll(Base64Constant.BASE64_REG, StrUtil.EMPTY);

        String filename = IdUtil.randomUUID() + "-" + System.currentTimeMillis() + FileConstant.JPEG_SUFFIX;

        if (!StrUtil.endWith(dirPath, StrUtil.SLASH)) {
            dirPath = dirPath + StrUtil.SLASH;
        }

        return Files.write(Paths.get(dirPath + filename), Base64.decode(base64), StandardOpenOption.CREATE).toFile();
    }

    /**
     * 获取图片
     *
     * @param image 图片， URL, base64
     * @return 文件
     */
    public static File getImageFile(String image) {
        File file = null;
        try {
            // true 包含，false 不包含
            boolean flag = image.startsWith(NetworkProtocol.HTTP_PROTOCOL);
            if (flag) {
                String fileName = IdUtil.fastUUID() + DateUtil.current() + FileConstant.JPEG_SUFFIX;
                file = FileUtils.urlToFile(image, FileConstant.TMP_DIR, fileName);
            } else {
                if (StrUtil.containsIgnoreCase(image, Base64Constant.IMAGE_PREFIX)) {
                    image = image.replace(Base64Constant.IMAGE_PREFIX, StrUtil.EMPTY);
                }
                file = FileUtils.base64ToFile(image, FileConstant.TMP_DIR);
            }
        } catch (Exception e) {
            log.error("Image conversion failed {}", e.getMessage());
            return null;
        }

        return file;
    }

    /**
     *  删除文件
     * @param file 文件
     * @return 是否成功
     */
    public static Boolean del(File file) {
        try {
            return FileUtil.del(file);
        }catch (Exception e){
            return Boolean.FALSE;
        }
    }

    /**
     * file转 MultipartFile
     *
     * @param file 文件
     * @return {@link MultipartFile}
     * @throws IOException IO异常
     */
    public static MultipartFile file2MultipartFile(File file) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = FileUtil.getInputStream(file);
            return new CommonMultipartFile(file.getName(), inputStream);
        }catch (Exception e) {
            log.error("file2Multipart {}", e.getMessage());
            throw new IOException(e);
        }finally {
            IoUtil.close(inputStream);
        }
    }

    /**
     * MultipartFile 转换对象
     * 该类过时, 请使用 {@link CustomMultipartFile }
     * @author Rong.Jia
     * @date 2022/03/23
     */
    @Deprecated
    public static class CommonMultipartFile extends CustomMultipartFile {

        public CommonMultipartFile(String name, byte[] content) {
            super(name, content);
        }

        public CommonMultipartFile(String name, InputStream contentStream) throws IOException {
            super(name, contentStream);
        }

        public CommonMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            super(name, originalFilename, contentType, content);
        }

        public CommonMultipartFile(String name, String originalFilename, String contentType, InputStream contentStream) throws IOException {
            super(name, originalFilename, contentType, contentStream);
        }
    }




}
