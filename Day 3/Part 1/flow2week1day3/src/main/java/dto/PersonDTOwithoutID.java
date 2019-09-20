/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Person;

/**
 *
 * @author Nikolaj
 */
public class PersonDTOwithoutID {
    private String fname;
    private String lname;
    private String phone;

    public PersonDTOwithoutID() {
    }

    public PersonDTOwithoutID(String fname, String lname, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
    }
    
    public PersonDTOwithoutID(Person person) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
    }
    

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
}
