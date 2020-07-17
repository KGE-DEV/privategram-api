package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.biz.CronJobs.SendDailyUpdateEmail;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class CronUtilities {
  private final long TWENTY_FOUR_HOURS = 1000L * 60L * 60L * 24L;

  private final SendDailyUpdateEmail sendDailyUpdateEmail;

  public CronUtilities(SendDailyUpdateEmail sendDailyUpdateEmail) {
    this.sendDailyUpdateEmail = sendDailyUpdateEmail;
  }

  public void scheduleDailyEmailUpdate() {
    log.infof("ScheduleDailyEmailUpdate Cron job set to run at: " + getTodayAtMidnight());
    Timer t = new Timer();
    t.scheduleAtFixedRate(sendDailyUpdateEmail, getTodayAtMidnight(), TWENTY_FOUR_HOURS);
  }

  public Date getTodayAtMidnight() {
    // today
    Calendar date = new GregorianCalendar();
    // reset minutes, seconds and millis
    // 7am UTC, 12am PST
    date.set(Calendar.HOUR_OF_DAY, 7);
    // reset minutes, seconds and millis
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    return date.getTime();
  }

}