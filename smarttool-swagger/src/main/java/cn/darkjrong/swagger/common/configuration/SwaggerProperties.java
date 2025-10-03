package cn.darkjrong.swagger.common.configuration;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import cn.darkjrong.swagger.common.enums.ScanMatchingRuleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SwaggerProperties
 *
 * @author Rong.Jia
 * @date 2021/12/20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.swagger")
public class SwaggerProperties {

	/**
	 * 是否是生产环境,默认为false
	 */
	private boolean production = false;

	/**
	 * swagger会解析的包路径
	 **/
	private List<String> basePackages = new ArrayList<>();

	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();

	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();

	/**
	 * 标题
	 **/
	private String title = "";

	/**
	 * API路径前缀
	 */
	private String pathMapping = "";

	/**
	 * 描述
	 **/
	private String description = "";
	/**
	 * 版本
	 **/
	private String version = "";
	/**
	 * 许可证
	 **/
	private String license = "";
	/**
	 * 许可证URL
	 **/
	private String licenseUrl = "";
	/**
	 * 服务条款URL
	 **/
	private String termsOfServiceUrl = "";

	/**
	 * host信息
	 **/
	private String host = "";
	/**
	 * 联系人信息
	 */
	private Contact contact = new Contact();

	/**
	 * 忽略的参数类型
	 **/
	private List<Class<?>> ignoredParameterTypes = new ArrayList<>();

	/**
	 * 分组文档
	 **/
	private Map<String, DocketInfo> docket = new LinkedHashMap<>();

	/**
	 * 个性化配置
	 */
	private OpenApiExtendSetting setting;

	@Data
	@NoArgsConstructor
	public static class Contact {

		/**
		 * 联系人
		 **/
		private String name = "";
		/**
		 * 联系人url
		 **/
		private String url = "";
		/**
		 * 联系人email
		 **/
		private String email = "";

	}

	@Data
	@NoArgsConstructor
	public static class DocketInfo {

		/**
		 * 分组名
		 */
		private String groupName = "";

		/**
		 * swagger会解析的包路径
		 **/
		private List<String> basePackages = new ArrayList<>();

		/**
		 * swagger解析的包路径的过滤规则
		 */
		private ScanMatchingRuleEnum basePackageRegex = ScanMatchingRuleEnum.startsWith;

		/**
		 * swagger会解析的url规则
		 **/
		private List<String> basePath = new ArrayList<>();
		/**
		 * 在basePath基础上需要排除的url规则
		 **/
		private List<String> excludePath = new ArrayList<>();
		/**
		 * 标题
		 **/
		private String title = "";

		/**
		 * API路径前缀
		 */
		private String pathMapping = "";
		/**
		 * 描述
		 **/
		private String description = "";
		/**
		 * 版本
		 **/
		private String version = "";
		/**
		 * 许可证
		 **/
		private String license = "";
		/**
		 * 许可证URL
		 **/
		private String licenseUrl = "";
		/**
		 * 服务条款URL
		 **/
		private String termsOfServiceUrl = "";

		/**
		 * host信息
		 **/
		private String host = "";
		/**
		 * 联系人信息
		 */
		private Contact contact = new Contact();

		/**
		 * 忽略的参数类型
		 **/
		private List<Class<?>> ignoredParameterTypes = new ArrayList<>();

	}

















}
