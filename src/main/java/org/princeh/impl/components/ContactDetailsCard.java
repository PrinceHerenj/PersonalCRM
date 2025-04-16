package org.princeh.impl.components;

import org.princeh.models.BaseContact;
import org.princeh.models.MongoBusinessContact;
import org.princeh.models.MongoPersonalContact;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContactDetailsCard extends JDialog {
    public ContactDetailsCard(JFrame parent, BaseContact contact) {
        super(parent, "Contact Details", true);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel nameLabel = new JLabel(contact.getFirstName() + " " + contact.getLastName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 18));
        headerPanel.add(nameLabel, BorderLayout.WEST);

        JLabel typeLabel = new JLabel(contact.getContactType());
        typeLabel.setFont(typeLabel.getFont().deriveFont(Font.BOLD, 14));
        headerPanel.add(typeLabel, BorderLayout.EAST);

        contentPanel.add(headerPanel);
        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel basicInfoPanel = new JPanel(new GridLayout(0, 2, 5, 8));
        basicInfoPanel.setBorder(BorderFactory.createTitledBorder("Basic Info"));

        addField(basicInfoPanel, "Email:", contact.getEmail());
        addField(basicInfoPanel, "Contact Method:", contact.getContactMethod());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        addField(basicInfoPanel, "Last Contacted:", dateFormat.format(new Date(contact.getLastContactedDate())));

        contentPanel.add(basicInfoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        if (contact instanceof MongoPersonalContact personalContact) {
            JPanel personalPanel = new JPanel(new GridLayout(0, 2, 5, 8));
            personalPanel.setBorder(BorderFactory.createTitledBorder("Personal Info"));

            addField(personalPanel, "Phone Number:", personalContact.getPhoneNumber());
            addField(personalPanel, "Address:", personalContact.getAddress());

            contentPanel.add(personalPanel);
        } else if (contact instanceof MongoBusinessContact businessContact) {
            JPanel businessPanel = new JPanel(new GridLayout(0, 2, 5, 8));
            businessPanel.setBorder(BorderFactory.createTitledBorder("Business Info"));

            addField(businessPanel, "Company:", businessContact.getCompanyName());
            addField(businessPanel, "Job Title:",businessContact.getJobTitle());
            addField(businessPanel, "Work Phone:",businessContact.getWorkPhone());
            contentPanel.add(businessPanel);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(_ -> dispose());
        buttonPanel.add(closeButton);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(buttonPanel);

        add(contentPanel);
        pack();
        setSize(400, getHeight());
        setLocationRelativeTo(parent);
    }

    private void addField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label, SwingConstants.RIGHT));

        JLabel valueLabel = new JLabel(value != null && !value.isEmpty() ? value : "N/A");
        valueLabel.setForeground(new Color(50,50, 50));
        panel.add(valueLabel);
    }
}
