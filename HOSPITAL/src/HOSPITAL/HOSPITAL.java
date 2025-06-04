package HOSPITAL;

import java.sql.*;
import java.util.Scanner;

public class HOSPITAL {

    public static final String url = "jdbc:mysql://localhost:3306/hospital";

    public static final String username = "root";

    public static final String password = "9602267101";

    public static void main (String[] ignoredArgs) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Patients patients = new Patients(connection, scanner);
            Docters docters = new Docters(connection, scanner);
            while (true){
                System.out.println("HOSPITAL System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. Add Docter");
                System.out.println("4. View Docters");
                System.out.println("5. Book Appointment");
                System.out.println("6. View Appointment");
                System.out.println("7. Exit");
                int Choich = scanner.nextInt();
                switch (Choich){
                    case 1:
                        //Add Patient
                        patients.addpatients();
                        System.out.println();
                        break;
                    case 2:
                        //View Patients
                        patients.viewpatients();
                        System.out.println();
                        break;
                    case 3:
                         //Add Docter
                        docters.adddocters();
                        System.out.println();
                        break;
                    case 4:
                        //View Docters
                        docters.viewdocters();
                        System.out.println();
                        break;
                    case 5:
                        //Book Appointment
                        bookAppoientment(patients, docters, connection, scanner);
                        System.out.println();
                        break;
                    case 6:
                        //View Appointment
                        viewAppoientment(patients, docters, connection);
                        System.out.println();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Enter Valid Choich!!!");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppoientment(Patients patients, Docters docters, Connection connection, Scanner scanner){
        System.out.println("Enter Patient ID : ");
        int patientid = scanner.nextInt();
        System.out.println("Enter Docter ID : ");
        int docterid = scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM-DD)");
        String appoientmentDate = scanner.next();
        if (patients.getpatientbyid(patientid) && docters.getdocterbyid(docterid)){
            if (ChackDocterAvalibility(docterid, appoientmentDate, connection)) {
                String appoientmentquery = "INSERT INTO `hospital`.`appoientment_date` (`patients_id`, `docters_id`, `appoientment_date`) VALUES (?, ?, ?);\n";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appoientmentquery);
                    preparedStatement.setInt(1, patientid);
                    preparedStatement.setInt(2, docterid);
                    preparedStatement.setString(3, appoientmentDate);
                    int affectedRow = preparedStatement.executeUpdate();
                    if (affectedRow > 0) {
                        System.out.print("Appoientment successful!!!");
                    } else {
                        System.out.print("Appoientment Failded!!!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            System.out.println("Either Docter or Patient Not Exited!!!");
        }
    }
    public static void viewAppoientment(Patients patients, Docters docters, Connection connection){
        String query="SELECT * FROM appoientment_date;";
         try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+------------------------+----------------+-------------+---------------------+");
            System.out.println("| Appoientment_Date ID   | Patients ID    | Docters ID  | Appoientment_Date   |");
            System.out.println("+------------------------+----------------+-------------+---------------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int patientid = resultSet.getInt("patients_id");
                int docterid = resultSet.getInt("docters_id");
                Date appoientmentdate = resultSet.getDate("appoientment_date");
                System.out.printf("|%-24s|%-16s|%-13s|%-21s|\n", id, patientid, docterid,  appoientmentdate);
            }
            System.out.println("+------------------------+----------------+-------------+---------------------+");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static boolean ChackDocterAvalibility(int docterid, String appoientmentDate, Connection connection) {
        String query = "select count(*) from appoientment_date where docters_id = ? and appoientment_date = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, docterid);
            preparedStatement.setString(2, appoientmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

