package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.entity.Book;
import com.library.entity.Favorite;
import com.library.mapper.FavoriteMapper;
import com.library.service.BookService;
import com.library.service.FavoriteService;
import com.library.vo.FavoriteVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * 收藏记录Service实现
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Resource
    private BookService bookService;

    @Override
    public Page<FavoriteVO> pageFavorites(Long current, Long size, Long userId) {
        Page<Favorite> page = new Page<>(current, size);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.orderByDesc(Favorite::getCreateTime);

        Page<Favorite> favoritePage = this.page(page, wrapper);

        Page<FavoriteVO> voPage = new Page<>(current, size);
        voPage.setTotal(favoritePage.getTotal());
        voPage.setRecords(favoritePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public boolean isFavorite(Long userId, Long bookId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getBookId, bookId);
        return this.count(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, Long bookId) {
        if (isFavorite(userId, bookId)) {
            throw new RuntimeException("已收藏该图书");
        }

        Book book = bookService.getById(bookId);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setBookId(bookId);
        this.save(favorite);

        book.setFavoriteCount(book.getFavoriteCount() + 1);
        book.setUpdateTime(java.time.LocalDateTime.now());
        bookService.updateById(book);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, Long bookId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getBookId, bookId);
        boolean removed = this.remove(wrapper);
        if (!removed) {
            throw new RuntimeException("未找到收藏记录");
        }

        Book book = bookService.getById(bookId);
        if (book != null) {
            book.setFavoriteCount(Math.max(0, book.getFavoriteCount() - 1));
            book.setUpdateTime(java.time.LocalDateTime.now());
            bookService.updateById(book);
        }
    }

    private FavoriteVO convertToVO(Favorite favorite) {
        FavoriteVO vo = new FavoriteVO();
        vo.setId(favorite.getId());
        vo.setBookId(favorite.getBookId());
        vo.setCreateTime(favorite.getCreateTime());

        Book book = bookService.getById(favorite.getBookId());
        if (book != null) {
            vo.setBookTitle(book.getTitle());
            vo.setAuthor(book.getAuthor());
            vo.setPublisher(book.getPublisher());
            vo.setCoverUrl(book.getCoverUrl());
        }

        return vo;
    }
}
