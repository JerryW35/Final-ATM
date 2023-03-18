package src.Database.Controllers.NotificationsObserver;

import src.Database.Models.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name

Notifications follow an observer pattern
Each user is registered as an observer when they are pulled from the database with an array of notifications
Whenver a manager creates a notification then the NotificationManager sends an update to all UserNotificationObserver objects stating that they
need to update thier notifications as new notifications have been sent
 */

public class NotificationManager {
    HashMap<Long, UserNotificationObserver> notificationsHashMap = new HashMap<>();

    public void addObserver(UserNotificationObserver userNotificationObserver) {
        System.out.println("Created new Observer for "+userNotificationObserver.userId);
        this.notificationsHashMap.put(userNotificationObserver.userId, userNotificationObserver);
    }

    public void removeObserver(UserNotificationObserver userNotificationObserver) {
        this.notificationsHashMap.remove(userNotificationObserver.userId);
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        notificationsHashMap.forEach((aLong, userNotificationObserver) -> {
            userNotificationObserver.update(aLong, new ArrayList<>(notifications.stream().filter(notification -> notification.getUserId()==aLong).collect(Collectors.toList())));
        });
    }

    public UserNotificationObserver getNotificationsOfUser(long userId){
        return notificationsHashMap.get(userId);
    }

    public boolean checkIfUserObserverPresent(long userId){
        return notificationsHashMap.containsKey(userId);
    }
}
