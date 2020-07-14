package group.user;

import java.util.List;

public class AdministrativeUser extends User {

    private String userName;
    private String email;
    private String telephone;
    private String password;
    private Boolean isHead;

    public AdministrativeUser(String userName, String email, String telephone, String password, boolean isHead) {
        super(userName, email, telephone, password);
        this.isHead = isHead;
    }

    public AdministrativeUser(List<String> record){super(record);}

    public boolean getIsHead() {
        return this.isHead;
    }

    public void setIsHead(boolean isHead) {
        this.isHead = isHead;
    }


}
