package cn.darkjrong.jpa.utils;

import cn.darkjrong.pager.dto.SortDTO;
import org.springframework.data.domain.Sort;

/**
 * 排序封装工具类
 *
 * @author Rong.Jia
 * @date 2021/12/20
 */
public class SortUtils {

    public static Sort basicSort() {
        return basicSort(SortDTO.DEFAULT_ORDER_TYPE, SortDTO.DEFAULT_ORDER_FIELD);
    }

    public static Sort basicSort(String orderType, String orderField) {
        return Sort.by(Sort.Direction.fromString(orderType), orderField);
    }

    public static Sort basicSort(SortDTO... dtos) {
        Sort result = null;
        for (SortDTO dto : dtos) {
            if (result == null) {
                result = Sort.by(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField());
            } else {
                result = result.and(Sort.by(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }
        return result;
    }
}
