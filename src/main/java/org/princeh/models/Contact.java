package org.princeh.models;

public class Contact implements Comparable<Contact>{
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String notes;
    private long lastContactedDate;

    public Contact(String firstName, String lastName, String email, String phoneNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.notes = "";
        this.lastContactedDate = System.currentTimeMillis();
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public long getLastContactedDate() { return lastContactedDate; }
    public void setLastContactedDate(long lastContactedDate) { this.lastContactedDate = lastContactedDate; }

    public void updateLastContacted() {
        this.lastContactedDate = System.currentTimeMillis();
    }

    public String getFormattedLastContactedDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(this.lastContactedDate));
    }

    @Override
    public int compareTo(Contact other) {
        int lastNameComparison = this.lastName.compareToIgnoreCase(other.lastName);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }
        return this.firstName.compareToIgnoreCase(other.firstName);
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName + " (" + email + ")";
    }
}
