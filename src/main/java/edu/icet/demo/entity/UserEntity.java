package edu.icet.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Shop staff account (owner/admin or employee).
 * Table is named "users" because "user" is a reserved word in PostgreSQL.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    private String email;

    /** SHA-256 hex digest of the password - never stored in plain text. */
    @Column(nullable = false)
    private String password;

    private String role;

    private String address;
}
