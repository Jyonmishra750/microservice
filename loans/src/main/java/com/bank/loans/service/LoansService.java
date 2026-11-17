package com.bank.loans.service;

import com.bank.loans.dto.request.UpdateLoanDetailsRequestDTO;
import com.bank.loans.dto.response.ResponseDTO;

public interface LoansService {
    ResponseDTO createLoan(String mobileNumber);

    ResponseDTO fetchLoan(String mobileNumber);

    ResponseDTO updateLoan(UpdateLoanDetailsRequestDTO updateLoanDetailsRequestDTO);

    ResponseDTO deleteLoan(String mobileNumber);
}
