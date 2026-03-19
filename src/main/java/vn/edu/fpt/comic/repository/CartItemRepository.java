package vn.edu.fpt.comic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.comic.entity.Book;
import vn.edu.fpt.comic.entity.CartItem;
import vn.edu.fpt.comic.entity.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // Tìm tất cả CartItem của một user
    List<CartItem> findByUser(User user);

    // Tìm CartItem cụ thể của user và book
    Optional<CartItem> findByUserAndBook(User user, Book book);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE user_id = :userId AND book_id = :bookId", nativeQuery = true)
    void deleteByUserAndBook(Integer userId, Integer bookId);


}
