package src.Database.Controllers;

import src.Database.Controllers.NotificationsObserver.NotificationManager;
import src.Database.Controllers.NotificationsObserver.UserNotificationObserver;
import src.Database.Models.Notification;
import src.Helpers.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class NotificationController {
    SQLConnection sqlConnection = new SQLConnection();
    HashMap<Long, ArrayList<Notification>> notifications = new HashMap<>();

    ArrayList<Notification> notificationsArray = new ArrayList<>();

    NotificationManager notificationManager = new NotificationManager();

    public boolean createNewNotification(long userId, String message){
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `notification`(user_id, message) VALUE (?,?)");
            pstmt.setLong(1, userId);
            pstmt.setString(2, message);

            int returnValue = pstmt.executeUpdate();

            if (returnValue > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void updateNotificationRead(long userId){
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE `notification` SET `read`=? WHERE user_id=?");
            pstmt.setBoolean(1, true);
            pstmt.setLong(2, userId);

            int returnValue = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAllNotifications(){
        notifications.clear();
        notificationsArray.clear();

        String sql = "select * from `notification` WHERE `read`=?";
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setBoolean(1, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet rs = null;
        try {
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Condition check
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                long userId = rs.getLong("user_id");
                if(notifications.containsKey(userId)){
                    Notification notification = new Notification();
                    notification.setId(rs.getInt("id"));
                    notification.setMessage(rs.getString("message"));
                    notification.setUserId(rs.getLong("user_id"));
                    notificationsArray.add(notification);
                    notifications.get(userId).add(notification);
                }else{
                    ArrayList<Notification> notifications1 = new ArrayList<>();
                    Notification notification = new Notification();
                    notification.setId(rs.getInt("id"));
                    notification.setMessage(rs.getString("message"));
                    notification.setUserId(rs.getLong("user_id"));
                    notificationsArray.add(notification);
                    notifications1.add(notification);
                    notifications.put(notification.getUserId(), notifications1);
                }
                if(!notificationManager.checkIfUserObserverPresent(userId)){
                    UserNotificationObserver userNotificationObserver = new UserNotificationObserver();
                    userNotificationObserver.update(userId, notifications.get(userId));
                    notificationManager.addObserver(userNotificationObserver);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        notificationManager.setNotifications(notificationsArray);
    }

    public ArrayList<Notification> getUserNotifications(long userId){
        getAllNotifications();
        if(notificationManager.checkIfUserObserverPresent(userId)) {
            UserNotificationObserver userNotificationObserver = notificationManager.getNotificationsOfUser(userId);
            return userNotificationObserver.getUserNotifications();
        }
        else{
            return null;
        }
    }

    public void createUserNotification(String message, long userId){
        createNewNotification(userId, message);
    }
}
