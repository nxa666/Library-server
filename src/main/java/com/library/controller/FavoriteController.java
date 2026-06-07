package com.library.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.common.result.Result;
import com.library.dto.FavoriteDTO;
import com.library.entity.Favorite;
import com.library.service.FavoriteService;
import com.library.vo.FavoriteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 收藏管理控制器
 */
@Tag(name = "收藏管理")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @Operation(summary = "查询当前用户收藏列表", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public Result<Page<FavoriteVO>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Page<FavoriteVO> page = favoriteService.pageFavorites(current, size, userId);
        return Result.success(page);
    }

    @Operation(summary = "添加收藏", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public Result<String> add(@RequestBody FavoriteDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.addFavorite(userId, dto.getBookId());
        return Result.success("收藏成功");
    }

    @Operation(summary = "取消收藏", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{bookId}")
    public Result<String> remove(@PathVariable Long bookId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.removeFavorite(userId, bookId);
        return Result.success("取消收藏成功");
    }

    @Operation(summary = "判断当前用户是否已收藏", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/check/{bookId}")
    public Result<Map<String, Object>> check(@PathVariable Long bookId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean favorite = favoriteService.isFavorite(userId, bookId);
        Map<String, Object> result = new HashMap<>();
        result.put("favorite", favorite);
        return Result.success(result);
    }
}
