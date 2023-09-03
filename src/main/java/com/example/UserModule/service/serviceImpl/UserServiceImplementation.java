package com.example.UserModule.service.serviceImpl;

import com.example.UserModule.entity.UserInsights;
import com.example.UserModule.helper.Constants;
import com.example.UserModule.helper.MessageStrings;
import com.example.UserModule.dto.*;
import com.example.UserModule.entity.AuthenticationToken;
import com.example.UserModule.entity.UserTable;
import com.example.UserModule.exceptions.AuthenticationFailException;
import com.example.UserModule.exceptions.CustomException;
import com.example.UserModule.helper.ResponseMessages;
import com.example.UserModule.repo.TokenRepository;
import com.example.UserModule.repo.UserInsightsRepository;
import com.example.UserModule.repo.UserTableRepository;
import com.example.UserModule.service.serviceImpl.service.GeoIPLocationService;
import com.example.UserModule.service.serviceImpl.service.UserService;

import java.time.LocalDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {

  @Autowired
  UserTableRepository userRepository;
  @Autowired
  TokenRepository tokenRepository;
  @Autowired
  AuthenticationService authenticationService;
  @Autowired
  UserInsightsRepository userInsightsRepository;
  @Autowired
  private JavaMailSender javaMailSender;
  @Value("${spring.mail.username}")
  private String sender;
  //  @Autowired
  //  @Qualifier("fixedThreadPool")
  //  private ExecutorService executorService;
  @Autowired
  GeoIPLocationService geoIPLocationService;


  Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);

  public SignUpResponseDto signUp(SignupDto signupDto) throws CustomException {

    // Check to see if the current email address has already been registered.
    if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
      // If the email address has been registered then throw an exception.
      return new SignUpResponseDto(ResponseMessages.EXISTING_USER, "Try Login");
    }

    // first encrypt the password
    String encryptedPassword = signupDto.getPassword();
    try {
      encryptedPassword = hashPassword(signupDto.getPassword());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      logger.error("hashing password failed {}", e.getMessage());
    }
    UserTable user = new UserTable();
    BeanUtils.copyProperties(signupDto, user, signupDto.getPassword());
    user.setPassword(encryptedPassword);
    try {
      // save the User
      userRepository.save(user);
      // generate token for user
      final AuthenticationToken authenticationToken = new AuthenticationToken(user);
      // save token in database
      authenticationService.saveConfirmationToken(authenticationToken);

      UserInsights userInsights = new UserInsights(user);
      userInsightsRepository.save(userInsights);

      // success in creating
      return new SignUpResponseDto(Constants.SUCCESS, ResponseMessages.USER_CREATED);
    } catch (Exception e) {

      // handle signup error
      throw new CustomException(e.getMessage());
    }
  }

  private String hashPassword(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());
    byte[] digest = md.digest();
    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
    return myHash;
  }

  public SignInResponseDto signIn(SignInDto signInDto) throws AuthenticationFailException, CustomException {
    // first find User by email
    UserTable user = userRepository.findByEmail(signInDto.getEmail());

    if (!Objects.nonNull(user)) {
      return new SignInResponseDto(ResponseMessages.USER_UNAVAILABLE, "Try Sign-UP !");
    }
    try {
      if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
        return new SignInResponseDto(MessageStrings.WRONG_PASSWORD, "Wrong Password");
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      logger.error("hashing password failed {}", e.getMessage());
      throw new CustomException(e.getMessage());
    }
    AuthenticationToken token = authenticationService.getToken(user);
    if (!Objects.nonNull(token)) {
      // token not present
      throw new CustomException(MessageStrings.AUTH_TOKEN_NOT_PRESENT);
    }
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      updateLastLoginTime(user.getId(), new Date());
    });
    executorService.shutdown();
    return new SignInResponseDto(Constants.SUCCESS, token.getToken());
  }

  private void updateLastLoginTime(Integer userId, Date dateTime) {
    userInsightsRepository.updateLastLoginDate(userId, dateTime);
  }

  @Override
  public PasswordResetLinkDto passwordResetLinkFunction(String email) {
    UserTable userDetails = userRepository.findByEmail(email);
    AuthenticationToken authenticationToken = (tokenRepository.findTokenByUser(userDetails));
    String userToken = authenticationToken.getToken();

    EmailDetails emailDetails = new EmailDetails();
    emailDetails.setRecipient(email);
    emailDetails.setSubject("Password Reset Token - CampusConnect");
    emailDetails.setMsgBody("The User Token for Password Reset is : " + userToken);
    PasswordResetLinkDto passwordResetLinkDto = new PasswordResetLinkDto();
    passwordResetLinkDto.setResponseMessage("Authentication Token Sent Successfully");
    int attemptsLeft = userInsightsRepository.getPasswordResetAttemptsById(userDetails.getId());

    if (attemptsLeft <= 0) {
      passwordResetLinkDto.setAttemptsLeft(0);
      passwordResetLinkDto.setResponseMessage(ResponseMessages.ATTEMPTS_EXHAUSTED + " " + LocalDateTime.now());
    } else {
      // Create an ExecutorService to run the email sending task asynchronously
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.submit(() -> {
        // Send the email in a separate thread
        sendSimpleMail(emailDetails);
      });
      executorService.shutdown(); // Shutdown the executor when done

      // Reduce password attempts in the main thread
      userInsightsRepository.reducePasswordAttempts(userDetails.getId());
    }

    passwordResetLinkDto.setAttemptsLeft(attemptsLeft);
    return passwordResetLinkDto;
  }

  @Override
  public PasswordChangeResponseDto passwordChangeFunction(PasswordChangeDto passwordChangeDto) {
    PasswordChangeResponseDto passwordChangeResponseDto = new PasswordChangeResponseDto();
    try {
      UserTable user = authenticationService.getUser(passwordChangeDto.getUniqueTokenId());
      String hashedPassword = hashPassword(passwordChangeDto.getNewPassword());
      userRepository.updatePassword(user.getId(), hashedPassword);
      passwordChangeResponseDto.setToken(passwordChangeDto.getUniqueTokenId());
      passwordChangeResponseDto.setResponse("Password Changed Successfully !");
      passwordChangeResponseDto.setUpdate(true);
    } catch (Exception e) {
      log.warn("Authentication Failed for User with Token : {} TimeStamp : {}", passwordChangeDto.getUniqueTokenId(),
          new Date());
      passwordChangeResponseDto.setResponse(ResponseMessages.AUTHENTICATION_ERROR);
      passwordChangeResponseDto.setToken(passwordChangeDto.getUniqueTokenId());
      passwordChangeResponseDto.setUpdate(false);
    }
    return passwordChangeResponseDto;
  }


  @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Kolkata")
  public void resetAttempts() {
    List<UserTable> users = userRepository.findAll();
    List<Integer> ids = users.stream().map(UserTable::getId).collect(Collectors.toList());
    userInsightsRepository.updatePasswordResetAttempts(ids, Constants.PASSWORD_ATTEMPTS);
  }

  private String sendSimpleMail(EmailDetails details) {

    try {
      // Creating a simple mail message
      SimpleMailMessage mailMessage = new SimpleMailMessage();

      // Setting up necessary details
      mailMessage.setFrom(sender);
      mailMessage.setTo(details.getRecipient());
      mailMessage.setText(details.getMsgBody());
      mailMessage.setSubject(details.getSubject());

      // Sending the mail
      javaMailSender.send(mailMessage);
      return ResponseMessages.MAIL_SENT_SUCCESS;
    }

    // Catch block to handle the exceptions
    catch (Exception e) {
      return ResponseMessages.MAIL_SENT_FAILURE;
    }
  }
}