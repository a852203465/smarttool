package cn.darkjrong.core.lang.constants;

import cn.hutool.core.io.unit.DataSize;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.system.SystemUtil;

/**
 * 文件常量类
 *
 * @author Rong.Jia
 * @date 2020/4/11 21:54
 */
public class FileConstant {

    public static final String TMP_DIR = SystemUtil.get(SystemUtil.USER_DIR) + "/data/tmp";

    public static final String JPEG_SUFFIX = ".jpeg";

    public static final String JPG_SUFFIX = ".jpg";

    public static final String PNG_SUFFIX = ".png";

    public static final String EXE_SUFFIX = ".exe";

    public static final String JSON_SUFFIX = ".json";

    public static final String EXCEL_SUFFIX = ".xls";

    public static final String ZIP_FILE_SUFFIX = ".zip";

    public static final String PART_SUFFIX = ".part";

    public static final String LOG_SUFFIX = ".log";

    public static final Long PART_SIZE = DataSize.of(10, DataUnit.MEGABYTES).toMegabytes();

    public static final int LIMIT_SPEED = 200 * 1024;

    public static final Long EXPIRATION_TIME = 7200L * 1000;




}
