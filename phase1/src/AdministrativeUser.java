package src;

public class AdministrativeUser extends User{

    private String userName;
    private String email;
    private String telephone;
    private String password;
    private boolean isHead;

    public AdministrativeUser(String userName, String email, String telephone, String password, boolean isHead) {
        super(userName, email, telephone, password);
        this.isHead = isHead;
    }


    public AdministrativeUser(String userName, String email, String password, boolean isHead) {
        super(userName, email, password);
        this.isHead = isHead;
    }


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

    public boolean getIsHead(){
        return this.isHead;
    }

    public void setIsHead(boolean isHead){
        this.isHead = isHead;
    }





}
