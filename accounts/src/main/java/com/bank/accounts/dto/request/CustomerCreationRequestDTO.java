package com.bank.accounts.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreationRequestDTO {
    @NotBlank(message = "Please provide the customer name.")
    private String name;

    @NotBlank(message = "Please provide customer email.")
    private String email;

    @NotBlank(message = "Please provide customer mobile number.")
    private String mobileNumber;

    @NotNull(message = "Please provide the account id.")
    private Long accountId;
}
