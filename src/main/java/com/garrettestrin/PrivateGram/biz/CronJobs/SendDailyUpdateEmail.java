package com.garrettestrin.PrivateGram.biz.CronJobs;

import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import com.garrettestrin.PrivateGram.biz.BizUtilities;
import com.garrettestrin.PrivateGram.biz.EventService;
import com.garrettestrin.PrivateGram.data.DataObjects.Event;
import com.garrettestrin.PrivateGram.data.EventDao;
import com.garrettestrin.PrivateGram.data.UserDao;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class SendDailyUpdateEmail extends TimerTask {

  public final BizUtilities bizUtilities;
  public final UserDao userDao;
  public final EventDao eventDao;
  public final EventService eventService;

  public SendDailyUpdateEmail(BizUtilities bizUtilities, UserDao userDao, EventDao eventDao, EventService eventService) {

    this.bizUtilities = bizUtilities;
    this.userDao = userDao;
    this.eventDao = eventDao;
    this.eventService = eventService;
  }

  @Override
  public void run() {
    processUpdateEmails();
  }

  private void processUpdateEmails() {
    // get list of users
    List<User> usersToSendUpdatesToo = filterUsersThatNeedAnUpdate(userDao.getSubscribedUsers());
    usersToSendUpdatesToo.forEach((user) -> {
      boolean wasUpdateSent = bizUtilities.sendEmail(user.getEmail(), "ElsieGram Update", "Here is what you missed on ElsieGram!");
      if(wasUpdateSent) {
        com.garrettestrin.PrivateGram.api.ApiObjects.Event emailEvent = new com.garrettestrin.PrivateGram.api.ApiObjects.Event().builder()
                .event("Update Email").userId((int)user.getId()).page("").build();
        eventService.saveEvent(emailEvent);
      }
    });
  }

  private List<User> filterUsersThatNeedAnUpdate(List<User> allUsers) {
    List<User> filteredUsers = new ArrayList<>();
    allUsers.forEach((user) -> {
      try {
        if(doesUserNeedAnUpdate(user.getId())) {
          filteredUsers.add(user);
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    });
    return filteredUsers;
  }

  private boolean doesUserNeedAnUpdate(long id) throws ParseException {
    Event lastEvent = eventDao.getUsersLastEvent(id);
    lastEvent.getDate_time();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
    Date lastEventDate = dateFormat.parse(lastEvent.getDate_time());
    long DAY_IN_MS = 1000 * 60 * 60 * 24;
    Date threeDaysAgo = new Date(System.currentTimeMillis() - (3 * DAY_IN_MS));
    return lastEventDate.before(threeDaysAgo);
  }
}
