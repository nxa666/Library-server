package com.library.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 收藏记录VO（返回前端）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteVO {

    private Long id;

    private Long bookId;

    private String bookTitle;

    private String author;

    private String publisher;

    private String coverUrl;

    private LocalDateTime createTime;
}
