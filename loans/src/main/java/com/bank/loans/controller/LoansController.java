package com.bank.loans.controller;

import com.bank.loans.dto.request.UpdateLoanDetailsRequestDTO;
import com.bank.loans.dto.response.ResponseDTO;
import com.bank.loans.service.LoansService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Loan APIs", description = "CRUD APIs for Loans Module")
@RestController
@RequestMapping("/loans")
public class LoansController {
    @Autowired
    private LoansService loansService;

    @PostMapping("/create-loan")
    public ResponseEntity<?> createLoan(@RequestParam
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                  String mobileNumber) {
        ResponseDTO response = loansService.createLoan(mobileNumber);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/fetch-loan")
    public ResponseEntity<?> fetchLoanDetails(@RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber) {
        ResponseDTO response = loansService.fetchLoan(mobileNumber);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/update-loan")
    public ResponseEntity<?> updateLoanDetails(@Valid @RequestBody UpdateLoanDetailsRequestDTO updateLoanDetailsRequestDTO) {
        ResponseDTO response = loansService.updateLoan(updateLoanDetailsRequestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete-loan")
    public ResponseEntity<?> deleteLoanDetails(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber) {
        ResponseDTO response = loansService.deleteLoan(mobileNumber);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
