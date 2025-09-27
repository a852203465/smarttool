package cn.darkjrong.core.multipart;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 负责将InputStream转换MultipartFile,可以少引一个jar包,本来用的是spring-test-4.3.9中的MockMultipartFile,直接提取出来使用
 *
 * @author Rong.Jia
 * @date 2022/05/16
 */
public class CustomMultipartFile implements MultipartFile {

    private final String name;
    private String originalFilename;
    private String contentType;
    private final byte[] content;

    public CustomMultipartFile(String name, byte[] content) {
        this(name, name, null, content);
    }

    public CustomMultipartFile(String name, InputStream contentStream) throws IOException {
        this(name, name, null, IoUtil.readBytes(contentStream));
    }

    public CustomMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
        this.name = name;
        this.originalFilename = (originalFilename != null ? originalFilename : name);
        this.contentType = contentType;
        this.content = (content != null ? content : new byte[0]);
    }

    public CustomMultipartFile(String name, String originalFilename, String contentType, InputStream contentStream) throws IOException {
        this(name, originalFilename, contentType, IoUtil.readBytes(contentStream));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return (this.content.length == 0);
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileUtil.writeBytes(this.content, dest);
    }

}
