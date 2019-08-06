package com.baeldung.accessmodifiers.publicmodifier;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Student {

    private BigDecimal grade; //new representation
    private String name;   
    private int age;

    public int getGrade() {
        return grade.intValue(); //Backward compatibility
    }
    
    public BigDecimal bigDecimalGrade() {
        return grade;
    }
    
    public Connection getConnection() throws SQLException {
        
        final String URL = "jdbc:h2:~/test";
        return DriverManager.getConnection(URL, "sa", "");

    }
        
    public void setAge(int age) {
        if (age < 0 || age > 150)
            throw new IllegalArgumentException();

        this.age = age;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
