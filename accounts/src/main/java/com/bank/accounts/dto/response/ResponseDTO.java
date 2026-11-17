package com.bank.accounts.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private Integer statusCode;
    private Object data;
    private String statusMsg;
}
