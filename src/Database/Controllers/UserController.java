package src.Database.Controllers;

import src.Database.Models.Account;
import src.Database.Models.User;
import src.Helpers.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class UserController {

    SQLConnection sqlConnection = new SQLConnection();

    HashMap<String, User> users = new HashMap<>();

    HashMap<Long, User> usersById = new HashMap<>();
    public boolean createNewUser(User user){
            sqlConnection.makeConnection();
            Connection connection = sqlConnection.getConnection();
            System.out.println(user.toString());
            if(!checkIfUserExists(user.getUserName())) {
                try {
                    Timestamp createdAt = new Timestamp(System.currentTimeMillis());
                    Timestamp updateAt = createdAt;
                    PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `user`(first_name,last_name,email, contact_number,password, company, user_name, date_of_birth, created_at, updated_at, manager, social_security_number) VALUE (?,?,?,?,?,?,?,?,?,?,?,?)");
                    pstmt.setString(1, user.getFirstName());
                    pstmt.setString(2, user.getLastName());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getContactNumber());
                    pstmt.setString(5, user.getPassword());
                    pstmt.setBoolean(6, user.getCompany());
                    pstmt.setString(7, user.getUserName());
                    pstmt.setDate(8, user.getDateOfBirth());
                    pstmt.setTimestamp(9, createdAt);
                    pstmt.setTimestamp(10, updateAt);
                    pstmt.setBoolean(11, user.getManager());
                    pstmt.setString(12, user.getSocialSecurity());

                    int returnValue = pstmt.executeUpdate();

                    if (returnValue > 0) {
                        User createdUser = getUserByUserName(user.getUserName());

                        PreparedStatement prepareAddress = connection.prepareStatement("INSERT INTO `address`(userId, address_1, address_2, city, country, zip_code, address_type, state) VALUE (?,?,?,?,?,?,?,?)");
                        prepareAddress.setLong(1, createdUser.getId());
                        prepareAddress.setString(2, user.getCurrentAddress().getAddress1());
                        prepareAddress.setString(3, user.getCurrentAddress().getAddress2());
                        prepareAddress.setString(4, user.getCurrentAddress().getCity());
                        prepareAddress.setString(5, user.getCurrentAddress().getCountry());
                        prepareAddress.setString(6, user.getCurrentAddress().getZipCode());
                        prepareAddress.setString(8, user.getCurrentAddress().getState());
                        prepareAddress.setString(7, "current");

                        prepareAddress.executeUpdate();

                        prepareAddress = connection.prepareStatement("INSERT INTO `address`(userId, address_1, address_2, city, country, zip_code, address_type, state) VALUE (?,?,?,?,?,?,?,?)");
                        prepareAddress.setLong(1, createdUser.getId());
                        prepareAddress.setString(2, user.getPermanentAddress().getAddress1());
                        prepareAddress.setString(3, user.getPermanentAddress().getAddress2());
                        prepareAddress.setString(4, user.getPermanentAddress().getCity());
                        prepareAddress.setString(5, user.getPermanentAddress().getCountry());
                        prepareAddress.setString(6, user.getPermanentAddress().getZipCode());
                        prepareAddress.setString(8, user.getPermanentAddress().getState());
                        prepareAddress.setString(7, "permanent");

                        prepareAddress.executeUpdate();

                        if (createdUser.getCompany()) {
                            PreparedStatement prepareCompany = connection.prepareStatement("INSERT INTO `company`(userId, company_name, company_address) VALUE (?,?,?)");
                            prepareCompany.setLong(1, createdUser.getId());
                            prepareCompany.setString(2, user.getCompanyDetails().getCompanyName());
                            prepareCompany.setString(3, user.getCompanyDetails().getCompanyAddress());

                            prepareCompany.executeUpdate();

                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else{
                return false;
            }

            return true;
    }

    public HashMap<String, User> getAllUsers(){
        users.clear();

        String sql = "select * from user";
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(sql);
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

            User user = new User();

            try {
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setContactNumber(rs.getString("contact_number"));
                user.setCompany(rs.getBoolean("company"));
                user.setUserName(rs.getString("user_name"));
                user.setDateOfBirth(rs.getDate("date_of_birth"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setSocialSecurity(rs.getString("social_security_number"));
                user.setPassword(rs.getString("password"));
                user.setManager(rs.getBoolean("manager"));

                users.put(user.getUserName(), user);
                usersById.put(user.getId(), user);


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return getUsers();
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public User getUserByUserName(String userName){
        getAllUsers();
        if(checkIfUserExists(userName)) {
            return users.get(userName);
        }
        else
            return null;
    }

    public User getUserByUserId(long userId){
        getAllUsers();
        if(usersById.containsKey(userId)){
            return usersById.get(userId);
        }
        else
            return null;
    }

    public boolean checkIfUserExists(String userName){
        getAllUsers();
        if(users.containsKey(userName)){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkIfUserExists(Long userId){
        getAllUsers();
        if(usersById.containsKey(userId)){
            return true;
        }else{
            return false;
        }
    }

    public double getAggregateBalanceOfUser(long userId){
        System.out.println(userId);
        AccountController accountController = new AccountController();
        ArrayList<Account> accounts = accountController.getAllAccountsOfUser(userId);
        AtomicReference<Double> aggregateBalance = new AtomicReference<>((double) 0);
        if(accounts!=null) {
            accounts.forEach(account -> {
                aggregateBalance.updateAndGet(v -> new Double((double) (v + account.getBalance())));
            });
        }
        return aggregateBalance.get();
    }
}
