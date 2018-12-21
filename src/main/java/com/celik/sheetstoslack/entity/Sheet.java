package com.celik.sheetstoslack.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Sheet {

    @Id
    @GeneratedValue
    private int id;
    private String sheetId;

    @OneToMany(mappedBy ="sheet" , fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<NotificationColumn> notifications;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public List<NotificationColumn> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationColumn> notifications) {
        this.notifications = notifications;
    }
}
