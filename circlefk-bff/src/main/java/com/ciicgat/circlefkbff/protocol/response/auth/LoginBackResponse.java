package com.ciicgat.circlefkbff.protocol.response.auth;

import lombok.Data;

@Data
public class LoginBackResponse {
    private String access_token;
    private Long expires_in;
    private String state;
    private String token_type;
}
