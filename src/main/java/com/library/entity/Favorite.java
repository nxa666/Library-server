package com.library.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 收藏记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("favorite_record")
public class Favorite {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("book_id")
    private Long bookId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
