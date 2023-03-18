package src.Database.Models;

import java.sql.Timestamp;


/*
Following the MVC structure this is the design for the beneficiary Model
 */
public class Beneficiary {
    int id;
    long userId;
    String benName;
    long benAccNo;
    long benRoutingNo;
    Timestamp createdAt;
    Timestamp updatedAt;

    String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBenName() {
        return benName;
    }

    public void setBenName(String benName) {
        this.benName = benName;
    }

    public long getBenAccNo() {
        return benAccNo;
    }

    public void setBenAccNo(long benAccNo) {
        this.benAccNo = benAccNo;
    }

    public long getBenRoutingNo() {
        return benRoutingNo;
    }

    public void setBenRoutingNo(long benRoutingNo) {
        this.benRoutingNo = benRoutingNo;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
