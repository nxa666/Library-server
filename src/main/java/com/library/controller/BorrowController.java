package com.library.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.common.result.Result;
import com.library.dto.BorrowDTO;
import com.library.service.BorrowService;
import com.library.vo.BorrowVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 借阅管理控制器
 */
@Tag(name = "借阅管理")
@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    @Resource
    private BorrowService borrowService;

    @Operation(summary = "分页查询借阅记录", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public Result<Page<BorrowVO>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) String status) {
        Page<BorrowVO> page = borrowService.pageBorrows(current, size, userId, bookId, status);
        return Result.success(page);
    }

    @Operation(summary = "查询借阅详情", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public Result<BorrowVO> getById(@PathVariable Long id) {
        BorrowVO vo = borrowService.getBorrowById(id);
        return Result.success(vo);
    }

    @Operation(summary = "借书", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public Result<String> borrow(@RequestBody BorrowDTO dto) {
        borrowService.borrowBook(dto);
        return Result.success("借书成功");
    }

    @Operation(summary = "还书", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}/return")
    public Result<String> returnBook(@PathVariable Long id) {
        borrowService.returnBook(id);
        return Result.success("还书成功");
    }

    @Operation(summary = "续借", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}/renew")
    public Result<String> renew(@PathVariable Long id) {
        borrowService.renewBook(id);
        return Result.success("续借成功");
    }
}
