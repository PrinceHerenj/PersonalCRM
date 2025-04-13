package org.princeh.interfaces;

import org.princeh.models.BaseContact;

import java.util.Observer;

public interface ContactSubject {
    void registerObserver(ContactObserver observer);
    void removeObserver(ContactObserver observer);
    void notifyObservers(BaseContact contact, String updateType);
}
