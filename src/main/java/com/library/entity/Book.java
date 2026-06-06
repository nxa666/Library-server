package com.library.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 图书实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("book")
public class Book {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String isbn;

    private String title;

    private String author;

    private String publisher;

    private Long categoryId;

    @TableField("cover_url")
    private String coverUrl;

    private String description;

    private String keywords;

    private String language;

    @TableField("page_count")
    private Integer pageCount;

    @TableField("publish_date")
    private LocalDate publishDate;

    private String location;

    @TableField("total_count")
    private Integer totalCount;

    @TableField("available_count")
    private Integer availableCount;

    @TableField("borrow_count")
    private Integer borrowCount;

    @TableField("favorite_count")
    private Integer favoriteCount;

    private BigDecimal rating;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
