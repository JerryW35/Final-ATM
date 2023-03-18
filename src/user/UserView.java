package src.user;

import java.sql.Date;

/*
View class for the user details that are stored
 */
public class UserView {

    long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private Date dateOfBirth;
    private String email;
    private String password;

    private String contactNumber;
    // TODO : phone number

//    private Address address;

//    private Company companyData;

//    private HashMap<Integer, AccountImplementation> accounts;

    private boolean company;
    private boolean manager;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isCompany() {
        return company;
    }

    public void setCompany(boolean company) {
        this.company = company;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
