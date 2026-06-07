package com.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.dto.BorrowDTO;
import com.library.entity.Borrow;
import com.library.vo.BorrowVO;

/**
 * 借阅记录Service接口
 */
public interface BorrowService extends IService<Borrow> {

    Page<BorrowVO> pageBorrows(Long current, Long size, Long userId, Long bookId, String status);

    BorrowVO getBorrowById(Long id);

    void borrowBook(BorrowDTO dto);

    void returnBook(Long id);

    void renewBook(Long id);
}
