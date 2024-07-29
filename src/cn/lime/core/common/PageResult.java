package cn.lime.core.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PageResult
 * @Description: 分页响应
 * @Author: Lime
 * @Date: 2023/9/26 11:44
 */
@Data
public final class PageResult<T> {

    private List<T> list;

    private Integer current;

    private Integer total;

    private Integer size;

    private Integer pages;

    public PageResult(IPage<?> page, List<T> data) {

        int start = (int) ((page.getCurrent() - 1) * page.getSize() < page.getRecords().size() ? (page.getCurrent() - 1) * page.getSize() : page.getRecords().size());
        int end = (int) ((page.getCurrent()) * page.getSize() < page.getRecords().size() ? (page.getCurrent()) * page.getSize() : page.getRecords().size());
        this.list = new ArrayList<>();
        for (int i = start; i < end; i++) {
            list.add(data.get(i));
        }

        this.current = (int) page.getCurrent();
        this.total = data.size();
        this.size = (int) page.getSize();
        this.pages = total / size + 1;
    }

    public PageResult(Page<T> page) {

        this.list = new ArrayList<>();
        list.addAll(page.getRecords());

        this.current = (int) page.getCurrent();
        this.total = Math.toIntExact(page.getTotal());
        this.size = (int) page.getSize();
        this.pages = (total / size) + (total % size == 0 ? 0 : 1);
    }

}
