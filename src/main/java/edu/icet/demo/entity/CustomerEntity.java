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
@Table(name = "customer")
public class CustomerEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String email;

    private String address;

    /** Id of the staff member who registered this customer. */
    @Column(name = "emp_id")
    private String empId;
}
