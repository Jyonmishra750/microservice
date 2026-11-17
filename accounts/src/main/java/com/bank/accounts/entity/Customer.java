package com.bank.accounts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;
}