package com.example.UserModule.service.serviceImpl.service;

import com.example.UserModule.dto.EmailDetails;
import com.example.UserModule.dto.PasswordChangeDto;
import com.example.UserModule.dto.PasswordChangeResponseDto;
import com.example.UserModule.dto.PasswordResetLinkDto;
import com.example.UserModule.dto.SignInDto;
import com.example.UserModule.dto.SignInResponseDto;
import com.example.UserModule.dto.SignUpResponseDto;
import com.example.UserModule.dto.SignupDto;
import com.example.UserModule.exceptions.AuthenticationFailException;
import com.example.UserModule.exceptions.CustomException;

public interface UserService {
  /**
   * @param signupDto
   * @return
   * @throws CustomException
   */
  public SignUpResponseDto signUp(SignupDto signupDto) throws CustomException;

  /**
   * @param signInDto
   * @return
   * @throws AuthenticationFailException
   * @throws CustomException
   */
  public SignInResponseDto signIn(SignInDto signInDto) throws AuthenticationFailException, CustomException;

  /**
   * @param email
   * @return
   */
  public PasswordResetLinkDto passwordResetLinkFunction(String email);

  /**
   * @param passwordChangeDto
   * @return
   */
  public PasswordChangeResponseDto passwordChangeFunction(PasswordChangeDto passwordChangeDto);

}
