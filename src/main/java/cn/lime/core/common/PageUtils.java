package cn.lime.core.common;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName: PageUtils
 * @Description: 分页工具
 * @Author: Lime
 * @Date: 2023/9/20 11:29
 */
public class PageUtils {

    public static Page<?> build(Integer current, Integer pageSize, String sortField, String orderType){
        if (ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(pageSize)){
            return null;
        }
        Page<?> page = new Page<>(current, pageSize);
        page.setOptimizeCountSql(false);
        if (StringUtils.isNotEmpty(sortField) && StringUtils.isNotEmpty(orderType)) {
            page = orderType.equals(PageRequest.SORT_ORDER_ASC) ?
                    page.addOrder(OrderItem.asc(sortField)) : page.addOrder(OrderItem.desc(sortField));
        }
        return page;
    }

    public static <T> Page<T> build(Class<T> clazz,Integer current, Integer pageSize, String sortField, String orderType){
        if (ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(pageSize)){
            return null;
        }
        Page<T> page = new Page<>(current, pageSize);
        page.setOptimizeCountSql(false);
        if (StringUtils.isNotEmpty(sortField) && StringUtils.isNotEmpty(orderType)) {
            page = orderType.equals(PageRequest.SORT_ORDER_ASC) ?
                    page.addOrder(OrderItem.asc(sortField)) : page.addOrder(OrderItem.desc(sortField));
        }
        return page;
    }

}
