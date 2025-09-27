package cn.darkjrong.swagger.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;

import java.util.List;
import java.util.function.Predicate;

/**
 * Swagger工具类
 *
 * @author 贾荣
 * @date 2021/05/13 17:22
 */
public class SwaggerUtils {

	/**
	 * 扫描包
	 * @param basePackages 包集合
	 * @return RequestHandler
	 */
	public static Predicate<RequestHandler> basePackages(List<String> basePackages) {
		Predicate<RequestHandler> predicate = null;
		for (String basePackage : basePackages) {
			if(StrUtil.isNotBlank(basePackage)){
				Predicate<RequestHandler> tempPredicate = RequestHandlerSelectors.basePackage(basePackage);
				predicate = ObjectUtil.isNull(predicate) ? tempPredicate : predicate.or(tempPredicate);
			}
		}

		return predicate;
	}

}
