package src.Database.Models;


/*
Following the MVC structure this is the design for the Company Model
 */
public class Company {
    String companyName;
    String companyAddress;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
}
