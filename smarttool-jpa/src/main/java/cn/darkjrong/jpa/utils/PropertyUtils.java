package cn.darkjrong.jpa.utils;

import cn.darkjrong.pager.vo.PageVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 属性工具类
 * @author Rong.Jia
 * @date 2020/01/14 09:22
 */
public class PropertyUtils {

    /**
     * 复制属性
     *
     * @param page    分页查询结果对象
     * @param records VO对象集合
     * @return {@link PageVO}<{@link T}>
     */
    public static <T> PageVO<T> copyProperties(Page<?> page, List<T> records) {
        PageVO<T> pageVO = new PageVO<>();
        copyProperties(page, pageVO, records);
        return pageVO;
    }


    /**
     * 复制属性
     *
     * @param page    分页查询结果对象
     * @param pageVO  分页查询结果vo对象
     * @param records VO对象集合
     */
    public static <T> void copyProperties(Page<?> page, PageVO<T> pageVO, List<T> records) {

        pageVO.setTotalPages(page.getTotalPages());
        pageVO.setHasNext(page.hasNext());
        pageVO.setHasPrevious(page.hasPrevious());
        pageVO.setIsFirst(page.isFirst());
        pageVO.setIsLast(page.isLast());
        pageVO.setTotal((int)page.getTotalElements());
        pageVO.setCurrentPage(page.getNumber());
        pageVO.setPageSize(page.getSize());
        pageVO.setRecords(records);

    }

    /**
     * 复制属性
     *
     * @param page   分页查询结果对象
     * @param pageVO 分页查询结果vo对象
     */
    public static <T> void copyProperties(Page<?> page, PageVO<T> pageVO) {
        copyProperties(page, pageVO, pageVO.getRecords());
    }



}
