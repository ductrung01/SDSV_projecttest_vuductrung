package com.example.Project_VuDucTrung.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    private Long cart_item_id;
    private Long book_id;
    private Long cart_id;
    private int quantity;
}
