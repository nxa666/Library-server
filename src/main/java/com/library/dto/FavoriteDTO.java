package com.library.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 收藏DTO（添加收藏）
 */
@Data
public class FavoriteDTO {

    @NotNull(message = "图书ID不能为空")
    private Long bookId;
}
