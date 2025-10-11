package cn.darkjrong.spring.boot.autoconfigure;

import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import org.apache.commons.net.ftp.FTP;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * FTP Client 配置类
 *
 * @author Rong.Jia
 * @date 2022/01/06
 */
@Data
@ConfigurationProperties(prefix = "stl.ftp.client")
public class FtpClientProperties {

    /**
     * 是否开启 FTP Client
     */
    private boolean enabled = Boolean.FALSE;

    /**
     *  FTP Server IP
     */
    private String host;

    /**
     * FTP Server端口,默认:21
     */
    private Integer port = 21;

    /**
     * 是否被动模式
     */
    private boolean passiveMode = Boolean.FALSE;

    /**
     * 工作目录
     */
    private String baseDir;

    /**
     *  用户名
     */
    private String username;

    /**
     *  密码
     */
    private String password;

    /**
     * 连接超时时间,默认:6秒
     */
    private Duration connectTimeout = Duration.ofSeconds(6);

    /**
     * ftp字符集
     */
    private String encoding = CharsetUtil.UTF_8;

    /**
     * 传输文件类型
     */
    private Integer transferFileType = FTP.BINARY_FILE_TYPE;

    /**
     * 缓冲大小
     */
    private Integer bufferSize = 2048;

    /**
     * 传输超时时间(秒)
     */
    private Duration dataTimeout;

    /**
     * 设置keepAlive,0:禁用
     * Zero (or less) disables
     */
    private Duration keepAliveTimeout = Duration.ofSeconds(0);

    /**
     * 连接池
     */
    public FtpClientPool pool = new FtpClientPool();

    /**
     * ftp客户端池
     *
     * @author Rong.Jia
     * @date 2022/01/06
     */
    @Data
    public static class FtpClientPool {

        /**
         * 最大连接数,默认为8
         */
        private int maxActive = 10;

        /**
         * 最大空闲的连接数,默认为8
         */
        private int maxIdle = 5;

        /**
         * 最少空闲的连接数,默认为2
         */
        private int minIdle = 2;

        /**
         * 连接池资源耗尽时，调用者最大阻塞的时间, 超时将跑出异常
         * 单位，毫秒数;默认为-1.表示永不超时
         */
        private int maxWait = 6000;


    }


}
