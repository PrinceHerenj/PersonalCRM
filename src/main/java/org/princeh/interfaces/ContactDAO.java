package org.princeh.interfaces;

import org.princeh.models.BaseContact;

import java.util.List;

public interface ContactDAO {
    void saveContact(BaseContact contact);
    void updateContact(BaseContact contact);
    void deleteContact(String id);
    BaseContact getContactById(String id);
    List<BaseContact> getAllContacts();
    List<BaseContact> searchContactsByName(String name);
    List<BaseContact> searchContactsByEmail(String email);
    List<BaseContact> searchContactsByDateRange(long startDate, long endDate);
}

