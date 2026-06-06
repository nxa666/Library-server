package com.library.controller;

import com.library.common.result.Result;
import com.library.mapper.BookMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@Tag(name = "测试接口")
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private BookMapper bookMapper;

    @Operation(summary = "测试接口")
    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("library-server running");
    }

    @Operation(summary = "查询所有图书")
    @GetMapping("/test/book")
    public Object book() {
        return bookMapper.selectList(null);
    }
}
