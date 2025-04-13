package org.princeh.concreteImplementations;

import org.princeh.interfaces.ContactObserver;
import org.princeh.models.BaseContact;

import java.util.ArrayList;
import java.util.List;

public class ReminderSystem implements ContactObserver {
    private static final long THIRTY_DAYS_MS = 30L * 24 * 60 * 60 * 1000;

    @Override
    public void onContactUpdate(BaseContact contact, String updateType) {
        if (updateType.equals("UPDATED")) {
            System.out.println("Reminder scheduled for " + contact.getFirstName() + " " +
                    contact.getLastName() + " in 30 days");
        }
    }

    public List<BaseContact> getDueContacts(List<BaseContact> allContacts) {
        List<BaseContact> dueContacts = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        for (BaseContact contact : allContacts) {
            if (currentTime - contact.getLastContactedDate() > THIRTY_DAYS_MS) {
                dueContacts.add(contact);
            }
        }

        return dueContacts;
    }
}
