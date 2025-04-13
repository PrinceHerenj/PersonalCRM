package org.princeh.interfaces;

import org.princeh.models.BaseContact;

public interface Observer {
    void onContactUpdate(BaseContact contact, String updateType);
}
