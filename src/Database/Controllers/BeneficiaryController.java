package src.Database.Controllers;

import src.Database.Models.Beneficiary;
import src.Helpers.SQLConnection;
import src.user.BeneficiaryView;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
This specific package handles all database functionalities using a MVC apporach
Every database has a Model, View and a Controller (View is handled by their specific classes as part of the user,account package)
Models have the exact definition of what the database is sending to perform required conversions
Controllers handle functions such as creating data in DB, retrieving data from DB, sending data in correct format to functions and UI

The functions are self-descriptive with their name
 */

public class BeneficiaryController {
    SQLConnection sqlConnection = new SQLConnection();
    HashMap<Long, ArrayList<Beneficiary>> beneficiaries = new HashMap<>();

    public boolean createNewBeneficiary(BeneficiaryView beneficiary){
        sqlConnection.makeConnection();
        Connection connection = sqlConnection.getConnection();
        try {
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            Timestamp updateAt = createdAt;
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `beneficiaries`(user_id, ben_name, ben_acc_no, ben_routing_no, created_at, updated_at) VALUE (?,?,?,?,?,?)");
            pstmt.setLong(1, beneficiary.getUserId());
            pstmt.setString(2, beneficiary.getBenName());
            pstmt.setLong(3, beneficiary.getBenAccNo());
            pstmt.setLong(4, beneficiary.getBenRoutingNo());
            pstmt.setTimestamp(5, createdAt);
            pstmt.setTimestamp(6, updateAt);

            int returnValue = pstmt.executeUpdate();

            if (returnValue > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean getBeneficiariesFromSQL(){
        beneficiaries.clear();


        String sql = "select * from beneficiaries";
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

            Beneficiary beneficiary = new Beneficiary();

            try {
                beneficiary.setId(rs.getInt("id"));
                beneficiary.setUserId(rs.getLong("user_id"));
                beneficiary.setBenName(rs.getString("ben_name"));
                beneficiary.setBenAccNo(rs.getLong("ben_acc_no"));
                beneficiary.setBenRoutingNo(rs.getLong("ben_routing_no"));
                beneficiary.setCreatedAt(rs.getTimestamp("created_at"));
                beneficiary.setUpdatedAt(rs.getTimestamp("updated_at"));
                beneficiary.setType(rs.getString("account_type"));

                if(beneficiaries.containsKey(beneficiary.getUserId())){
                    ArrayList<Beneficiary> tempBen = beneficiaries.get(beneficiary.getUserId());
                    tempBen.add(beneficiary);
                }else{
                    ArrayList<Beneficiary> tempBen = new ArrayList<>();
                    tempBen.add(beneficiary);
                    beneficiaries.put(beneficiary.getUserId(), tempBen);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }


    public ArrayList<Beneficiary> getAllBeneficiariesOfUser(long userId){
        getBeneficiariesFromSQL();
        if(beneficiaries.containsKey(userId)){
            return beneficiaries.get(userId);
        }else{
            return null;
        }
    }
}
