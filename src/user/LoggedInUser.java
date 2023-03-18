package src.user;

/*
Singleton class storing the current logged in user to allow the ATM to be only used by one user at a time
 */
public class LoggedInUser {
    static UserView user;

    public static UserView getUser() {
        return user;
    }

    public static void setUser(UserView user) {
        LoggedInUser.user = user;
    }
}
