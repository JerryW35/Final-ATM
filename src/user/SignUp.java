/*
 This class is used for signing up a user.
 create by Jerry
 */
package src.user;

import src.Database.Controllers.UserController;
import src.Database.Models.User;

/*
Class to perform sign up for the user
 */
public class SignUp {
    public boolean signUp(User user) {
        UserController userController = new UserController();
        boolean result  = userController.createNewUser(user);
        return result;
    }
}
