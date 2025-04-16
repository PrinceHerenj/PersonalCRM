package org.princeh.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.princeh.controller.MongoDBManager;
import org.princeh.interfaces.ContactDAO;
import org.princeh.models.BaseContact;
import org.princeh.models.MongoBaseContact;
import org.princeh.models.MongoBusinessContact;
import org.princeh.models.MongoPersonalContact;

import java.util.ArrayList;
import java.util.List;

public class MongoContactDAO implements ContactDAO {
    private final MongoCollection<Document> contactCollection;

    public MongoContactDAO(MongoDBManager mongoDBManager) {
        contactCollection = mongoDBManager.getDatabase().getCollection("contacts");
    }

    @Override
    public void saveContact(BaseContact contact) {
        Document doc = contactToDocument(contact);
        contactCollection.insertOne(doc);

        String id = doc.getObjectId("_id").toString();
        if (contact instanceof MongoBaseContact) {
            ((MongoBaseContact) contact).setId(id);
        }
    }

    @Override
    public void updateContact(BaseContact contact) {
        if (!(contact instanceof MongoBaseContact)) {
            throw new IllegalArgumentException("Contact must be a MongoBaseContact");
        }

        String id = ((MongoBaseContact) contact).getId();

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Contact id cannot be null or empty");
        }
        Document doc = contactToDocument(contact);
        contactCollection.replaceOne(
                Filters.eq("_id", new ObjectId(id)),
                doc
        );
    }

    @Override
    public void deleteContact(String id) {
        contactCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    @Override
    public BaseContact getContactById(String id) {
        Document doc = contactCollection.find(Filters.eq("_id", new ObjectId(id))).first();
        return documentToContact(doc);
    }

    @Override
    public List<BaseContact> getAllContacts() {
        List<BaseContact> contacts = new ArrayList<>();
        try (MongoCursor<Document> cursor = contactCollection.find().iterator()) {
            while (cursor.hasNext()) {
                BaseContact contact = documentToContact(cursor.next());
                if (contact != null) {
                    contacts.add(contact);
                }
            }
        }
        return contacts;
    }

    @Override
    public List<BaseContact> searchContactsByName(String name) {
        List<BaseContact> contacts = new ArrayList<>();

        Document regexQuery = new Document()
                .append("$regex", name)
                .append("$options", "i");

        Document query = new Document().append("$or", List.of(
                new Document("firstName", regexQuery),
                new Document("lastName", regexQuery)
        ));

        try (MongoCursor<Document> cursor = contactCollection.find(query).iterator()) {
            while (cursor.hasNext()) {
                BaseContact contact = documentToContact(cursor.next());
                if (contact != null) {
                    contacts.add(contact);
                }
            }
        }
        return contacts;
    }

    @Override
    public List<BaseContact> searchContactsByEmail(String email) {
        List<BaseContact> contacts = new ArrayList<>();

        Document regexQuery = new Document()
                .append("$regex", email)
                .append("$options", "i");

        try (MongoCursor<Document> cursor = contactCollection.find(new Document("email", regexQuery)).iterator()) {
            while(cursor.hasNext()) {
                BaseContact contact = documentToContact(cursor.next());
                if (contact != null) {
                    contacts.add(contact);
                }
            }
        }

        return contacts;
    }

    @Override
    public List<BaseContact> searchContactsByDateRange(long startDate, long endDate) {
        List<BaseContact> contacts = new ArrayList<>();

        try (MongoCursor<Document> cursor = contactCollection.find(
                Filters.and(
                        Filters.gte("lastContactedDate", startDate),
                        Filters.lte("lastContactedDate", endDate)
                )).iterator()) {
            while (cursor.hasNext()) {
                BaseContact contact = documentToContact(cursor.next());
                if (contact != null) {
                    contacts.add(contact);
                }
            }
        }
        return contacts;
    }

    private Document contactToDocument(BaseContact contact) {
        Document doc = new Document()
                .append("firstName", contact.getFirstName())
                .append("lastName", contact.getLastName())
                .append("email", contact.getEmail())
                .append("lastContactedDate", contact.getLastContactedDate());

        if (contact instanceof MongoBaseContact && ((MongoBaseContact) contact).getId() != null) {
            String id = ((MongoBaseContact) contact).getId();
            if (!id.isEmpty())
                doc.append("_id", new ObjectId(id));
        }

        if (contact instanceof MongoPersonalContact personalContact) {
            doc.append("type", "Personal")
                    .append("phoneNumber", personalContact.getPhoneNumber())
                    .append("address", personalContact.getAddress())
                    .append("birthday", personalContact.getBirthday());
        } else if (contact instanceof MongoBusinessContact businessContact) {
            doc.append("type", "Business")
                    .append("companyName", businessContact.getCompanyName())
                    .append("jobTitle", businessContact.getJobTitle())
                    .append("workPhone", businessContact.getWorkPhone());
        }

        return doc;
    }

    private BaseContact documentToContact(Document document) {
        if (document == null) {
            return null;
        }
        String id = document.getObjectId("_id").toString();
        String firstName = document.getString("firstName");
        String lastName = document.getString("lastName");
        String email = document.getString("email");
        long lastContactedDate = document.getLong("lastContactedDate");
        String type = document.getString("type");

        BaseContact contact = null;

        if ("Personal".equals(type)) {
            String phoneNumber = document.getString("phoneNumber");
            String address = document.getString("address");
            String birthday = document.getString("birthday");

            MongoPersonalContact personalContact = new MongoPersonalContact(
                    firstName, lastName, email, phoneNumber, address);
            personalContact.setId(id);
            personalContact.setBirthday(birthday);
            contact = personalContact;
        } else if ("Business".equals(type)) {
            String companyName = document.getString("companyName");
            String jobTitle = document.getString("jobTitle");
            String workPhone = document.getString("workPhone");

            MongoBusinessContact businessContact = new MongoBusinessContact(
                    firstName, lastName, email, companyName, jobTitle, workPhone);
            businessContact.setId(id);
            contact = businessContact;
        }

        if (contact != null) {
            ((MongoBaseContact) contact).setLastContactedDate(lastContactedDate);
        }

        return contact;
    }
}
