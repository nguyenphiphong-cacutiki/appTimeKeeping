package com.example.project03_timekeeping.object;

public class Member {
    private int id;
    private String name;
    private int keyName;
    private Boolean isCheck;
    private int countTime;

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
        this.keyName = id;
        this.isCheck = false;
    }

    public Member(String name, int countTime) {
        this.name = name;
        this.countTime = countTime;
    }

    public int getCountTime() {
        return countTime;
    }

    public void setCountTime(int countTime) {
        this.countTime = countTime;
    }

    public int getKeyName() {
        return keyName;
    }

    public void setKeyName(int keyName) {
        this.keyName = keyName;
    }

    public Member(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }
}
