package org.princeh.interfaces;

import org.princeh.models.BaseContact;

public interface ContactSearchable {
    BaseContact[] searchByName(String name);
    BaseContact[] searchByEmail(String email);
    BaseContact[] searchByDateRange(long startDate, long endDate);
}
