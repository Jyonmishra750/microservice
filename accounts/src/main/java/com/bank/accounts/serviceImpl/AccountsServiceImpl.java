package com.bank.accounts.serviceImpl;

import com.bank.accounts.dto.request.AccountsCreationRequestDTO;
import com.bank.accounts.dto.request.UpdateAccountDetailsRequestDTO;
import com.bank.accounts.dto.response.AccountResponseDTO;
import com.bank.accounts.dto.response.AuditResponse;
import com.bank.accounts.dto.response.ResponseDTO;
import com.bank.accounts.entity.Accounts;
import com.bank.accounts.repository.AccountsRepository;
import com.bank.accounts.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    @Transactional
    public ResponseDTO createAccount(AccountsCreationRequestDTO account) {
        try {
            if (account == null
                    || account.getBranchAddress() == null || account.getBranchAddress().isBlank()
                    || account.getAccountType() == null || account.getAccountType().isBlank()) {
                return new ResponseDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        "Branch address and account type are required.",
                        HttpStatus.BAD_REQUEST.name()
                );
            }

            String branchAddress = account.getBranchAddress().trim();
            String accountType = account.getAccountType().trim();

            boolean branchExists = accountsRepository.existsByBranchAddressIgnoreCase(branchAddress);

            if (branchExists) {
                boolean accountTypeExistsInBranch = accountsRepository
                        .existsByBranchAddressIgnoreCaseAndAccountTypeIgnoreCase(branchAddress, accountType);

                if (accountTypeExistsInBranch) {
                    return new ResponseDTO(
                            HttpStatus.CONFLICT.value(),
                            "Account type '" + accountType + "' already exists for branch: " + branchAddress,
                            HttpStatus.CONFLICT.name()
                    );
                }
            }

            // Creates the account (whether the branch is brand new or already exists without this account type)
            Accounts newAccount = new Accounts();
            newAccount.setBranchAddress(branchAddress);
            newAccount.setAccountType(accountType);

            // Required because Audit has nullable = false (until @EnableJpaAuditing is configured)
            /*newAccount.setCreatedAt(LocalDateTime.now());
            newAccount.setCreatedBy("SYSTEM");*/

            Accounts savedAccount = accountsRepository.save(newAccount);

            String successMessage = branchExists
                    ? "New account type '" + accountType + "' added to existing branch. Account Number: " + savedAccount.getAccountNumber()
                    : "New branch created and account '" + accountType + "' added. Account Number: " + savedAccount.getAccountNumber();

            return new ResponseDTO(
                    HttpStatus.CREATED.value(),
                    savedAccount,
                    successMessage
            );

        } catch (Exception e) {
            return new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getAccountDetails(Long accountId) {
        try {
            if (accountId != null) {
                // Fetch single account
                Optional<Accounts> accountOpt = accountsRepository.findById(accountId);
                if (accountOpt.isEmpty()) {
                    return new ResponseDTO(HttpStatus.NOT_FOUND.value(),  "Account not found with ID: " + accountId, HttpStatus.NOT_FOUND.toString());
                }

                AccountResponseDTO dto = mapToAccountResponseDTO(accountOpt.get());
                return new ResponseDTO(HttpStatus.OK.value(), dto, "Account details fetched successfully");

            } else {
                // Fetch all accounts
                List<Accounts> accountsList = accountsRepository.findAll();
                List<AccountResponseDTO> dtoList = accountsList.stream()
                        .map(this::mapToAccountResponseDTO)
                        .collect(Collectors.toList());

                return new ResponseDTO(HttpStatus.OK.value(), dtoList, "All accounts fetched successfully");
            }
        } catch (Exception e) {
            return new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString()
            );
        }
    }

    @Override
    @Transactional
    public ResponseDTO updateAccountDetails(UpdateAccountDetailsRequestDTO requestDTO) {
        try {
            if (requestDTO == null || requestDTO.getAccountId() == null) {
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Account id is required for update.", HttpStatus.BAD_REQUEST.toString());
            }

            Optional<Accounts> accountOpt = accountsRepository.findById(requestDTO.getAccountId());
            if (accountOpt.isEmpty()) {
                return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Account not found.", HttpStatus.NOT_FOUND.toString());
            }

            Accounts existingAccount = accountOpt.get();

            // Update fields if they are provided in the request
            if (requestDTO.getAccountType() != null && !requestDTO.getAccountType().isBlank()) {
                existingAccount.setAccountType(requestDTO.getAccountType());
            }
            if (requestDTO.getBranchAddress() != null && !requestDTO.getBranchAddress().isBlank()) {
                existingAccount.setBranchAddress(requestDTO.getBranchAddress());
            }

            /*existingAccount.setUpdatedAt(LocalDateTime.now());
            existingAccount.setUpdatedBy("SYSTEM");*/

            Accounts updatedAccount = accountsRepository.save(existingAccount);
            return new ResponseDTO(HttpStatus.OK.value(), updatedAccount, "Account updated successfully");
        } catch (Exception e) {
            return new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString()
            );
        }
    }

    @Override
    @Transactional
    public ResponseDTO deleteAccount(Long accountId) {
        try {
            if (!accountsRepository.existsById(accountId)) {
                return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Account not found with ID: " + accountId, HttpStatus.NOT_FOUND.toString());
            }

            accountsRepository.deleteById(accountId);
            return new ResponseDTO(HttpStatus.OK.value(), "Account deleted successfully", HttpStatus.OK.toString());
        } catch (Exception e) {
            return new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString()
            );
        }
    }

    private AccountResponseDTO mapToAccountResponseDTO(Accounts account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBranchAddress(account.getBranchAddress());

        // Map Audit fields
        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setCreatedAt(account.getCreatedAt());
        auditResponse.setCreatedBy(account.getCreatedBy());
        auditResponse.setUpdatedAt(account.getUpdatedAt());
        auditResponse.setUpdatedBy(account.getUpdatedBy());

        dto.setAuditResponse(auditResponse);

        return dto;
    }
}
