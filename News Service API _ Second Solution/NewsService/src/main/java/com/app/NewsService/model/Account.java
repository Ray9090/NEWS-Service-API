package com.app.NewsService.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "account")
public class Account {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    Integer accountId;
    /**
     * The First name.
     */
    @NotBlank
    String firstName;
    /**
     * The Last name.
     */
    String lastName;
    /**
     * The Username.
     */
    @NotBlank
    @Column(unique = true)
    String username;
    /**
     * The Password.
     */
    @NotBlank
    @Size(min = 2, max = 30)
    String password;
    /**
     * The Role.
     */
    @NotBlank
    String role;

}
