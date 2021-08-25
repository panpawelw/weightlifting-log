package com.panpawelw.weightliftinglog.validatorstests;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.validators.RegistrationPasswordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationPasswordValidatorTests {

  private static final User TEST_USER = new User(1L, "Test username",
      "Test password", "Test password", "test@email.com", true);

  private RegistrationPasswordValidator validator;

  BindException errors = new BindException(TEST_USER, "user");

  @Before
  public void setup() {
    validator = new RegistrationPasswordValidator();
  }

  @Test
  public void supports() {
    assertTrue(validator.supports(User.class));
    assertFalse(validator.supports(Object.class));
  }

  @Test
  public void passwordIsValid() {
    TEST_USER.setPassword("Test password");
    TEST_USER.setConfirmPassword("Test password");
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertFalse(errors.hasErrors());
  }

  @Test
  public void passwordIsBlank() {
    TEST_USER.setPassword("");
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals("NotBlank.password", errors.getFieldErrors("password").get(0).getCode());
  }

  @Test
  public void passwordHasWrongLength() {
    TEST_USER.setPassword("xx");
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals("Size.password", errors.getFieldErrors("password").get(0).getCode());
  }

  @Test
  public void passwordAndConfirmationDontMatch() {
    TEST_USER.setPassword("Test password");
    TEST_USER.setConfirmPassword("Not password");
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals("Diff.confirmPassword",
        errors.getFieldErrors("confirmPassword").get(0).getCode());
  }
}
