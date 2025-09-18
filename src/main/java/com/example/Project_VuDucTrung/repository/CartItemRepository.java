package com.example.Project_VuDucTrung.repository;

import com.example.Project_VuDucTrung.model.CartItem;
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
public class CartItemRepository {

    private static final String FILE_PATH = "src/main/resources/data/cartItems.json";
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<CartItem>>() {}.getType();

    public boolean save(CartItem cartItem) {
        try {
            // load hiện tại
            List<CartItem> items;
            try (Reader reader = new FileReader(FILE_PATH)) {
                items = gson.fromJson(reader, listType);
                if (items == null) items = new ArrayList<>();
            }

            // nếu đã tồn tại id thì xóa để update
            items.removeIf(ci -> ci.getCart_item_id().equals(cartItem.getCart_item_id()));
            items.add(cartItem);

            // ghi lại file
            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                gson.toJson(items, writer);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void deleteAll(List<CartItem> listCartItem) {
        try {
            // load toàn bộ cart items hiện tại
            List<CartItem> items;
            try (Reader reader = new FileReader(FILE_PATH)) {
                items = gson.fromJson(reader, listType);
                if (items == null) items = new ArrayList<>();
            }

            // xóa các cart item có id trùng trong listCartItem
            for (CartItem ci : listCartItem) {
                items.removeIf(item -> item.getCart_item_id().equals(ci.getCart_item_id()));
            }

            // ghi lại file JSON
            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                gson.toJson(items, writer);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting cart items", e);
        }
    }
}

