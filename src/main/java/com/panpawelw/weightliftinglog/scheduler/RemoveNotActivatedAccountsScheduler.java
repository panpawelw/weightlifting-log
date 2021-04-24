package com.panpawelw.weightliftinglog.scheduler;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RemoveNotActivatedAccountsScheduler {

  private final UserService uService;
  private final VerificationTokenService vtService;

  @Autowired
  public RemoveNotActivatedAccountsScheduler(
      UserService uService, VerificationTokenService vtService) {
    this.uService = uService;
    this.vtService = vtService;
  }

  @Scheduled(fixedDelay = 90000000)
  public void removeNotActivatedAccounts() {
    for(User user: uService.findAllByActivated(false)) {
      vtService.removeAccountIfTokenExpired(user);
    }
  }
}
