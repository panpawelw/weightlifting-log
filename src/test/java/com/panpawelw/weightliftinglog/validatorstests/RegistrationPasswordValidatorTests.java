package com.panpawelw.weightliftinglog.validatorstests;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.validators.RegistrationPasswordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationPasswordValidatorTests {

  private static final User TEST_USER = new User(1L, "Test username",
      "Test password", "Test password", "test@email.com", true);

  private RegistrationPasswordValidator validator;

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
  public void userIsValid() {
    BindException errors = new BindException(TEST_USER, "user");
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertFalse(errors.hasErrors());
  }
}
