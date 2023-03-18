package src.Database.Models;

import java.sql.Date;
import java.sql.Timestamp;



/*
Following the MVC structure this is the design for the user model
 */
public class User {
    long id;
    String firstName;
    String lastName;
    String email;
    String password;
    Boolean company;
    String userName;
    Date dateOfBirth;

    String contactNumber;

    String socialSecurity;
    Timestamp createdAt;
    Timestamp updatedAt;
    Boolean manager;

    Address permanentAddress;

    Address currentAddress;

    Company companyDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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


    public Boolean getCompany() {
        return company;
    }

    public void setCompany(Boolean company) {
        this.company = company;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public Address getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(Address permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Company getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(Company companyDetails) {
        this.companyDetails = companyDetails;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(String socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", company=" + company +
                ", userName='" + userName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", contactNumber='" + contactNumber + '\'' +
                ", socialSecurity='" + socialSecurity + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", manager=" + manager +
                ", permanentAddress=" + permanentAddress +
                ", currentAddress=" + currentAddress +
                ", companyDetails=" + companyDetails +
                '}';
    }
}
