
package com.Exam.Model;

import java.util.List;

public class Student {

    private int studentId;
    private String studentName;
    private String email;
    private String courseName;
    private double programmingScore;
    private double mathematicsScore;
    private List<String> technicalSkills;

    public Student() {
    }

    public Student(int studentId, String studentName, String email,
                   String courseName, double programmingScore,
                   double mathematicsScore, List<String> technicalSkills) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        this.technicalSkills = technicalSkills;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getProgrammingScore() {
        return programmingScore;
    }

    public void setProgrammingScore(double programmingScore) {
        this.programmingScore = programmingScore;
    }

    public double getMathematicsScore() {
        return mathematicsScore;
    }

    public void setMathematicsScore(double mathematicsScore) {
        this.mathematicsScore = mathematicsScore;
    }

    public List<String> getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(List<String> technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public double getAverageScore() {
        return (programmingScore + mathematicsScore) / 2;
    }

    public String getPerformance() {
        double average = getAverageScore();

        if (average >= 85)
            return "Excellent";
        else if (average >= 70)
            return "Good";
        else if (average >= 50)
            return "Satisfactory";
        else
            return "Needs Improvement";
    }

    public String getStrongerSubject() {
        if (programmingScore > mathematicsScore)
            return "Programming";
        else if (mathematicsScore > programmingScore)
            return "Mathematics";
        else
            return "Equal";
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId +
                "\nName: " + studentName +
                "\nEmail: " + email +
                "\nCourse: " + courseName +
                "\nProgramming Score: " + programmingScore +
                "\nMathematics Score: " + mathematicsScore +
                "\nTechnical Skills: " + technicalSkills +
                "\nAverage: " + getAverageScore() +
                "\nPerformance: " + getPerformance() +
                "\nStronger Subject: " + getStrongerSubject();
    }
}
