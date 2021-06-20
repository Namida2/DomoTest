package registration;

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
}


