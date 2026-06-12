package edu.icet.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private int size;

    private int qty;

    private String category;

    private double price;

    /** Supplier that provides this product. */
    @Column(name = "sup_id")
    private String supId;
}
