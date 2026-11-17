package com.bank.accounts.controller;

import com.bank.accounts.dto.request.AccountsCreationRequestDTO;
import com.bank.accounts.dto.request.UpdateAccountDetailsRequestDTO;
import com.bank.accounts.dto.response.ResponseDTO;
import com.bank.accounts.service.AccountsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account APIs", description = "CRUD APIs for Accounts Module")
@RestController
@RequestMapping("/account")
public class AccountsController {
    @Autowired
    private AccountsService accountsService;

    @PostMapping("/account-creation")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountsCreationRequestDTO account) {
        ResponseDTO response = accountsService.createAccount(account);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/account-details")
    public ResponseEntity<ResponseDTO> accountDetails(@RequestParam(required = false) Long accountId) {
        ResponseDTO response = accountsService.getAccountDetails(accountId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/update-account-details")
    public ResponseEntity<?> updateAccountDetails(@Valid @RequestBody UpdateAccountDetailsRequestDTO updateAccountDetailsRequestDTO) {
        ResponseDTO response = accountsService.updateAccountDetails(updateAccountDetailsRequestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@RequestParam Long accountId) {
        ResponseDTO response = accountsService.deleteAccount(accountId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
