package com.celik.sheetstoslack.model;

import java.util.List;

public class UsersListDTO {

    private boolean ok;

    private String error;

    private List<Member> members;


    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
