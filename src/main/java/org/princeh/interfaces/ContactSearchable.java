package org.princeh.interfaces;

import org.princeh.models.BaseContact;

public interface ContactSearchable {
    public BaseContact[] searchByName(String name);
    public BaseContact[] searchByEmail(String email);
    public BaseContact[] searchByDateRange(long startDate, long endDate);
}
