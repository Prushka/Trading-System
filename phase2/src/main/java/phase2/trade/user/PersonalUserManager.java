package phase2.trade.user;


public class PersonalUserManager{

    /*
    public List<PersonalUser> suggest(){
        List<Tuple> ans = new ArrayList<>();
        List<User> pu = new ArrayList<>();
        List<User> allUser = userDAO.findAllUser();
        for (User user : allUser){
            if (user instanceof PersonalUser){
                pu.add((PersonalUser) user);
            }
        }
        for (Item i : loggedInUser.getWishList()) {
             for (PersonalUser p : pu) {
                if (p.getInventory().contains(i)){
                    ans.add(new tuple(i,p))
                }
            }
        return ans;
    }

    /*public void suggest (PersonalUser p) {
        for (PersonalUser x : personalUserRepository) {
            for (Item i : x.getInventory()) {
                if (p.getWishlist().contains(i)) {
                    p.suggest(i);
                }
            }
        }
    }*/
}


