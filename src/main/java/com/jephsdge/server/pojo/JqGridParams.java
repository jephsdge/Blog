package com.jephsdge.server.pojo;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//getRoleListWithPager?_search=false&nd=1489983253884&rows=15&page=1&sidx=&sord=asc
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JqGridParams {
    //_search=false&nd=1680907056644&limit=10&page=1&sidx=blogTitle&order=asc&totalrows=&keyword=11
    //是否是查询
    private boolean _search;
    //时间戳（毫秒）
    private String nd;
    //每页显示条数
    private Integer limit;
    //当前页数
    private Integer page;
    //排序的字段
    private String sidx;
    //排序方式 asc升序  desc降序
    private String order;
    private Integer totalrows;
    private String keyword;


    //获取mybatisPlus封装的Page对象
    public <T> Page<T> generatePage(){
        Page<T> pagePlus = new Page<>();
        pagePlus.setCurrent(this.page);
        pagePlus.setSize(this.limit);
        if ("asc".equals(order) && null != sidx){
            pagePlus.addOrder(OrderItem.asc(sidx));
        }else if ("desc".equals(order) && null != sidx){
            pagePlus.addOrder(OrderItem.desc(sidx));
        }
        return pagePlus;
    }
}
