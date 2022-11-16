package com.aminov.corporativelibrary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aminov.corporativelibrary.models.UserBook;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    
    @Query(value = 
    "SELECT * FROM USER_BOOK " + 
    "ORDER BY ISSUE_DATE ",
    nativeQuery = true)
    public List<UserBook> findListOrderByIssueDate();
    
    @Query(value = 
    "SELECT * FROM USER_BOOK " + 
    "WHERE USER_ID = :user_id " +
    "ORDER BY ISSUE_DATE ",
    nativeQuery = true)
    public List<UserBook> findListByUserId(@Param("user_id") Long user_id);
    
    @Query(value = 
    "SELECT * FROM USER_BOOK " + 
    "WHERE BOOK_VENDOR_CODE = :vendor_code " +
    "ORDER BY ISSUE_DATE ",
    nativeQuery = true)
    public List<UserBook> findListByBookId(@Param("vendor_code") Long vendor_code);
    
    @Query(value = 
    "SELECT ub.* FROM USER_BOOK ub, BOOK b " + 
    "WHERE RETURN_DATE = '1970-01-01 05:00:00' AND ub.BOOK_VENDOR_CODE = b.VENDOR_CODE " +
    "ORDER BY ISSUE_DATE ",
    nativeQuery = true)
    public List<UserBook> findReserveListByBookId(@Param("vendor_code") Long vendor_code);
    
}
