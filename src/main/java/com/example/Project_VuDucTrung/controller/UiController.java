package com.example.Project_VuDucTrung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // trả về login.html
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";   // trả về cart.html
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "checkout"; // trả về checkout.html
    }
}
