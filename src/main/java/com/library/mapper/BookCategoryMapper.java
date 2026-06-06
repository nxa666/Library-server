package com.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.entity.BookCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书分类Mapper
 */
@Mapper
public interface BookCategoryMapper extends BaseMapper<BookCategory> {
}
