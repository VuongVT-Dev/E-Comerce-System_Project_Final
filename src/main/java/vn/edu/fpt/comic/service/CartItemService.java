package vn.edu.fpt.comic.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.comic.entity.Book;
import vn.edu.fpt.comic.entity.CartItem;
import vn.edu.fpt.comic.entity.User;
import vn.edu.fpt.comic.repository.CartItemRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookService bookService;


    /**
     * Lấy tất cả CartItem của user
     */
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    /**
     * Thêm sách vào giỏ hàng
     */
    @Transactional
    public CartItem addBookToCart(User user, Integer bookId, Integer quantity) {
        Book book = bookService.findById(bookId);

        if (book == null) {
            throw new IllegalArgumentException("Book not found!");
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByUserAndBook(user, book);

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setUpdatedAt(new Date());
            return cartItemRepository.save(existingItem);
        } else {
            // Tạo CartItem mới
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setBook(book);
            newItem.setQuantity(quantity);
            newItem.setCreatedAt(new Date());
            newItem.setUpdatedAt(new Date());
            return cartItemRepository.save(newItem);
        }
    }

    public CartItem findByUserAndBook(User user, Book book) {
        return cartItemRepository.findByUserAndBook(user, book).orElse(null);
    }

    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }



}