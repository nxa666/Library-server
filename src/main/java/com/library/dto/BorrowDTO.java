package com.library.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 借阅记录DTO（借书）
 */
@Data
public class BorrowDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "图书ID不能为空")
    private Long bookId;
}
