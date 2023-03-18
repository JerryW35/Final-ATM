package src.Database.Controllers.NotificationsObserver;

import src.Database.Models.Notification;

import java.util.ArrayList;

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

public class UserNotificationObserver implements UserNotificationInterface{
    long userId;
    ArrayList<Notification> userNotifications = new ArrayList<>();

    @Override
    public void update(long userId, ArrayList<Notification> notifications) {
        this.userId = userId;
        this.userNotifications = notifications;
    }

    public ArrayList<Notification> getUserNotifications() {
        return userNotifications;
    }
}
