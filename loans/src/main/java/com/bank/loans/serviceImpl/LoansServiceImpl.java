package com.bank.loans.serviceImpl;

import com.bank.loans.dto.request.UpdateLoanDetailsRequestDTO;
import com.bank.loans.dto.response.AuditResponse;
import com.bank.loans.dto.response.LoansResponseDTO;
import com.bank.loans.dto.response.ResponseDTO;
import com.bank.loans.entity.Loans;
import com.bank.loans.repository.LoansRepository;
import com.bank.loans.service.LoansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LoansServiceImpl implements LoansService {

    @Autowired
    private LoansRepository loansRepository;

    private static final int NEW_LOAN_LIMIT = 100_000;
    private static final String HOME_LOAN = "Home Loan";

    @Override
    @Transactional
    public ResponseDTO createLoan(String mobileNumber) {
        Optional<Loans> existingLoan = loansRepository.findByMobileNumber(mobileNumber);
        if (existingLoan.isPresent()) {
            return new ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Loan already registered with given mobileNumber: " + mobileNumber,
                    HttpStatus.BAD_REQUEST.toString()
            );
        }

        Loans newLoan = createNewLoan(mobileNumber);
        Loans savedLoan = loansRepository.save(newLoan);
        LoansResponseDTO responseData = mapToLoansResponseDTO(savedLoan);

        return new ResponseDTO(
                HttpStatus.CREATED.value(),
                responseData,
                "Loan created successfully"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO fetchLoan(String mobileNumber) {
        Optional<Loans> loanOptional = loansRepository.findByMobileNumber(mobileNumber);
        if (loanOptional.isEmpty()) {
            return new ResponseDTO(
                    HttpStatus.NOT_FOUND.value(),
                    "Loan not found for mobileNumber: " + mobileNumber,
                    HttpStatus.NOT_FOUND.toString()
            );
        }

        LoansResponseDTO responseData = mapToLoansResponseDTO(loanOptional.get());
        return new ResponseDTO(
                HttpStatus.OK.value(),
                responseData,
                "Loan fetched successfully"
        );
    }

    @Override
    @Transactional
    public ResponseDTO updateLoan(UpdateLoanDetailsRequestDTO updateLoanDetailsRequestDTO) {
        Optional<Loans> loanOptional = loansRepository.findByLoanNumber(updateLoanDetailsRequestDTO.getLoanNumber());
        if (loanOptional.isEmpty()) {
            return new ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Loan not found for loanNumber: " + updateLoanDetailsRequestDTO.getLoanNumber(),
                    HttpStatus.BAD_REQUEST.toString()
            );
        }

        Loans loan = loanOptional.get();
        mapToLoans(updateLoanDetailsRequestDTO, loan);

        Loans updatedLoan = loansRepository.save(loan);

        LoansResponseDTO responseData = mapToLoansResponseDTO(updatedLoan);

        return new ResponseDTO(
                HttpStatus.OK.value(),
                responseData,
                HttpStatus.OK.toString()
        );
    }

    @Override
    @Transactional
    public ResponseDTO deleteLoan(String mobileNumber) {
        Optional<Loans> loanOptional = loansRepository.findByMobileNumber(mobileNumber);
        if (loanOptional.isEmpty()) {
            return new ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Loan not found for mobileNumber: " + mobileNumber,
                    HttpStatus.BAD_REQUEST.toString()
            );
        }

        loansRepository.delete(loanOptional.get());
        return new ResponseDTO(
                HttpStatus.OK.value(),
                "Loan deleted successfully.",
                HttpStatus.OK.toString()
        );
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100_000_000_000L + ThreadLocalRandom.current().nextLong(900_000_000_000L);
        newLoan.setLoanNumber(String.valueOf(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(HOME_LOAN);
        newLoan.setTotalLoan(NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(NEW_LOAN_LIMIT);
        return newLoan;
    }

    private LoansResponseDTO mapToLoansResponseDTO(Loans loans) {
        AuditResponse auditResponse = new AuditResponse(
                loans.getCreatedAt() != null ? loans.getCreatedAt().atStartOfDay() : null,
                loans.getCreatedBy(),
                loans.getUpdatedAt() != null ? loans.getUpdatedAt().atStartOfDay() : null,
                loans.getUpdatedBy()
        );

        return new LoansResponseDTO(
                loans.getMobileNumber(),
                loans.getLoanNumber(),
                loans.getLoanType(),
                loans.getTotalLoan(),
                loans.getAmountPaid(),
                loans.getOutstandingAmount(),
                auditResponse
        );
    }

    public static void mapToLoans(UpdateLoanDetailsRequestDTO dto, Loans loans) {
        if (dto.getMobileNumber() != null && !dto.getMobileNumber().isBlank()) {
            loans.setMobileNumber(dto.getMobileNumber());
        }
        if (dto.getLoanType() != null && !dto.getLoanType().isBlank()) {
            loans.setLoanType(dto.getLoanType());
        }
        if (dto.getTotalLoan() != null) {
            loans.setTotalLoan(dto.getTotalLoan());
        }
        if (dto.getAmountPaid() != null) {
            loans.setAmountPaid(dto.getAmountPaid());
        }
        if (dto.getOutstandingAmount() != null) {
            loans.setOutstandingAmount(dto.getOutstandingAmount());
        } else if (dto.getTotalLoan() != null || dto.getAmountPaid() != null) {
            loans.setOutstandingAmount(loans.getTotalLoan() - loans.getAmountPaid());
        }
    }
}
