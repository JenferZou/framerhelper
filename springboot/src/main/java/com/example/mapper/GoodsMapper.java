package com.example.mapper;

import com.example.entity.Goods;

import java.util.List;

/**
 * 操作goods相关数据接口
*/
public interface GoodsMapper {

    /**
      * 新增
    */
    int insert(Goods goods);
    /*需要传进去很多内容，则用Goods*/

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Goods goods);

    /**
      * 根据ID查询
    */
    Goods selectById(Integer id);
    /*不是要表示修改成功，而是要返回一整个类*/

    /**
      * 查询所有
    */
    List<Goods> selectAll(Goods goods);

}