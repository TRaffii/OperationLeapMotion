/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.model;

import java.sql.Date;

/**
 *
 * @author Rafal
 */
public class Users {
    private int id;
    private String userName;
    private int result;
    private Date dateTime;

    public Users(String userName, int result, Date dateTime) {
        this.userName = userName;
        this.result = result;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public int getResult() {
        return result;
    }

    public Date getDateTime() {
        return dateTime;
    }
    @Override
    public String toString()
    {
        return userName+" "+result+" "+dateTime+"\n";
    }
}
