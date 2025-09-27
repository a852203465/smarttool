package cn.darkjrong.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ReflectUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Bean 操作工具类
 *
 * @author Rong.Jia
 * @date 2020/4/11 23:17
 */
public class BeanUtils {

    /**
     * 复制Bean对象属性
     * @param fromColl 待转换对象集合
     * @param clazz 目标对象class
     * @param <S>
     * @param <T>
     * @return 目标对象集合
     */
    public static <S, T> List<T> copyProperties(Collection<S> fromColl, Class<T> clazz) {

        if (CollUtil.isEmpty(fromColl)) {
            return null;
        }

        List<T> toList = CollUtil.newArrayList();
        for (S from : fromColl) {
            T t = cn.hutool.core.bean.BeanUtil.copyProperties(from, clazz);
            Optional.ofNullable(t).ifPresent(toList::add);
        }

        return toList;
    }

    /**
     * 复制Bean对象属性
     * @param fromColl 待转换对象集合
     * @param clazz 目标对象class
     * @param ignoreProperties 不拷贝的的属性列表
     * @param <S>
     * @param <T>
     * @return 目标对象集合
     */
    public static <S, T> List<T> copyProperties(Collection<S> fromColl, Class<T> clazz, String... ignoreProperties) {

        if (CollUtil.isEmpty(fromColl)) {
            return null;
        }

        List<T> toList = CollUtil.newArrayList();
        for (S from : fromColl) {
            T t = copyProperties(from, clazz, ignoreProperties);
            Optional.ofNullable(t).ifPresent(toList::add);
        }

        return toList;
    }

    /**
     * 复制Bean对象属性
     * @param from 源对象
     * @param clazz 目标对象class
     * @param <S>
     * @param <T>
     * @param ignoreProperties  不拷贝的的属性列表
     * @return 目标对象
     */
    public static <S, T> T copyProperties(S from, Class<T> clazz, String... ignoreProperties) {

        T to = null;

        try {
            to = ReflectUtil.newInstance(clazz);
        } catch (UtilException e) {
            return null;
        }

        cn.hutool.core.bean.BeanUtil.copyProperties(from, to, ignoreProperties);
        return to;
    }


}
