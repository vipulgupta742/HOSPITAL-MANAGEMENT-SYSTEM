package HOSPITALMANAGEMENTSYSTEM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
    private Connection connection;
    public Doctor(Connection connection){
        this.connection=connection;

    }
    public void viewDoctor(){
        String query = "SELECT * FROM doctors";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("Doctors:");
            System.out.println("+------------+------------------+-------------------+");
            System.out.println("| Doctor Id  | Name             | Specialization    |");
            System.out.println("+------------+------------------+-------------------+");
            while (rs.next()) {
                int doctorId = rs.getInt("id");
                String doctorName = rs.getString("name");
                String specialization = rs.getString("specialization");
                System.out.printf("|%-12s|%-18s|%-19s|\n",doctorId,doctorName,specialization);
                System.out.println("+------------+------------------+-------------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";
        try {
            PreparedStatement ps=connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
