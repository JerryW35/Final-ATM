/*
the class for creating account
create by Jerry
 */
package src.user;

import src.Database.Models.Account;
import src.Database.Models.User;
import src.account.AccountFactory;

import java.util.Scanner;

/*
Create account middleware to allow UI to connect with specific account creation functions
 */
public class CreateAccount {
    AccountFactory factory =new AccountFactory();
    public void createAccount(User user){
        Account account = new Account();
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("Please select the account type you want to create:");
            System.out.println("1. saving account   2. checking account  3.security account");

            String input = sc.nextLine();

            if(input.equals("1")){
                if(checkIfExist(user,"saving")) continue;
                //facotory.createAccount();
            }else if (input.equals("2")){
                if(checkIfExist(user,"checking")) continue;
                //facotory.createAccount();
            }else if(input.equals("3")){
                if(checkIfExist(user,"security")) continue;
                //facotory.createAccount();
            }

        }
    }
    private boolean checkIfExist(User user,String targetAccount){
        //check if user already has the type of account

        return false;
    }
}
