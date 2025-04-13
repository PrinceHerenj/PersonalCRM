package org.princeh.interfaces;

import org.princeh.models.BaseContact;

public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(BaseContact contact, String updateType);
}
