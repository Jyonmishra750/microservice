package com.bank.accounts.controller;

import com.bank.accounts.dto.request.CustomerCreationRequestDTO;
import com.bank.accounts.dto.request.UpdateCustomerDetailsRequestDTO;
import com.bank.accounts.dto.response.ResponseDTO;
import com.bank.accounts.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer APIs", description = "CRUD APIs for Customer module")
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer-creation")
    public ResponseEntity<ResponseDTO> createCustomer(@Valid @RequestBody CustomerCreationRequestDTO customer) {
        ResponseDTO response = customerService.createCustomer(customer);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/customer-details")
    public ResponseEntity<ResponseDTO> customerDetails(@RequestParam(required = false) Long customerId) {
        ResponseDTO response = customerService.getCustomerDetails(customerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/update-customer-details")
    public ResponseEntity<ResponseDTO> updateCustomerDetails(
            @Valid @RequestBody UpdateCustomerDetailsRequestDTO updateCustomerDetailsRequestDTO) {
        ResponseDTO response = customerService.updateCustomerDetails(updateCustomerDetailsRequestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> deleteCustomer(@RequestParam Long customerId) {
        ResponseDTO response = customerService.deleteCustomer(customerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
