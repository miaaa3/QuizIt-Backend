package com.estsb.QuizIT.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private String email;
    private String username;
    private String role;
    private String password;
}
