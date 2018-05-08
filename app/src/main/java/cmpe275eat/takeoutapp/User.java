package cmpe275eat.takeoutapp;

public class User {
    private String uid, email, password, type;

    public User(){}

    public User(String uid, String email, String password, String type) {
        this.uid = uid; // primary key
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

}
