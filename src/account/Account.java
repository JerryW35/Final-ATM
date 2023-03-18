package src.account;

/*
Account interface defining the interface for the account implementation class
Basic functions like getBalance, getInterest, setInterest and getAccountType which are associated with every account are given here
 */
public interface Account {
    public String getAccountType();
    public double getBalance();
    // create account in DB
    public double getInterest();
    public void setInterest(double interest);
}
