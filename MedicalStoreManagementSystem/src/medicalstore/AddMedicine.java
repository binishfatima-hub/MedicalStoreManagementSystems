package medicalstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;

public class AddMedicine extends JFrame {

    JTextField nameField;
    JTextField companyField;
    JTextField priceField;
    JTextField quantityField;
    JTextField expiryField;

    // Colors
    Color dark = new Color(20, 47, 65);
    Color primary = new Color(19, 150, 137);
    Color background = new Color(245, 248, 250);
    Color textColor = new Color(40, 50, 60);

    AddMedicine() {

        setTitle("MediCare Pharmacy - Add Medicine");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(background);
        setLayout(new BorderLayout());

        // =====================================================
        // LEFT PANEL
        // =====================================================

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(dark);
        leftPanel.setPreferredSize(new Dimension(250, 600));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(
                BorderFactory.createEmptyBorder(45, 25, 30, 25)
        );

        JLabel logo1 = new JLabel("MEDI");
        logo1.setFont(new Font("Arial", Font.BOLD, 32));
        logo1.setForeground(Color.WHITE);
        logo1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logo2 = new JLabel("CARE");
        logo2.setFont(new Font("Arial", Font.BOLD, 32));
        logo2.setForeground(new Color(57, 210, 190));
        logo2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pharmacy = new JLabel("PHARMACY");
        pharmacy.setFont(new Font("Arial", Font.BOLD, 13));
        pharmacy.setForeground(new Color(190, 205, 215));
        pharmacy.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(logo1);
        leftPanel.add(logo2);
        leftPanel.add(pharmacy);

        leftPanel.add(Box.createVerticalStrut(60));

        JLabel icon = new JLabel("✚");
        icon.setFont(new Font("Arial", Font.BOLD, 65));
        icon.setForeground(new Color(57, 210, 190));
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(icon);

        leftPanel.add(Box.createVerticalStrut(20));

        JLabel heading = new JLabel(
                "<html><div style='width:180px'>Add medicines to your inventory easily.</div></html>"
        );

        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(heading);

        leftPanel.add(Box.createVerticalStrut(15));

        JLabel description = new JLabel(
                "<html><div style='width:180px'>"
                        + "Enter accurate medicine details including price, quantity and expiry date."
                        + "</div></html>"
        );

        description.setFont(new Font("Arial", Font.PLAIN, 13));
        description.setForeground(new Color(180, 195, 205));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(description);

        add(leftPanel, BorderLayout.WEST);

        // =====================================================
        // RIGHT PANEL
        // =====================================================

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(background);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(35, 40, 30, 40)
        );

        // =====================================================
        // TOP
        // =====================================================

        JPanel topPanel = new JPanel();
        topPanel.setBackground(background);
        topPanel.setLayout(
                new BoxLayout(topPanel, BoxLayout.Y_AXIS)
        );

        JLabel title = new JLabel("Add New Medicine");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(textColor);

        JLabel subtitle = new JLabel(
                "Add medicine details to your pharmacy inventory"
        );

        subtitle.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        subtitle.setForeground(
                new Color(120, 130, 140)
        );

        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(subtitle);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // =====================================================
        // FORM
        // =====================================================

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 235)
                        ),
                        BorderFactory.createEmptyBorder(
                                25, 30, 25, 30
                        )
                )
        );

        formPanel.setLayout(
                new GridLayout(5, 2, 20, 15)
        );

        // Medicine Name
        JLabel nameLabel = createLabel("Medicine Name");
        nameField = createField("Enter medicine name");

        // Company
        JLabel companyLabel = createLabel("Company");
        companyField = createField("Enter company name");

        // Price
        JLabel priceLabel = createLabel("Price (₹)");
        priceField = createField("Enter price");

        // Quantity
        JLabel quantityLabel = createLabel("Quantity");
        quantityField = createField("Enter quantity");

        // Expiry
        JLabel expiryLabel = createLabel("Expiry Date");
        expiryField = createField("YYYY-MM-DD");

        expiryField.setToolTipText(
                "Example: 2027-12-31"
        );

        formPanel.add(nameLabel);
        formPanel.add(nameField);

        formPanel.add(companyLabel);
        formPanel.add(companyField);

        formPanel.add(priceLabel);
        formPanel.add(priceField);

        formPanel.add(quantityLabel);
        formPanel.add(quantityField);

        formPanel.add(expiryLabel);
        formPanel.add(expiryField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel bottomPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        12,
                        15
                )
        );

        bottomPanel.setBackground(background);

        JButton clearButton = new JButton("Clear");

        styleButton(
                clearButton,
                new Color(235, 238, 240),
                textColor
        );

        JButton addButton = new JButton(
                "＋  Add Medicine"
        );

        styleButton(
                addButton,
                primary,
                Color.WHITE
        );

        bottomPanel.add(clearButton);
        bottomPanel.add(addButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel, BorderLayout.CENTER);

        // =====================================================
        // ADD MEDICINE ACTION
        // =====================================================

        addButton.addActionListener(e -> {

            String name =
                    nameField.getText().trim();

            String company =
                    companyField.getText().trim();

            String priceText =
                    priceField.getText().trim();

            String quantityText =
                    quantityField.getText().trim();

            String expiryText =
                    expiryField.getText().trim();

            // Empty fields
            if (name.isEmpty()
                    || company.isEmpty()
                    || priceText.isEmpty()
                    || quantityText.isEmpty()
                    || expiryText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter all medicine details!",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            try {

                double price =
                        Double.parseDouble(priceText);

                int quantity =
                        Integer.parseInt(quantityText);

                // Positive values
                if (price < 0 || quantity < 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Price and Quantity cannot be negative!",
                            "Invalid Input",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                // Date
                Date expiryDate =
                        Date.valueOf(expiryText);

                // Database
                Connection con =
                        DatabaseConnection.getConnection();

                if (con == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Database Connection Failed!",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                // INSERT
                String sql =
                        "INSERT INTO medicine "
                                + "(name, company, price, quantity, expiry_date) "
                                + "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, company);
                ps.setDouble(3, price);
                ps.setInt(4, quantity);
                ps.setDate(5, expiryDate);

                int rows =
                        ps.executeUpdate();

                if (rows > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "✓ Medicine Added Successfully!\n\n"
                                    + "Medicine : " + name
                                    + "\nCompany  : " + company
                                    + "\nPrice    : ₹" + price
                                    + "\nQuantity : " + quantity
                                    + "\nExpiry   : " + expiryDate,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    clearFields();
                }

                ps.close();
                con.close();

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Price must be a number and Quantity must be a whole number!",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Expiry Date must be in this format:\n\n"
                                + "YYYY-MM-DD\n"
                                + "Example: 2027-12-31",
                        "Invalid Date",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Database Error:\n"
                                + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // =====================================================
        // CLEAR
        // =====================================================

        clearButton.addActionListener(
                e -> clearFields()
        );

        setVisible(true);
    }

    // =========================================================
    // CREATE LABEL
    // =========================================================

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(textColor);

        return label;
    }

    // =========================================================
    // CREATE FIELD
    // =========================================================

    private JTextField createField(
            String tooltip
    ) {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(textColor);

        field.setBackground(
                new Color(250, 252, 253)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 218, 223)
                        ),
                        BorderFactory.createEmptyBorder(
                                8, 10, 8, 10
                        )
                )
        );

        field.setToolTipText(tooltip);

        return field;
    }

    // =========================================================
    // BUTTON STYLE
    // =========================================================

    private void styleButton(
            JButton button,
            Color bg,
            Color fg
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setBackground(bg);
        button.setForeground(fg);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 22, 12, 22
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private void clearFields() {

        nameField.setText("");
        companyField.setText("");
        priceField.setText("");
        quantityField.setText("");
        expiryField.setText("");

        nameField.requestFocus();
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new AddMedicine()
        );
    }
}