package edu.icet.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One cart line / order line item. {@code productName} is carried for display
 * purposes only and is not persisted.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private String productId;
    private String productName;
    private int qty;
    private double unitPrice;

    public double getTotal() {
        return qty * unitPrice;
    }
}
