package InterfaceLib;

public interface SignUp {
    public void registerUser(String name , String password , String email , String userType);
    public void deleteUser(String name , String userType);
}
