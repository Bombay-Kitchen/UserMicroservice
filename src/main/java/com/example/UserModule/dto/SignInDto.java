package com.example.UserModule.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {
  private String email;
  private String password;

}
