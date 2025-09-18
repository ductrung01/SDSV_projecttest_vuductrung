package com.example.Project_VuDucTrung.repository;

import com.example.Project_VuDucTrung.model.Cart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CartRepository {

    private static final String FILE_PATH = "src/main/resources/data/carts.json";
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<Cart>>() {}.getType();

    public Cart findById(long cartId) {
        try (Reader reader = new FileReader(FILE_PATH)) {
            List<Cart> carts = gson.fromJson(reader, listType);
            if (carts == null) return null;
            return carts.stream()
                    .filter(c -> c.getCartId().equals(cartId))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Cart cart) {
        try (Reader reader = new FileReader(FILE_PATH)) {
            List<Cart> carts = gson.fromJson(reader, listType);
            if (carts == null) {
                carts = new ArrayList<>();
            }
            // xóa cart cũ nếu trùng id
            carts.removeIf(c -> c.getCartId().equals(cart.getCartId()));
            carts.add(cart);

            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                gson.toJson(carts, writer);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving cart", e);
        }
    }
}
