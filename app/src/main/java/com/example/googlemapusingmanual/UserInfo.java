package com.example.googlemapusingmanual;

public class UserInfo {
    private String nickname;
    private String age;
    private String weight;
    private String height;
    private String gender;
    private String ID; //TODO added

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setID(String s) {this.ID = s;}

    public String getID() {return this.ID;}

}
