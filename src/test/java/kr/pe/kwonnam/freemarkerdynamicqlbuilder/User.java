package kr.pe.kwonnam.freemarkerdynamicqlbuilder;

/**
 *
 */
public class User {
    private String name;
    private int birthyear;
    private EmployeeType employeeType;

    public User() {
    }

    public User(String name, int birthyear) {
        this.name = name;
        this.birthyear = birthyear;
    }

    public User(String name, int birthyear, EmployeeType employeeType) {
        this.name = name;
        this.birthyear = birthyear;
        this.employeeType = employeeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }
}
