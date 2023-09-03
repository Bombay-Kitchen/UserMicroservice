package com.example.UserModule.repo;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.UserModule.entity.UserInsights;


@Repository
public interface UserInsightsRepository extends JpaRepository<UserInsights, Integer> {

  @Query("SELECT ui.passwordResetAttempts FROM UserInsights ui WHERE ui.id = :id")
  int getPasswordResetAttemptsById(@Param("id") Integer id);


  /**
   * Query to Update the Last Login Date for the User
   *
   * @param id
   * @param lastLoginDate
   */
  @Modifying
  @Transactional
  @Query("UPDATE UserInsights ui SET ui.lastLoginDate = :lastLoginDate WHERE ui.id = :id")
  void updateLastLoginDate(@Param("id") Integer id, @Param("lastLoginDate") Date lastLoginDate);


  /**
   * Query to Update Password Reset Attempt
   *
   * @param ids
   * @param newAttempts
   */
  @Modifying
  @Transactional
  @Query("UPDATE UserInsights ui SET ui.passwordResetAttempts = :passwordResetAttempts WHERE ui.id IN :id")
  void updatePasswordResetAttempts(@Param("id") List<Integer> ids, @Param("passwordResetAttempts") int newAttempts);

  /**
   * Query to Reduce the Attempt of Password Attempt Left
   *
   * @param id
   */
  @Modifying
  @Transactional
  @Query("UPDATE UserInsights ui SET ui.passwordResetAttempts = ui.passwordResetAttempts - 1 WHERE ui.id = :id")
  void reducePasswordAttempts(@Param("id") Integer id);
}