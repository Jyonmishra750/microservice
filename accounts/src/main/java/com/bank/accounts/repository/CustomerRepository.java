package com.bank.accounts.repository;

import com.bank.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByAccountNumber(Long accountNumber);

    Optional<Customer> findByMobileNumber(String mobileNumber);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByAccountNumber(String accountNumber);
}
