package org.princeh.interfaces;

import org.princeh.models.BaseContact;

public interface ContactObserver {
    void onContactUpdate(BaseContact contact, String updateType);
}
