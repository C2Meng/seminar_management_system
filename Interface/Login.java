package Interface;

public interface Login{
    void authenticateUser(String username , String password);
    void logoutUser();
    String dashboardString();
}

