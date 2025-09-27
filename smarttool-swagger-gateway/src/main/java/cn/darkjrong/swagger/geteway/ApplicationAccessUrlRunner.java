package cn.darkjrong.swagger.geteway;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 应用程序启动后输出访问地址
 *
 * @author Rong.Jia
 * @date 2022/02/15
 */
@Slf4j
@Component
@AllArgsConstructor
public class ApplicationAccessUrlRunner implements ApplicationRunner {

    private static final String DEFAULT_NAME = "default";
    private static final String DEFAULT_PORT = "8080";

    private final Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String applicationName = StrUtil.isBlank(env.getProperty("spring.application.name")) ? DEFAULT_NAME : env.getProperty("spring.application.name");
        String port = StrUtil.isBlank(env.getProperty("server.port")) ? DEFAULT_PORT : env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}/doc.html\n\t" +
                        "External: \thttp://{}:{}\n\t"+
                        "Doc: \thttp://{}:{}/doc.html\n"+
                        "----------------------------------------------------------",

                applicationName, port,
                InetAddress.getLocalHost().getHostAddress(), port,
                InetAddress.getLocalHost().getHostAddress(), port);
    }
}
