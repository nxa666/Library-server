package com.library.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 借阅记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("borrow_record")
public class Borrow {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("book_id")
    private Long bookId;

    @TableField("borrow_time")
    private LocalDateTime borrowTime;

    @TableField("due_time")
    private LocalDateTime dueTime;

    @TableField("return_time")
    private LocalDateTime returnTime;

    private String status; // BORROWED / RETURNED / OVERDUE

    @TableField("renew_count")
    private Integer renewCount;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
