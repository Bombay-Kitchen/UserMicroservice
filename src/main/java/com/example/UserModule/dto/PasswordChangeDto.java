package com.example.UserModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
  String newPassword;
  String conformPassword;
  String uniqueTokenId;
}
