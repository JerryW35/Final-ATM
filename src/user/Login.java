/*
    the class is used for login
    create by Jerry
 */

package src.user;

import src.Database.Controllers.UserController;
import src.Database.Models.User;

/*
Class to perform login operations and act as a middleman for the user and the db
 */
public class Login {
    public UserView login(String userName, String password){
        UserController control = new UserController();
        //TODO: check if the user is in DB
        if(control.checkIfUserExists(userName)){
            // you can only input password 3 times
//            Scanner sc = new Scanner(System.in);
//            System.out.println("Please input your password:");
//            int cnt=0;
//            while(!sc.nextLine().equals(getPassword(user))){
//                cnt+=1;
//                if(cnt==3) return false;
//                System.out.println("Your input password is error!");
//                System.out.println("Please input your password again:");
//            }
            System.out.println(userName + " " + password);
            if(control.getUserByUserName(userName).getPassword().equals(password)) {
                UserView user = copyProperties(control.getUserByUserName(userName));
                LoggedInUser.setUser(user);
                return user;
            }
        }
       return null;
    }


    private UserView copyProperties(User user){
        UserView userView = new UserView();
        userView.setCompany(user.getCompany());
        userView.setEmail(user.getEmail());
        userView.setContactNumber(user.getContactNumber());
        userView.setManager(user.getManager());
        userView.setFirstName(user.getFirstName());
        userView.setPassword(user.getPassword());
        userView.setLastName(user.getLastName());
        userView.setDateOfBirth(user.getDateOfBirth());
        userView.setUserId(user.getId());

        return userView;
    }

//    public String getPassword(User user){
//        // TODO: the password need to be accessed by DB rather than User class
//        // get password from DB by searching user.username or anything else
//        return null;
//    }
}
