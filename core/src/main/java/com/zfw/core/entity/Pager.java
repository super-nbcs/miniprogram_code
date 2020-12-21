
package com.zfw.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * @Author:zfw
 * @Date:2019/7/22
 * @Content: 封装分页
 */

@Data
@AllArgsConstructor
public class Pager<T> {
    private List<T> content;
    private int pageSize;
    private int currentPage;
    private boolean first;
    private boolean last;
    private boolean empty;
    private int totalPages;
    private long total;

    public static Pager of(Page page){
        return  new Pager(page.getContent(),page.getSize(),page.getNumber()+1,page.isFirst(),page.isLast(),page.isEmpty(),page.getTotalPages(),page.getTotalElements());
    }

    public static Pager changContent(Pager pager, List content){
        pager.setContent(content);
        return pager;
    }
 }

