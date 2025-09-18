package com.example.Project_VuDucTrung.service;


import com.example.Project_VuDucTrung.model.Book;
import com.example.Project_VuDucTrung.model.Cart;
import com.example.Project_VuDucTrung.model.CartItem;
import com.example.Project_VuDucTrung.model.User;
import com.example.Project_VuDucTrung.repository.BookRepository;
import com.example.Project_VuDucTrung.repository.CartItemRepository;
import com.example.Project_VuDucTrung.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addBookToCart_whenLoginAndAvailable_shouldSucceed() {
        //data
        Book sampleBook = Book.builder()
                .bookId(1L)
                .title("Clean Code")
                .price(450000.0)
                .stockQuantity(20)
                .build();

        User user = new User(
                1L, "vuductrung", "123456",
                "trungvu@samsung.com", "Vu Duc Trung",
                "123 Đường A, Hà Nội", "0335654203", "USER"
        );

        Cart cart = new Cart(1L, 1L, new ArrayList<>());

        //mock
        when(cartRepository.findById(1L)).thenReturn(cart);
        when(bookRepository.checkStock(1L)).thenReturn(20);
        when(cartItemRepository.save(org.mockito.Mockito.any(CartItem.class))).thenReturn(true);


        boolean actual = cartService.addToCart(1L, user, sampleBook);

        assertTrue(actual);
        assertEquals(1, cart.getListCartItem().size());
        assertEquals(1L, cart.getListCartItem().get(0).getBook_id());
    }

    @Test
    void addBookToCart_whenNotLoggedIn_shouldThrow() {
        Book sampleBook = Book.builder()
                .bookId(1L)
                .title("Clean Code")
                .build();

        assertThrows(IllegalStateException.class,
                () -> cartService.addToCart(1L, null, sampleBook));
    }

    @Test
    void addBookToCart_whenOutOfStock_shouldThrow() {
        Book sampleBook = Book.builder()
                .bookId(1L)
                .title("Clean Code")
                .build();

        User user = new User(1L, "vuductrung", "123456",
                "trungvu@samsung.com", "Tú Demo",
                "123 Đường A, Hà Nội", "0912345678", "USER");

        when(bookRepository.checkStock(1L)).thenReturn(0);

        assertThrows(IllegalStateException.class,
                () -> cartService.addToCart(1L, user, sampleBook));
    }



    @Test
    void checkout_whenStockEnough_shouldSucceed() {
        User user = new User(1L,"trungvu","123","mail","name","addr","phone","USER");

        CartItem item = new CartItem();
        item.setCart_item_id(1L);
        item.setBook_id(1L);
        item.setQuantity(2);

        Cart cart = new Cart(1L,1L, new ArrayList<>(List.of(item)));

        Book book = Book.builder()
                .bookId(1L)
                .title("Clean Code")
                .stockQuantity(5)
                .build();

        when(cartRepository.findById(1L)).thenReturn(cart);
        when(bookRepository.checkStock(1L)).thenReturn(5);
        when(bookRepository.findById(1L)).thenReturn(book);

        boolean result = cartService.checkout(1L, user);

        assertTrue(result);
        assertEquals(3, book.getStockQuantity());
        verify(cartItemRepository, times(1)).deleteAll(cart.getListCartItem());
    }

    @Test
    void checkout_whenOutOfStock_shouldThrow() {
        User user = new User(1L,"trungvu","123","mail","name","addr","phone","USER");

        CartItem item = new CartItem();
        item.setCart_item_id(1L);
        item.setBook_id(1L);
        item.setQuantity(10);

        Cart cart = new Cart(1L,1L, List.of(item));

        when(cartRepository.findById(1L)).thenReturn(cart);
        when(bookRepository.checkStock(1L)).thenReturn(5);

        assertThrows(IllegalStateException.class,
                () -> cartService.checkout(1L, user));
    }

    @Test
    void checkout_whenUserNotLoggedIn_shouldThrow() {
        assertThrows(IllegalStateException.class,
                () -> cartService.checkout(1L, null));
    }


    @Test
    void checkout_whenCartEmpty_shouldThrow() {
        User user = new User(1L,"trungvu","123","mail","name","addr","phone","USER");

        Cart emptyCart = new Cart(1L, 1L, new ArrayList<>());

        when(cartRepository.findById(1L)).thenReturn(emptyCart);

        assertThrows(IllegalStateException.class,
                () -> cartService.checkout(1L, user));
    }
}
