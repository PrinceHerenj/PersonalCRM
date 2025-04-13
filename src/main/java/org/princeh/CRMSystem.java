package org.princeh;

import org.princeh.concreteImplementations.ReminderSystem;
import org.princeh.controller.EnhancedManager;

public class CRMSystem {
    public static CRMSystem instance;
    private EnhancedManager contactManager;
    private ReminderSystem reminderSystem;

    private CRMSystem() {
        contactManager = new EnhancedManager();
        reminderSystem = new ReminderSystem();
        contactManager.registerObserver(reminderSystem);
    }

    public static synchronized CRMSystem getInstance() {
        if (instance == null) {
            instance = new CRMSystem();
        }
        return instance;
    }

    public EnhancedManager getContactManager() {
        return contactManager;
    }

    public ReminderSystem getReminderSystem() {
        return reminderSystem;
    }
}
