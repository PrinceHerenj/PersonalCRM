package org.princeh.models;

public abstract class BaseContact implements Comparable<BaseContact> {
    private String firstName;
    private String lastName;
    private String email;
    private long lastContactedDate;

    public BaseContact(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.lastContactedDate = System.currentTimeMillis();
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public long getLastContactedDate() { return lastContactedDate; }
    public void updateLastContacted() { this.lastContactedDate = System.currentTimeMillis(); }

    public abstract String getContactMethod();
    public abstract String getContactType();

    public String getFormattedLastContactDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(this.lastContactedDate));
    }

    @Override
    public int compareTo(BaseContact other) {
        int lastNameComparison = this.lastName.compareToIgnoreCase(other.lastName);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }
        return this.firstName.compareToIgnoreCase(other.firstName);
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " (" + this.email + ")";
    }
}
