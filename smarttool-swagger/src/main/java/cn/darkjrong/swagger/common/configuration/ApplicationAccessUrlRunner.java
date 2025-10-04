package cn.darkjrong.swagger.common.configuration;

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

    private final Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String appName = env.getProperty("spring.application.name");
        String name = StrUtil.isBlank(appName) ? "default" : appName;
        String port = StrUtil.isBlank(env.getProperty("server.port"))
                ? "8080" : env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}/doc.html\n\t" +
                        "External: \thttp://{}:{}\n\t"+
                        "Doc: \thttp://{}:{}/doc.html\n"+
                        "----------------------------------------------------------",
                name, port,
                InetAddress.getLocalHost().getHostAddress(), port,
                InetAddress.getLocalHost().getHostAddress(), port);
    }
}
