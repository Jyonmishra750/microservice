package com.bank.accounts.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDetailsRequestDTO {
    @NotNull(message = "Account id is required.")
    private Long accountId;

    private String accountType;

    private String branchAddress;
}
