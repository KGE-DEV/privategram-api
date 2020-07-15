package com.garrettestrin.PrivateGram.biz.CronJobs;

import com.garrettestrin.PrivateGram.biz.BizUtilities;
import java.util.TimerTask;

public class SendDailyUpdateEmail extends TimerTask {

  public final BizUtilities bizUtilities;

  public SendDailyUpdateEmail(BizUtilities bizUtilities) {
    this.bizUtilities = bizUtilities;
  }

  @Override
  public void run() {
    bizUtilities.sendTestEmail();
  }
}
