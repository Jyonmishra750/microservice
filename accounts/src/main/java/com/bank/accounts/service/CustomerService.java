package com.bank.accounts.service;

import com.bank.accounts.dto.request.CustomerCreationRequestDTO;
import com.bank.accounts.dto.request.UpdateCustomerDetailsRequestDTO;
import com.bank.accounts.dto.response.ResponseDTO;

public interface CustomerService {
    ResponseDTO createCustomer(CustomerCreationRequestDTO customer);
    ResponseDTO getCustomerDetails(Long customerId);
    ResponseDTO updateCustomerDetails(UpdateCustomerDetailsRequestDTO requestDTO);
    ResponseDTO deleteCustomer(Long customerId);
}
