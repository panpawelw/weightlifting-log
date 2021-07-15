package com.panpawelw.weightliftinglog.misc;

import org.springframework.ui.Model;

import java.util.HashMap;

public class Message {
  public static void prepMessage(Model model, String header, String text, String button, String address) {
    model.addAllAttributes(new HashMap<String, String>() {{
      put("page", "fragments.html :: show-message");
      put("header", header);
      put("text", text);
      put("button", button);
      put("address", address);
    }});
  }
}
