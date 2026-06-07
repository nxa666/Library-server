package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.dto.BorrowDTO;
import com.library.entity.Borrow;
import com.library.entity.Book;
import com.library.entity.User;
import com.library.mapper.BorrowMapper;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.UserService;
import com.library.vo.BorrowVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 借阅记录Service实现
 */
@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {

    private static final Map<String, String> STATUS_TEXT_MAP = Map.of(
            "BORROWED", "借出中",
            "RETURNED", "已归还",
            "OVERDUE", "已逾期"
    );

    @Resource
    private BookService bookService;

    @Resource
    private UserService userService;

    @Override
    public Page<BorrowVO> pageBorrows(Long current, Long size, Long userId, Long bookId, String status) {
        Page<Borrow> page = new Page<>(current, size);
        LambdaQueryWrapper<Borrow> wrapper = new LambdaQueryWrapper<>();

        if (userId != null) {
            wrapper.eq(Borrow::getUserId, userId);
        }
        if (bookId != null) {
            wrapper.eq(Borrow::getBookId, bookId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Borrow::getStatus, status);
        }
        wrapper.orderByDesc(Borrow::getBorrowTime);

        Page<Borrow> borrowPage = this.page(page, wrapper);

        Page<BorrowVO> voPage = new Page<>(current, size);
        voPage.setTotal(borrowPage.getTotal());
        voPage.setRecords(borrowPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public BorrowVO getBorrowById(Long id) {
        Borrow borrow = this.getById(id);
        if (borrow == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        return convertToVO(borrow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void borrowBook(BorrowDTO dto) {
        Book book = bookService.getById(dto.getBookId());
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }
        if (book.getAvailableCount() <= 0) {
            throw new RuntimeException("库存不足");
        }

        Borrow borrow = new Borrow();
        borrow.setUserId(dto.getUserId());
        borrow.setBookId(dto.getBookId());
        borrow.setBorrowTime(LocalDateTime.now());
        borrow.setDueTime(LocalDateTime.now().plusDays(30));
        borrow.setStatus("BORROWED");
        borrow.setRenewCount(0);
        borrow.setRemark("正常借阅");
        this.save(borrow);

        book.setAvailableCount(book.getAvailableCount() - 1);
        book.setBorrowCount(book.getBorrowCount() + 1);
        book.setUpdateTime(LocalDateTime.now());
        bookService.updateById(book);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnBook(Long id) {
        Borrow borrow = this.getById(id);
        if (borrow == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        if (!"BORROWED".equals(borrow.getStatus())) {
            throw new RuntimeException("图书已归还");
        }

        borrow.setStatus("RETURNED");
        borrow.setReturnTime(LocalDateTime.now());
        borrow.setUpdateTime(LocalDateTime.now());
        this.updateById(borrow);

        Book book = bookService.getById(borrow.getBookId());
        if (book != null) {
            book.setAvailableCount(book.getAvailableCount() + 1);
            book.setUpdateTime(LocalDateTime.now());
            bookService.updateById(book);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void renewBook(Long id) {
        Borrow borrow = this.getById(id);
        if (borrow == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        if (!"BORROWED".equals(borrow.getStatus())) {
            throw new RuntimeException("只能续借借出中的图书");
        }

        borrow.setDueTime(borrow.getDueTime().plusDays(30));
        borrow.setRenewCount(borrow.getRenewCount() + 1);
        borrow.setUpdateTime(LocalDateTime.now());
        this.updateById(borrow);
    }

    private BorrowVO convertToVO(Borrow borrow) {
        BorrowVO vo = new BorrowVO();
        vo.setId(borrow.getId());
        vo.setUserId(borrow.getUserId());
        vo.setBookId(borrow.getBookId());
        vo.setBorrowTime(borrow.getBorrowTime());
        vo.setDueTime(borrow.getDueTime());
        vo.setReturnTime(borrow.getReturnTime());
        vo.setStatus(borrow.getStatus());
        vo.setStatusText(STATUS_TEXT_MAP.getOrDefault(borrow.getStatus(), borrow.getStatus()));
        vo.setRenewCount(borrow.getRenewCount());
        vo.setRemark(borrow.getRemark());
        vo.setCreateTime(borrow.getCreateTime());

        Book book = bookService.getById(borrow.getBookId());
        if (book != null) {
            vo.setBookTitle(book.getTitle());
            vo.setAuthor(book.getAuthor());
        }

        User user = userService.getById(borrow.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setRealName(user.getRealName());
        }

        return vo;
    }
}
