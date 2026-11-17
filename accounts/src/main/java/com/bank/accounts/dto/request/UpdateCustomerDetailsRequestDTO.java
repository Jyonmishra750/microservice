package com.bank.accounts.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerDetailsRequestDTO {

    @NotNull(message = "Customer ID is required.")
    private Long customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private Long accountId;
}