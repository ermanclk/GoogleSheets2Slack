package com.celik.sheetstoslack.model;

public class Member {

    private String id;
    private String team_id;
    private String name;
    private boolean deleted;
    private SlackProfile profile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public SlackProfile getProfile() {
        return profile;
    }

    public void setProfile(SlackProfile profile) {
        this.profile = profile;
    }
}
