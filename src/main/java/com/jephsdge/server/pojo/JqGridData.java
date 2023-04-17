package com.jephsdge.server.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JqGridData {
    //数据
    private List<?> list;
    //当前页码
    private long curPage;
    //一共多少页
    private long totalPage;
    //一共多少记录
    private long totalCount;
    //每页多少记录
    private long pageSize;

    public static JqGridData generateJqGridData(Page<?> page){
        return new JqGridData(
                page.getRecords(),
                page.getCurrent(),
                (int) Math.ceil((double) page.getTotal() / page.getSize()),
                page.getTotal(),
                page.getSize()
        );
    }

}
