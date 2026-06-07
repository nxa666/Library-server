package com.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏记录Mapper
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
