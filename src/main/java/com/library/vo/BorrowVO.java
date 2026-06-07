package com.library.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 借阅记录VO（返回前端）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowVO {

    private Long id;

    private Long userId;

    private Long bookId;

    private String bookTitle;

    private String author;

    private String username;

    private String realName;

    private LocalDateTime borrowTime;

    private LocalDateTime dueTime;

    private LocalDateTime returnTime;

    private String status;

    private String statusText;

    private Integer renewCount;

    private String remark;

    private LocalDateTime createTime;
}
