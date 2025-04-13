package org.princeh;

import org.princeh.concreteImplementations.ReminderSystem;
import org.princeh.controller.ContactManager;

public class CRMSystem {
    public static CRMSystem instance;
    private final ContactManager contactManager;
    private final ReminderSystem reminderSystem;

    private CRMSystem() {
        contactManager = new ContactManager();
        reminderSystem = new ReminderSystem();
        contactManager.registerObserver(reminderSystem);
    }

    public static synchronized CRMSystem getInstance() {
        if (instance == null) {
            instance = new CRMSystem();
        }
        return instance;
    }

    public ContactManager getContactManager() {
        return contactManager;
    }

    public ReminderSystem getReminderSystem() {
        return reminderSystem;
    }
}
