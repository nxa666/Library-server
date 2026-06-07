package com.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.dto.FavoriteDTO;
import com.library.entity.Favorite;
import com.library.vo.FavoriteVO;

/**
 * 收藏记录Service接口
 */
public interface FavoriteService extends IService<Favorite> {

    Page<FavoriteVO> pageFavorites(Long current, Long size, Long userId);

    void addFavorite(Long userId, Long bookId);

    void removeFavorite(Long userId, Long bookId);

    boolean isFavorite(Long userId, Long bookId);
}
