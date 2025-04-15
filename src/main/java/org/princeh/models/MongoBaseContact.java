package org.princeh.models;

public abstract class MongoBaseContact extends BaseContact {
    private String id;

    public MongoBaseContact(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastContactedDate(long date) {
        super.lastContactedDate = date;
    }
}
