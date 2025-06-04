package HOSPITAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Docters {

    private final Connection connection;

    private final Scanner scanner;

    public Docters(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void adddocters(){
        System.out.print("Enter Docter Name : ");
        String name = scanner.next();
        System.out.print("Enter Docter Specialization : ");
        String specialization = scanner.next();
        try{
            String query = "INSERT INTO `hospital`.`docters` (`name`, `specialization`) VALUES (?, ?);\n";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,specialization);
            int affecctedRow = preparedStatement.executeUpdate();
            if (affecctedRow>0){
                System.out.print("Docter Added Successfully");
            }
            else {
                System.out.print("Faild to Add Docter");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void viewdocters(){
        String query = "select * from docters;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+--------------+----------------------+------------------+");
            System.out.println("| Docters ID   | Name                 | Specialization   |");
            System.out.println("+--------------+----------------------+------------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getNString("name");
                String specialization = resultSet.getNString("specialization");
                System.out.printf("|%-14s|%-22s|%-18s|\n", id, name, specialization);
            }
            System.out.println("+--------------+----------------------+-----------------+");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Boolean getdocterbyid(int id){
        String query = "select * from docters where id = ?";
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
