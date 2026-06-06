package com.library.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 图书分类实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("book_category")
public class BookCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("category_name")
    private String categoryName;

    private Long parentId;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
