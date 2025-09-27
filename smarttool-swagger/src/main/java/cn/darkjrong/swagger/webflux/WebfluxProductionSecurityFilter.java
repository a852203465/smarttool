package cn.darkjrong.swagger.webflux;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * webflux生产安全过滤器
 *
 * @author Rong.Jia
 * @date 2022/02/11
 */
@Data
public class WebfluxProductionSecurityFilter implements WebFilter {

    /***
     * 是否生产环境,如果是生成环境,过滤Swagger的相关资源请求
     */
    private boolean production = false;
    protected List<Pattern> urlFilters= new ArrayList<>();

    public WebfluxProductionSecurityFilter(boolean production) {
        this();
        this.production = production;
    }

    public WebfluxProductionSecurityFilter(){
        urlFilters.add(Pattern.compile(".*?/doc\\.html.*",Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/v2/api-docs.*",Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/v3/api-docs.*",Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/v2/api-docs-ext.*",Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources.*",Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-ui\\.html.*",Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-ui.*",Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources/configuration/ui.*",Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources/configuration/security.*",Pattern.CASE_INSENSITIVE));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        if (production){
            String uri=request.getPath().value();
            if (!match(uri)){
                return chain.filter(exchange);
            }else{
                ServerHttpResponse originalResponse = exchange.getResponse();
                DataBufferFactory bufferFactory = originalResponse.bufferFactory();
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                DataBufferUtils.release(dataBuffer);
                                return bufferFactory.wrap(StrUtil.bytes("You do not have permission to access this page"));
                            }));
                        }
                        return super.writeWith(body);
                    }
                };

                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
        }

        return chain.filter(exchange);
    }

    /**
     * 匹配
     *
     * @param uri uri
     * @return boolean
     */
    private boolean match(String uri){
        boolean match=false;
        if (uri!=null){
            for (Pattern pattern: getUrlFilters()){
                if (pattern.matcher(uri).matches()){
                    match=true;
                    break;
                }
            }
        }
        return match;
    }

}
