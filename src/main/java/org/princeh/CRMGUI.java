package org.princeh;

import org.princeh.models.BaseContact;
import org.princeh.models.MongoBusinessContact;
import org.princeh.models.MongoPersonalContact;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CRMGUI extends JFrame {
    private CRMSystem crmSystem;
    private final JTable contactTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField, startDateField, endDateField;
    private final JButton addButton, searchButton, updateButton, deleteButton;
    private final JComboBox<String> contactTypeCombo, searchTypeCombo;
    private final JLabel startDateLabel =  new JLabel("Start Date:"), endDateLabel = new JLabel("End Date:");

    private static final String MONGODB_CONNECTION_STRING =
            "mongodb+srv://princeherenj:Sh353478@cluster0.7xs6q3p.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

    public CRMGUI() {
        try {
            crmSystem = CRMSystem.getInstance(MONGODB_CONNECTION_STRING);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to MongoDB: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Personal CRM System");
        setSize(1300, 731);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        contactTypeCombo = new JComboBox<>(new String[] {"All", "Personal", "Business"});

        searchTypeCombo = new JComboBox<>(new String[] {"Name/Email", "Date-Range"});
        startDateField = new JTextField(20);
        endDateField = new JTextField(20);

        startDateField.setVisible(false);
        endDateField.setVisible(false);
        startDateLabel.setVisible(false);
        endDateLabel.setVisible(false);

        searchTypeCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    boolean isDateRange = "Date-Range".equals(e.getItem());
                    startDateLabel.setVisible(isDateRange);
                    startDateField.setVisible(isDateRange);
                    endDateLabel.setVisible(isDateRange);
                    endDateField.setVisible(isDateRange);
                    topPanel.revalidate();
                    topPanel.repaint();
                }
            }
        });

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(contactTypeCombo);
        topPanel.add(searchTypeCombo);
        topPanel.add(searchButton);
        topPanel.add(startDateLabel);
        topPanel.add(startDateField);
        topPanel.add(endDateLabel);
        topPanel.add(endDateField);

        String[] columnNames = {"Type", "Firstname", "Lastname", "Email", "Contact", "Last Contacted"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);

        addButton = new JButton("Add Contact");
        updateButton = new JButton("Update Last Contacted");
        deleteButton = new JButton("Delete Contact");

        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setupEventHandlers();

        refreshTableData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupEventHandlers() {
        searchButton.addActionListener(_ -> {
            String searchTerm = searchField.getText().trim();
            String contactType = (String) contactTypeCombo.getSelectedItem();
            String contactMethod = (String) searchTypeCombo.getSelectedItem();
            if (contactMethod != null && contactMethod.equals("Name/Email"))
                performSearch(searchTerm, contactType);
            else {
                long startDate = Long.parseLong(startDateField.getText().trim());
                long endDate = Long.parseLong(endDateField.getText().trim());
                performSearch(searchTerm, contactType, startDate, endDate);
            }
        });

        addButton.addActionListener(_ -> showAddContactDialog());

        updateButton.addActionListener(_ -> {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow >= 0) {
                updateSelectedContact(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a contact to update",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(_ -> {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow >= 0) {
                deleteSelectedContact(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a contact to delete",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);

        List<BaseContact> contacts = crmSystem.getContactManager().getAllContacts();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (BaseContact contact : contacts) {
            tableModel.addRow(new Object[]{
                    contact.getContactType(),
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getEmail(),
                    contact.getContactMethod(),
                    dateFormat.format(new Date(contact.getLastContactedDate()))
            });
        }
    }
    private void performSearch(String searchTerm, String contactType, long startDate, long endDate) {
        tableModel.setRowCount(0);

        BaseContact[] results = null;
        if (searchTerm.isEmpty()) results = crmSystem.getContactManager().getAllContacts().toArray(new BaseContact[0]);
        else  {
            results = crmSystem.getContactManager().searchByDateRange(startDate, endDate);
        }

        fillContacts(contactType, results);
    }

    private void fillContacts(String contactType, BaseContact[] results) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        assert results != null;
        for (BaseContact contact : results) {
            if (contactType.equals("All") || contact.getContactType().equals(contactType)) {
                tableModel.addRow(new Object[]{
                        contact.getContactType(),
                        contact.getFirstName(),
                        contact.getLastName(),
                        contact.getEmail(),
                        contact.getContactMethod(),
                        dateFormat.format(new Date(contact.getLastContactedDate()))
                });
            }
        }
    }


    private void performSearch(String searchTerm, String contactType) {
        tableModel.setRowCount(0);

        BaseContact[] results = null;
        if (searchTerm.isEmpty()) {
            results = crmSystem.getContactManager().getAllContacts().toArray(new BaseContact[0]);
        } else {
            results = crmSystem.getContactManager().searchByName(searchTerm);
            if (results.length == 0)
                results = crmSystem.getContactManager().searchByEmail(searchTerm);
        }

        fillContacts(contactType, results);
    }

    private void showAddContactDialog() {
        JDialog dialog = new JDialog(this, "Add New Contact" ,true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> typeCombo = new JComboBox<>(new String[] {"Personal", "Business"});
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);

        JTextField phoneField = new JTextField(20);
        JTextField addressField = new JTextField(20);

        JTextField companyField = new JTextField(20);
        JTextField jobTitleField = new JTextField(20);
        JTextField workPhoneField = new JTextField(20);

        formPanel.add(new JLabel("Contact Type:"));
        formPanel.add(typeCombo);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        JPanel personalPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        personalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        personalPanel.add(new JLabel("Phone Number:"));
        personalPanel.add(phoneField);
        personalPanel.add(new JLabel("Address:"));
        personalPanel.add(addressField);

        JPanel businessPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        businessPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        businessPanel.add(new JLabel("Company:"));
        businessPanel.add(companyField);
        businessPanel.add(new JLabel("Job Title:"));
        businessPanel.add(jobTitleField);
        businessPanel.add(new JLabel("Work Phone:"));
        businessPanel.add(workPhoneField);
        businessPanel.setVisible(false);

        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.add(personalPanel, "Personal");
        cardPanel.add(businessPanel, "Business");

        typeCombo.addActionListener(_ -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, (String) typeCombo.getSelectedItem());
        });

        JPanel buttomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttomPanel.add(saveButton);
        buttomPanel.add(cancelButton);

        saveButton.addActionListener(_ -> {
            String type = (String) typeCombo.getSelectedItem();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Firstname, Lastname and email are required", "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            assert type != null;
            if (type.equals("Personal")) {
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                MongoPersonalContact contact = new MongoPersonalContact(firstName, lastName, email, phone, address);
                crmSystem.getContactManager().addContact(contact);
            } else {
                String company = companyField.getText().trim();
                String jobTitle = jobTitleField.getText().trim();
                String workPhone = workPhoneField.getText().trim();
                MongoBusinessContact contact = new MongoBusinessContact(firstName, lastName, email, company, jobTitle, workPhone);
                crmSystem.getContactManager().addContact(contact);
            }

            refreshTableData();
            dialog.dispose();
        });

        cancelButton.addActionListener(_ -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.NORTH);
        dialog.add(cardPanel, BorderLayout.CENTER);
        dialog.add(buttomPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateSelectedContact(int selectedRow) {
        String type = (String) tableModel.getValueAt(selectedRow, 0);
        String firstName = (String) tableModel.getValueAt(selectedRow, 1);
        String lastName = (String) tableModel.getValueAt(selectedRow, 2);
        String email = (String) tableModel.getValueAt(selectedRow, 3);

        List<BaseContact> contacts = crmSystem.getContactManager().getAllContacts();
        for (BaseContact contact : contacts) {
            if (contact.getFirstName().equals(firstName) && contact.getLastName().equals(lastName) && contact.getEmail().equals(email)) {
                crmSystem.getContactManager().updateLastContactedDate(contact);

                refreshTableData();

                JOptionPane.showMessageDialog(this,
                        "Updated last contacted date for "+ firstName + " " + lastName,
                "Contact Updated", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void deleteSelectedContact(int selectedRow) {
        String type = (String) tableModel.getValueAt(selectedRow, 0);
        String firstName = (String) tableModel.getValueAt(selectedRow, 1);
        String lastName = (String) tableModel.getValueAt(selectedRow, 2);
        String email = (String) tableModel.getValueAt(selectedRow, 3);

        int result = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete " + firstName + " " + lastName + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            List<BaseContact> contacts = crmSystem.getContactManager().getAllContacts();
            for (BaseContact contact : contacts) {
                if (contact.getFirstName().equals(firstName) && contact.getLastName().equals(lastName)) {

                    boolean removed = crmSystem.getContactManager().removeContact(contact);

                    if (removed) {
                        refreshTableData();
                        JOptionPane.showMessageDialog(this,
                                "Contact deleted succesfully",
                                "Contact Deleted", JOptionPane.INFORMATION_MESSAGE);
                    }
                    return;
                }
            }
        }
    }



    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(CRMGUI::new);
    }
}
