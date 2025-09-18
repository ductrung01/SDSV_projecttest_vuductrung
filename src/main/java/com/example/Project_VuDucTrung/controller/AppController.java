package com.example.Project_VuDucTrung.controller;

import com.example.Project_VuDucTrung.model.Book;
import com.example.Project_VuDucTrung.model.User;
import com.example.Project_VuDucTrung.service.CartService;
import com.example.Project_VuDucTrung.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AppController {

    private final UserService userService;
    private final CartService cartService;

    public AppController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    // ====== LOGIN ======
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "Login successful";
        } else {
            return "Invalid credentials";
        }
    }

    // ====== ADD TO CART ======
    @PostMapping("/cart/{cartId}/add")
    public String addToCart(@PathVariable Long cartId,
                            @RequestBody Book book,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "Please login first";
        }

        boolean added = cartService.addToCart(cartId, user, book);
        return added ? "Book added to cart" : "Failed to add book to cart";
    }

    // ====== CHECKOUT ======
    @PostMapping("/cart/{cartId}/checkout")
    public String checkout(@PathVariable Long cartId,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "Please login first";
        }

        boolean result = cartService.checkout(cartId, user);
        return result ? "Checkout successful" : "Checkout failed";
    }
}
