package org.princeh.controller;

import org.princeh.interfaces.Observer;
import org.princeh.interfaces.ContactSearchable;
import org.princeh.interfaces.Subject;
import org.princeh.models.BaseContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnhancedManager implements ContactSearchable, Subject {
    private List<BaseContact> contacts;
    private List<Observer> observers;

    public EnhancedManager() {
        contacts = new ArrayList<>();
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(BaseContact contact, String updateType) {
        for (Observer observer : observers) {
            observer.onContactUpdate(contact, updateType);
        }
    }

    public void addContact(BaseContact contact) {
        contacts.add(contact);
        Collections.sort(contacts);
        notifyObservers(contact, "ADDED");
    }

    public boolean removeContact(BaseContact contact) {
        return contacts.remove(contact);
    }

    public List<BaseContact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    @Override
    public BaseContact[] searchByName(String name) {
        if (name == null || name.isEmpty()) {
            return new BaseContact[0];
        }

        name = name.toLowerCase();
        List<BaseContact> results = new ArrayList<>();

        for (BaseContact contact : contacts) {
            if (contact.getFirstName().toLowerCase().contains(name) ||
                    contact.getLastName().toLowerCase().contains(name)) {
                results.add(contact);
            }
        }

        return results.toArray(new BaseContact[0]);
    }

    @Override
    public BaseContact[] searchByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return new BaseContact[0];
        }

        email = email.toLowerCase();
        List<BaseContact> results = new ArrayList<>();

        for (BaseContact contact : contacts) {
            if (contact.getEmail().toLowerCase().contains(email)) {
                results.add(contact);
            }
        }

        return results.toArray(new BaseContact[0]);
    }

    @Override
    public BaseContact[] searchByDateRange(long startDate, long endDate) {
        List<BaseContact> results = new ArrayList<>();

        for (BaseContact contact : contacts) {
            long contactDate = contact.getLastContactedDate();
            if (contactDate >= startDate && contactDate <= endDate) {
                results.add(contact);
            }
        }

        return results.toArray(new BaseContact[0]);
    }

    public void updateLastContactedDate(BaseContact contact) {
        if (contacts.contains(contact)) {
            contact.updateLastContacted();
            notifyObservers(contact, "UPDATED");
        }
    }
}
