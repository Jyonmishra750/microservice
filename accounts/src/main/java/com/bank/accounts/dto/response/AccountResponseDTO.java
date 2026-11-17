package com.bank.accounts.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private Long accountNumber;
    private String accountType;
    private String branchAddress;
    private AuditResponse auditResponse;
}
