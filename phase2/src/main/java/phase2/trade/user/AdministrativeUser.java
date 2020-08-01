package phase2.trade.user;


public class AdministrativeUser extends User {

    private Boolean isHead;

    public AdministrativeUser(String userName, String email, String telephone, String password, boolean isHead) {
        super(userName, email, telephone, password);
        this.isHead = isHead;
    }

    public boolean getIsHead() {
        return this.isHead;
    }

    public void setIsHead(boolean isHead) {
        this.isHead = isHead;
    }


}
