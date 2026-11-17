package com.bank.accounts.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsCreationRequestDTO {
    @NotBlank(message = "Please provide account type.")
    private String accountType;

    @NotBlank(message = "Please provide branch address.")
    private String branchAddress;
}
