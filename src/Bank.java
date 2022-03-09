import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    public String getNewUserUUID() {
        users = new ArrayList<>();
        String uuid;
        Random random = new Random();
        int len = 6;
        boolean nonUnique;
        do {
            uuid = "";
            for (int c = 0; c < len; c ++){
                uuid += ((Integer)random.nextInt(10)).toString();
            }
            nonUnique = false;
            for(User user : this.users){
                if(uuid.compareTo(user.getUUID()) == 0){
                    nonUnique = true;
                    break;
                }
            }

        }while (nonUnique);

        return uuid;
    }

    public String getNewAccountUUID() {
        String uuid;
        Random random = new Random();
        int len = 10;
        boolean nonUnique;
        do {
            uuid = "";
            for (int c = 0; c < len; c ++){
                uuid += ((Integer)random.nextInt(10)).toString();
            }
            nonUnique = false;
            for(Account account : this.accounts){
                if(uuid.compareTo(account.getUUID()) == 0){
                    nonUnique = true;
                    break;
                }
            }
        }while (nonUnique);
        return uuid;
    }
    public void addAccount(Account account){
        this.accounts.add(account);
    }


    public User addUser(String firstName,String lastName,String pin){
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);
        Account newAccount = new Account("Savings",newUser,this);
        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);
        return newUser;
    }
    public User userLogin(String userID, String pin){
        for(User user : this.users){
            if(user.getUUID().compareTo(userID) == 0 && user.validatePin(pin)){
                return user;
            }
        }
        return null;
    }
}
