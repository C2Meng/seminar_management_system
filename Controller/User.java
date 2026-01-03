package Controller;

import InterfaceLib.Role;

public class User {
     protected String id;
     protected String name;
     protected Role role;
     protected String email;
     protected String password;



       public User(String email, String name, String password, Role role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
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