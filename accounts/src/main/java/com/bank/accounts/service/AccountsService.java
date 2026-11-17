package com.bank.accounts.service;

import com.bank.accounts.dto.request.AccountsCreationRequestDTO;
import com.bank.accounts.dto.request.UpdateAccountDetailsRequestDTO;
import com.bank.accounts.dto.response.ResponseDTO;

public interface AccountsService {
    ResponseDTO createAccount(AccountsCreationRequestDTO account);
    ResponseDTO getAccountDetails(Long accountId);
    ResponseDTO updateAccountDetails(UpdateAccountDetailsRequestDTO updateAccountDetailsRequestDTO);
    ResponseDTO deleteAccount(Long accountId);
}
