package com.panpawelw.weightliftinglog.controllertests;

import com.panpawelw.weightliftinglog.controllers.ChangePasswordController;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.validators.ChangePasswordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ChangePasswordControllerTests {

  @Mock
  private UserService service;

  @Mock
  private ChangePasswordValidator validator;

  @Mock
  private Model model;

  private ChangePasswordController controller;

  @Before
  public void setup() {
    this.controller = new ChangePasswordController(validator, service);
  }

  @Test
  public void testChangePasswordGet() {
    assertEquals(controller.changePasswordGet(model), "home");
  }
}
