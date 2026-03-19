package vn.edu.fpt.comic.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

}