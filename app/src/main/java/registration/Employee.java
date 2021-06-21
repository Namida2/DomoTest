package registration;

import androidx.annotation.Nullable;

public class Employee {

    private String name;
    private String email;
    private String password;
    private String post;
    private Boolean permission;

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }
    public Boolean getPermission() {
        return permission;
    }
    private static Employee employee = new Employee();
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public String getPost() {
        return post;
    }
    public static Employee getEmployee() {
        return employee;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if( obj == null || obj.getClass() != this.getClass()) return false;
        Employee employee = (Employee) obj;
        return employee.getName().equals(getName())
            && employee.getEmail().equals(getEmail())
            && employee.getPassword().equals(getPassword())
            && employee.getPost().equals(getPost());
    }
}


