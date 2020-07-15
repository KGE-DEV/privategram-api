package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.biz.CronJobs.SendDailyUpdateEmail;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

public class CronUtilities {
  private final long TWENTY_FOUR_HOURS = 1000L * 60L * 60L * 24L;

  private final SendDailyUpdateEmail sendDailyUpdateEmail;

  public CronUtilities(SendDailyUpdateEmail sendDailyUpdateEmail) {
    this.sendDailyUpdateEmail = sendDailyUpdateEmail;
  }

  public void scheduleDailyEmailUpdate() {
    Timer t = new Timer();
    t.scheduleAtFixedRate(sendDailyUpdateEmail, getTodayAtMidnight(), TWENTY_FOUR_HOURS);
  }

  public Date getTodayAtMidnight() {
    // today
    Calendar date = new GregorianCalendar();
    // reset hour, minutes, seconds and millis
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);

    // next day
    date.add(Calendar.DAY_OF_MONTH, 1);
    return date.getTime();
  }

}