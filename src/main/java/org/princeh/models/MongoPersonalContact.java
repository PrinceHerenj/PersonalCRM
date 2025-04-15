package org.princeh.models;

public class MongoPersonalContact extends MongoBaseContact {
    private String phoneNumber;
    private String address;
    private String birthday;

    public MongoPersonalContact(String firstName, String lastName, String email, String phoneNumber, String address) {
        super(firstName, lastName, email);
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthday = "";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String getContactMethod() {
        return "Phone: " + phoneNumber;
    }

    @Override
    public String getContactType() {
        return "Personal";
    }
}
