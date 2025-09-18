package com.example.Project_VuDucTrung.service;


import com.example.Project_VuDucTrung.model.Book;
import com.example.Project_VuDucTrung.model.Cart;
import com.example.Project_VuDucTrung.model.CartItem;
import com.example.Project_VuDucTrung.model.User;
import com.example.Project_VuDucTrung.repository.BookRepository;
import com.example.Project_VuDucTrung.repository.CartItemRepository;
import com.example.Project_VuDucTrung.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    public boolean addToCart(Long cartId, User user, Book book) {

        if (user == null) {
            throw new IllegalStateException("User must be logged in");
        }


        int stock = bookRepository.checkStock(book.getBookId());
        if (stock <= 0) {
            throw new IllegalStateException("Book is out of stock");
        }


        Cart cart = cartRepository.findById(cartId);
        if (cart == null) {
            cart = new Cart(cartId, user.getUserid(), new ArrayList<>());
        }


        CartItem cartItem = new CartItem();
        cartItem.setCart_id(cartId);
        cartItem.setBook_id(book.getBookId());
        cartItem.setQuantity(1);


        boolean saved = cartItemRepository.save(cartItem);
        if (saved) {
            cart.getListCartItem().add(cartItem);
            return true;
        }
        return false;
    }


    public boolean checkout(Long cartId, User user) {
        if (user == null) {
            throw new IllegalStateException("User must be logged in");
        }

        Cart cart = cartRepository.findById(cartId);
        if (cart == null || cart.getListCartItem().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        for (CartItem item : cart.getListCartItem()) {
            int stock = bookRepository.checkStock(item.getBook_id());
            if (stock < item.getQuantity()) {
                throw new IllegalStateException("Not enough stock for book " + item.getBook_id());
            }
        }

        for (CartItem item : cart.getListCartItem()) {
            Book book = bookRepository.findById(item.getBook_id());
            book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
            bookRepository.save(book);
        }

        cartItemRepository.deleteAll(cart.getListCartItem());
        cart.getListCartItem().clear();
        cartRepository.save(cart);

        return true;
    }
}
