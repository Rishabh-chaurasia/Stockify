package models;

public class User {
    public String name, email, username, pan;
    public long phone, aadhar;
    public int age;
    public double income;

    public User(){}
    public User(String name,String email,long phone,int age,long aadhar,String pan,double income,String username){
        this.name=name; this.email=email; this.phone=phone; this.age=age; this.aadhar=aadhar; this.pan=pan; this.income=income; this.username=username;
    }
}
