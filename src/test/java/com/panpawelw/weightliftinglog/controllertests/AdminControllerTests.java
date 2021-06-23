package com.panpawelw.weightliftinglog.controllertests;

import com.panpawelw.weightliftinglog.controllers.AdminController;
import com.panpawelw.weightliftinglog.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTests {

  @Mock
  private UserService service;

  @Mock
  private Model model;

  private AdminController controller;

  @Before
  public void setup() {
    controller = new AdminController(service);
  }

  @Test
  public void testAdmin() {
    assertEquals(controller.admin(model), "home");
  }
}
