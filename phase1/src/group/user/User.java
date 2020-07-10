package group.user;

import group.repository.UniqueId;
import group.repository.map.MappableBase;

import java.util.ArrayList;
import java.util.List;


public class User extends MappableBase implements UniqueId {

    private String userName;
    private String email;
    private String telephone;
    private String password;

    /**
     * Creates a new User with userName, email, telephone and given password.
     *
     * @param userName the username of this Person.
     * @param email the email this Person.
     * @param telephone the telephone number of this person
     * @param password the password this user set to
     */


    public User(String userName, String email, String telephone, String password){
        List<String> user = new ArrayList<String>();
        user.add(userName);
        user.add(email);
        user.add(telephone);
        user.add(password);
        super (user);
    }

    //public {
         //this.userName = userName;
         //this.email = email;
         //this.telephone = telephone;
         //this.password = password;
     //}



    public String getName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public void setUid(long value) {

    }

    @Override
    public long getUid() {
        return 0;
    }
}
