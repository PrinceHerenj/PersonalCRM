package org.princeh.models;

public class MongoBusinessContact extends MongoBaseContact {
    private String companyName;
    private String jobTitle;
    private String workPhone;

    public MongoBusinessContact(String firstName, String lastName, String email, String companyName, String jobTitle, String workPhone) {
        super(firstName, lastName, email);
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.workPhone = workPhone;
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getWorkPhone() { return workPhone; }
    public void setWorkPhone(String workPhone) { this.workPhone = workPhone; }

    @Override
    public String getContactMethod() {
        return "Work: " + workPhone;
    }

    @Override
    public String getContactType() {
        return "Business";
    }
}
