package com.example.project03_timekeeping.object;

import java.util.List;

public class MonthData {
    private String month;
    private List<Member> listMember;

    public MonthData(String month, List<Member> listMember) {
        this.month = month;
        this.listMember = listMember;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<Member> getListMember() {
        return listMember;
    }

    public void setListMember(List<Member> listMember) {
        this.listMember = listMember;
    }
}
