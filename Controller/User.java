package Controller;

import InterfaceLib.Role;
import java.util.UUID;

public class User {
     protected String id;
     protected String name;
     protected Role role;
     protected String email;
     protected String password;



       public User(String email, String name, String password, Role role) {
        this.id = generateUserId();
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

     private String generateUserId() {
        return UUID.randomUUID().toString();
     }

     public String getId(){
        return id;
     }

     public Role getRole(){
        return role;
     }

     public String getName(){
        return name;
     }


        public String getEmail(){
            return email;
        }

        public String getPassword(){
            return password;
        }


}