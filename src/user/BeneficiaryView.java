package src.user;

/*
View for details of the beneficiary created to transfer money to
 */
public class BeneficiaryView {
    long userId;
    long benAccNo;
    String benName;
    long benRoutingNo;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBenAccNo() {
        return benAccNo;
    }

    public void setBenAccNo(long benAccNo) {
        this.benAccNo = benAccNo;
    }

    public String getBenName() {
        return benName;
    }

    public void setBenName(String benName) {
        this.benName = benName;
    }

    public long getBenRoutingNo() {
        return benRoutingNo;
    }

    public void setBenRoutingNo(long benRoutingNo) {
        this.benRoutingNo = benRoutingNo;
    }
}
