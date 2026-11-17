package com.bank.accounts.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private Long customerId;
    private Long accountId;
    private Long accountNumber;
    private String name;
    private String email;
    private String mobileNumber;
    private AuditResponse auditResponse;
}
