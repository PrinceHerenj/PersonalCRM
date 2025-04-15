package org.princeh;

import org.princeh.controller.MongoContactManager;
import org.princeh.controller.MongoDBManager;
import org.princeh.impl.MongoContactDAO;
import org.princeh.impl.ReminderSystem;
import org.princeh.interfaces.ContactDAO;

public class CRMSystem {
    public static CRMSystem instance;
    private final MongoContactManager contactManager;
    private final ReminderSystem reminderSystem;
    private final MongoDBManager mongoDBManager;

    private CRMSystem(String mongoConnectionString) {
        mongoDBManager = MongoDBManager.getInstance(mongoConnectionString);
        ContactDAO contactDAO = new MongoContactDAO(mongoDBManager);
        contactManager = new MongoContactManager(contactDAO);
        reminderSystem = new ReminderSystem();
        contactManager.registerObserver(reminderSystem);
    }

    public static synchronized CRMSystem getInstance(String mongoConnectionString) {
        if (instance == null) {
            instance = new CRMSystem(mongoConnectionString);
        }
        return instance;
    }

    public MongoContactManager getContactManager() {
        return contactManager;
    }

    public ReminderSystem getReminderSystem() {
        return reminderSystem;
    }

    public void shutdown() {
        if (mongoDBManager != null) {
            mongoDBManager.close();
        }
    }
}
