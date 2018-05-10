package cmpe275eat.takeoutapp;

public class User {
    private String uid, password, email;

    public User(){

    }

    public User(String uid, String password, String email) {
        this.uid = uid; // primary key
        this.email = email;
        this.password = password;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
