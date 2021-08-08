package com.panpawelw.weightliftinglog.controllers.misc;

import org.springframework.ui.Model;

import java.util.HashMap;

public class Message {
  /**
   * Stores message that will be displayed on HTML page in Thymeleaf form as model attributes.
   * @param model   - model
   * @param address - address the application will redirect to after displaying the message
   * @param header  - header of the message
   * @param text    - body of the message
   */
  public static void prepMessage(Model model, String address, String header, String text) {
    model.addAllAttributes(new HashMap<String, String>() {{
      put("page", "fragments.html :: show-message");
      put("header", header);
      put("text", text);
      put("address", address);
    }});
  }
}
