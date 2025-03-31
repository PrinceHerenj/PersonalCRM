package org.princeh.models;

public class PersonalContact extends BaseContact {
    private String phoneNumber;
    private String address;
    private String notes;

    public PersonalContact(String firstName, String lastName, String email, String phoneNumber, String address) {
        super(firstName, lastName, email);
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.notes = "";
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String getContactMethod() {
        return "Phone " + phoneNumber;
    }

    @Override
    public String getContactType() {
        return "Personal";
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.phoneNumber + " " + this.address;
    }
}
