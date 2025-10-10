package cn.darkjrong.ftpclient.pool;

import cn.darkjrong.ftpclient.exceptions.FtpClientException;
import cn.darkjrong.spring.boot.autoconfigure.FtpClientProperties;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * ftp客户端池工厂
 * 通过FTPClient工厂提供FTPClient实例的创建和销毁
 *
 * @author Rong.Jia
 * @date 2022/01/06
 */
@Slf4j
@Configuration
public class FtpClientPoolFactory extends BasePooledObjectFactory<FTPClient> {

    private final FtpClientProperties ftpClientProperties;

    public FtpClientPoolFactory(FtpClientProperties ftpClientProperties) {
        this.ftpClientProperties = ftpClientProperties;
    }

    /**
     * 创建对象
     *
     * @return {@link FTPClient }
     */
    @Override
    public FTPClient create() {
        return connectFtpServer(ftpClientProperties);
    }

    /**
     * 封装对象放入池中
     *
     * @param ftpClient 对象
     * @return {@link PooledObject }<{@link FTPClient }>
     */
    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    /**
     * 销毁FtpClient对象
     *
     * @param ftpPooled ftp池
     * @throws Exception 异常
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> ftpPooled) throws Exception {
        if (ftpPooled == null) {
            return;
        }
        FTPClient ftpClient = ftpPooled.getObject();
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (Exception e) {
            log.error(String.format("ftp client logout failed, 【%s】", e.getMessage()), e);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (Exception e) {
                log.error(String.format("close ftp client failed,【%s】", e.getMessage()), e);
            }
        }


    }

    /**
     * 验证FtpClient对象
     *
     * @param ftpPooled ftp池
     * @return boolean
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> ftpPooled) {
        try {
            FTPClient ftpClient = ftpPooled.getObject();
            return ftpClient.sendNoOp();
        } catch (Exception e) {
            log.error(String.format("Failed to validate client: 【%s】", e.getMessage()), e);
        }
        return false;
    }

    /**
     * 连接ftp服务器
     *
     * @param ftpClientProperties ftp客户端属性
     * @return {@link FTPClient}
     */
    private FTPClient connectFtpServer(FtpClientProperties ftpClientProperties){
        String host = ftpClientProperties.getHost();
        Integer port = ftpClientProperties.getPort();
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(ftpClientProperties.getEncoding());
        Duration dataTimeout = ftpClientProperties.getDataTimeout();
        if (ObjectUtil.isNotNull(dataTimeout)) {
            ftpClient.setDataTimeout(dataTimeout);
        }
        ftpClient.setConnectTimeout(Convert.toInt(ftpClientProperties.getConnectTimeout().toMillis()));
        ftpClient.setControlKeepAliveTimeout(ftpClientProperties.getKeepAliveTimeout());
        if (ftpClientProperties.isPassiveMode()) {
            ftpClient.enterLocalPassiveMode();
        }
        try {
            ftpClient.connect(host, port);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)){
                log.error("connect ftp {} failed", host);
                ftpClient.disconnect();
                throw new FtpClientException(String.format("connect ftp %s failed", host));
            }
            if (!ftpClient.login(ftpClientProperties.getUsername(), ftpClientProperties.getPassword())) {
                log.warn("ftpClient login failed... username is {}; password: {}",
                        ftpClientProperties.getUsername(), ftpClientProperties.getPassword());
            }
            ftpClient.setBufferSize(ftpClientProperties.getBufferSize());
            ftpClient.setFileType(ftpClientProperties.getTransferFileType());
        } catch (Exception e) {
            log.error(String.format("【%s:%s】connect fail,【%s】", host, port, e.getMessage()),e);
            throw new FtpClientException(e);
        }
        return ftpClient;
    }

}
