package org.princeh;

import org.princeh.concreteImplementations.ReminderSystem;
import org.princeh.controller.EnhancedContactManager;

public class CRMSystem {
    public static CRMSystem instance;
    private EnhancedContactManager contactManager;
    private ReminderSystem reminderSystem;

    private CRMSystem() {
        contactManager = new EnhancedContactManager();
        reminderSystem = new ReminderSystem();
        contactManager.registerObserver(reminderSystem);
    }

    public static synchronized CRMSystem getInstance() {
        if (instance == null) {
            instance = new CRMSystem();
        }
        return instance;
    }

    public EnhancedContactManager getContactManager() {
        return contactManager;
    }

    public ReminderSystem getReminderSystem() {
        return reminderSystem;
    }
}
