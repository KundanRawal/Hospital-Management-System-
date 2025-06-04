package HOSPITAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patients {

    private final Connection connection;

    private final Scanner scanner;

    public Patients(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addpatients(){
        System.out.print("Enter Patient Name : ");
        String name = scanner.next();
        System.out.print("Enter Patient Age : ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender : ");
        String gender = scanner.next();
        try{
            String query = "INSERT INTO `hospital`.`patients` (`name`, `age`, `gender`) VALUES (?, ?, ?);\n";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affecctedRow = preparedStatement.executeUpdate();
            if (affecctedRow>0){
                System.out.print("Patient Added Successfully");
            }
            else {
                System.out.print("Faild to Add Patient");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void viewpatients(){
        String query = "select * from patients;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+--------------+----------------------+--------+----------+");
            System.out.println("| Patients ID  | Name                 | Age    | Gender   |");
            System.out.println("+--------------+----------------------+--------+----------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getNString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getNString("gender");
                System.out.printf("|%-14s|%-22s|%-8s|%-10s|\n", id, name, age, gender);
            }
            System.out.println("+--------------+----------------------+--------+----------+");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Boolean getpatientbyid(int id){
        String query = "select * from patients where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
