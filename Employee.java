import java.io.*;

public class Employee implements Serializable {
    private String name;
    private String id;
    private String department;
    private String securityLevel;

    public Employee (String name, String id, String department, String securityLevel) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.securityLevel = securityLevel;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public String toString() {
        return "Name: " + name +
                    " ID: " + id +
                        " Department: " + department +
                            " Security Level " + securityLevel;
    }
}