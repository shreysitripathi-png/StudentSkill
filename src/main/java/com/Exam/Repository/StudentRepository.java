
package com.Exam.Repository;

import com.Exam.Model.Student;
import java.sql.*;
import java.util.*;

public class StudentRepository {

    String url = "jdbc:mysql://localhost:3306/StudentDB";
    String username = "root";
    String password = "Shreysi2006@";

    Scanner sc = new Scanner(System.in);

    public void createStudent() {

        List<Student> students = new ArrayList<>();

        students.add(new Student(101, "Shreysi", "shreysi@gmail.com",
                "BTech", 90, 85,
                Arrays.asList("Java", "C++", "Python")));

        students.add(new Student(102, "Samyak", "samyak@gmail.com",
                "BTech", 45, 80,
                Arrays.asList("Java", "Python")));

        students.add(new Student(103, "Rahul", "rahul@gmail.com",
                "BCA", 75, 65,
                Arrays.asList("Java", "C++", "Python")));

        String studentSQL = "INSERT INTO Student VALUES (?, ?, ?, ?, ?, ?)";
        String skillSQL = "INSERT INTO TechnicalSkills VALUES (?, ?)";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password)) {

            PreparedStatement studentStatement =
                    connection.prepareStatement(studentSQL);

            PreparedStatement skillStatement =
                    connection.prepareStatement(skillSQL);

            for (Student student : students) {

                try {
                    studentStatement.setInt(1, student.getStudentId());
                    studentStatement.setString(2, student.getStudentName());
                    studentStatement.setString(3, student.getEmail());
                    studentStatement.setString(4, student.getCourseName());
                    studentStatement.setDouble(5, student.getProgrammingScore());
                    studentStatement.setDouble(6, student.getMathematicsScore());

                    studentStatement.executeUpdate();

                    for (String skill : student.getTechnicalSkills()) {
                        skillStatement.setInt(1, student.getStudentId());
                        skillStatement.setString(2, skill);
                        skillStatement.executeUpdate();
                    }

                    System.out.println(student.getStudentName() +
                            " inserted successfully");

                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Duplicate Student ID or Email: "
                            + student.getStudentId());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getStudentDetails() {

        String SQL = "SELECT *, " +
                "(programming_score + mathematics_score)/2 AS average_score, " +
                "CASE " +
                "WHEN (programming_score + mathematics_score)/2 >= 85 THEN 'Excellent' " +
                "WHEN (programming_score + mathematics_score)/2 >= 70 THEN 'Good' " +
                "WHEN (programming_score + mathematics_score)/2 >= 50 THEN 'Satisfactory' " +
                "ELSE 'Needs Improvement' END AS performance, " +
                "CASE " +
                "WHEN programming_score > mathematics_score THEN 'Programming' " +
                "WHEN mathematics_score > programming_score THEN 'Mathematics' " +
                "ELSE 'Equal' END AS stronger_subject " +
                "FROM Student";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                System.out.println("Student ID: " +
                        resultSet.getInt("student_id"));

                System.out.println("Name: " +
                        resultSet.getString("student_name"));

                System.out.println("Average: " +
                        resultSet.getDouble("average_score"));

                System.out.println("Performance: " +
                        resultSet.getString("performance"));

                System.out.println("Stronger Subject: " +
                        resultSet.getString("stronger_subject"));

                System.out.println("--------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchStudent() {

        System.out.println("1. Search by ID");
        System.out.println("2. Search by Email");
        System.out.println("3. Search by Course");

        int choice = sc.nextInt();
        sc.nextLine();

        String SQL = "";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password)) {

            PreparedStatement statement;

            if (choice == 1) {

                System.out.println("Enter Student ID:");
                int id = sc.nextInt();

                SQL = "SELECT * FROM Student WHERE student_id = ?";
                statement = connection.prepareStatement(SQL);
                statement.setInt(1, id);

            } else if (choice == 2) {

                System.out.println("Enter Email:");
                String email = sc.nextLine();

                SQL = "SELECT * FROM Student WHERE email = ?";
                statement = connection.prepareStatement(SQL);
                statement.setString(1, email);

            } else if (choice == 3) {

                System.out.println("Enter Course:");
                String course = sc.nextLine();

                SQL = "SELECT * FROM Student WHERE course_name = ?";
                statement = connection.prepareStatement(SQL);
                statement.setString(1, course);

            } else {
                System.out.println("Invalid Choice");
                return;
            }

            displayResults(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayResults(PreparedStatement statement)
            throws SQLException {

        ResultSet resultSet = statement.executeQuery();

        boolean found = false;

        while (resultSet.next()) {
            found = true;

            int id = resultSet.getInt("student_id");

            System.out.println("ID: " + id);
            System.out.println("Name: " +
                    resultSet.getString("student_name"));
            System.out.println("Email: " +
                    resultSet.getString("email"));
            System.out.println("Course: " +
                    resultSet.getString("course_name"));
            System.out.println("Programming: " +
                    resultSet.getDouble("programming_score"));
            System.out.println("Mathematics: " +
                    resultSet.getDouble("mathematics_score"));

            String skillSQL =
                    "SELECT skill_name FROM TechnicalSkills WHERE student_id = ?";

            try (PreparedStatement skillStatement =
                         statement.getConnection().prepareStatement(skillSQL)) {

                skillStatement.setInt(1, id);

                try (ResultSet skillResult = skillStatement.executeQuery()) {
                    System.out.print("Skills: ");

                    while (skillResult.next()) {
                        System.out.print(
                                skillResult.getString("skill_name") + " ");
                    }
                }
            }

            System.out.println("\n--------------------------");
        }

        if (!found) {
            System.out.println("Student Not Found");
        }

        resultSet.close();
    }

    public void meritList() {

        String SQL = "SELECT student_id, student_name, " +
                "(programming_score + mathematics_score)/2 AS average_score " +
                "FROM Student ORDER BY average_score DESC";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("MERIT LIST");

            while (resultSet.next()) {

                System.out.println("ID: " +
                        resultSet.getInt("student_id"));

                System.out.println("Name: " +
                        resultSet.getString("student_name"));

                System.out.println("Average: " +
                        resultSet.getDouble("average_score"));

                System.out.println("--------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sameTechnicalSkills() {

        String SQL = "SELECT skill_name, " +
                "GROUP_CONCAT(student_name SEPARATOR ', ') AS students " +
                "FROM Student JOIN TechnicalSkills USING(student_id) " +
                "GROUP BY skill_name HAVING COUNT(DISTINCT student_id) > 1";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                System.out.println("Common Skill: " +
                        resultSet.getString("skill_name"));

                System.out.println("Students: " +
                        resultSet.getString("students"));

                System.out.println("--------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void programmingImprovement() {

        String SQL = "SELECT * FROM Student " +
                "WHERE programming_score < 50 AND mathematics_score >= 75";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet resultSet = statement.executeQuery()) {

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                System.out.println("Student ID: " +
                        resultSet.getInt("student_id"));

                System.out.println("Name: " +
                        resultSet.getString("student_name"));

                System.out.println("Programming Score: " +
                        resultSet.getDouble("programming_score"));

                System.out.println("Mathematics Score: " +
                        resultSet.getDouble("mathematics_score"));

                System.out.println("--------------------------");
            }

            if (!found) {
                System.out.println("No students found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mainMenu() {

        while (true) {

            System.out.println("\n1. Insert Student Records");
            System.out.println("2. Calculate Average and Performance");
            System.out.println("3. Search Student");
            System.out.println("4. Generate Merit List");
            System.out.println("5. Find Same Technical Skills");
            System.out.println("6. Programming Improvement Students");
            System.out.println("7. Exit");

            System.out.println("Enter your choice:");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    createStudent();
                    break;

                case 2:
                    getStudentDetails();
                    break;

                case 3:
                    searchStudent();
                    break;

                case 4:
                    meritList();
                    break;

                case 5:
                    sameTechnicalSkills();
                    break;

                case 6:
                    programmingImprovement();
                    break;

                case 7:
                    System.out.println("Program Ended");
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}
