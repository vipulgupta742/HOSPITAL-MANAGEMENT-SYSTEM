package HOSPITALMANAGEMENTSYSTEM;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient Details");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println("");
                    case 2:
                        patient.viewPatient();
                        System.out.println("");
                        break;
                    case 3:
                        doctor.viewDoctor();
                        System.out.println("");
                        break;
                    case 4:
                        bookAppointment(patient,doctor,connection,scanner);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection, Scanner scanner){
        System.out.println("Enter Patient ID: ");
        int patientID = scanner.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorID = scanner.nextInt();
        System.out.println("Enter Appointment Date in YYYY-MM-DD: ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientID) && doctor.getDoctorById(doctorID)){
             if(checkDoctorAvailability(doctorID,appointmentDate,connection)){
                 String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date)VALUES(?,?,?)";
                 try{
                     PreparedStatement preparedStatement=connection.prepareStatement(sql);
                     preparedStatement.setInt(1,patientID);
                     preparedStatement.setInt(2,doctorID);
                     preparedStatement.setString(3,appointmentDate);
                     int rowsaffected=preparedStatement.executeUpdate();
                     if(rowsaffected>0){
                         System.out.println("Appointment Booked Successfully");
                     }else{
                         System.out.println("Appointment Booking Failed");
                     }
                 }catch(SQLException e){
                     e.printStackTrace();
                 }
             }else{
                 System.out.println("Doctor is not available on this date.");
             }
        }else{
            System.out.println("Either Patient or Doctor does not exist");
        }


    }
    public static boolean checkDoctorAvailability(int doctorID,String appointmentDate,Connection connection){
        String sql = "SELECT count(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1,doctorID);
            ps.setString(2,appointmentDate);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                int count=rs.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
