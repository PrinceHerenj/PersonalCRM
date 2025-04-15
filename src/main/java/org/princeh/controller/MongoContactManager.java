package org.princeh.controller;

import org.princeh.interfaces.ContactDAO;
import org.princeh.interfaces.ContactSearchable;
import org.princeh.interfaces.Observer;
import org.princeh.models.BaseContact;
import org.princeh.models.MongoBaseContact;

import java.util.ArrayList;
import java.util.List;

public class MongoContactManager implements ContactSearchable {
    private final ContactDAO contactDAO;
    private final List<Observer> observers;

    public MongoContactManager(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
        this.observers = new ArrayList<>();
    }

    public void addContact(BaseContact contact) {
        contactDAO.saveContact(contact);
        notifyObservers(contact, "ADDED");
    }

    public void updateContact(BaseContact contact) {
        contactDAO.updateContact(contact);
        notifyObservers(contact, "UPDATED");
    }

    public boolean removeContact(BaseContact contact) {
        if (contact instanceof MongoBaseContact) {
            String id = ((MongoBaseContact) contact).getId();

            if (id != null && !id.isEmpty()) {
                contactDAO.deleteContact(id);
                notifyObservers(contact, "DELETED");
                return true;
            }
        }
        return false;
    }

    public List<BaseContact> getAllContacts() {
        return contactDAO.getAllContacts();
    }

    @Override
    public BaseContact[] searchByName(String name) {
        if (name == null || name.isEmpty()) {
            return new BaseContact[0];
        }

        List<BaseContact> results = contactDAO.searchContactsByName(name);
        return results.toArray(new BaseContact[0]);
    }

    @Override
    public BaseContact[] searchByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return new BaseContact[0];
        }

        List<BaseContact> results = contactDAO.searchContactsByEmail(email);
        return results.toArray(new BaseContact[0]);
    }

    @Override
    public BaseContact[] searchByDateRange(long startDate, long endDate) {
        List<BaseContact> results = contactDAO.searchContactsByDateRange(startDate, endDate);
        return results.toArray(new BaseContact[0]);
    }

    public void updateLastContactedDate(BaseContact contact) {
        if (contact instanceof MongoBaseContact) {
            contact.updateLastContacted();
            contactDAO.updateContact(contact);
            notifyObservers(contact, "UPDATED");
        }
    }

    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(BaseContact contact, String updateType) {
        for (Observer observer : observers) {
            observer.onContactUpdate(contact, updateType);
        }
    }
}
