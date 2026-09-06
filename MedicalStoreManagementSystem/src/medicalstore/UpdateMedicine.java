package medicalstore;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateMedicine extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField companyField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField expiryField;

    // ================= COLORS =================

    private final Color PRIMARY = new Color(19, 150, 137);
    private final Color DARK = new Color(13, 71, 76);
    private final Color BACKGROUND = new Color(245, 248, 250);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(35, 45, 55);

    public UpdateMedicine() {

        setTitle(
                "MediCare Pharmacy - Update Medicine"
        );

        setSize(700, 650);

        setMinimumSize(
                new Dimension(650, 600)
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        getContentPane().setBackground(
                BACKGROUND
        );

        setLayout(
                new BorderLayout()
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(DARK);

        header.setPreferredSize(
                new Dimension(
                        700,
                        95
                )
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        30,
                        18,
                        30
                )
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "MEDICARE PHARMACY"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(
                Color.WHITE
        );

        JLabel subtitle =
                new JLabel(
                        "Update Medicine Information"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                new Color(
                        190,
                        225,
                        220
                )
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JLabel icon =
                new JLabel("✎");

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        50
                )
        );

        icon.setForeground(
                new Color(
                        57,
                        210,
                        190
                )
        );

        header.add(
                icon,
                BorderLayout.EAST
        );

        add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN FORM
        // =====================================================

        JPanel mainPanel =
                new JPanel();

        mainPanel.setBackground(
                BACKGROUND
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        50,
                        20,
                        50
                )
        );

        mainPanel.setLayout(
                new BoxLayout(
                        mainPanel,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // ID SEARCH PANEL
        // =====================================================

        JPanel idPanel =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        idPanel.setBackground(WHITE);

        idPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        230,
                                        235
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                18,
                                15,
                                18
                        )
                )
        );

        idPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        75
                )
        );

        JLabel idLabel =
                new JLabel(
                        "Medicine ID:"
                );

        idLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        idLabel.setForeground(TEXT);

        idField =
                createTextField();

        JButton loadButton =
                createButton(
                        "🔍  Load Medicine"
                );

        idPanel.add(
                idLabel,
                BorderLayout.WEST
        );

        idPanel.add(
                idField,
                BorderLayout.CENTER
        );

        idPanel.add(
                loadButton,
                BorderLayout.EAST
        );

        mainPanel.add(idPanel);

        mainPanel.add(
                Box.createVerticalStrut(18)
        );

        // =====================================================
        // FORM PANEL
        // =====================================================

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBackground(WHITE);

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        230,
                                        235
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // NAME

        addFormRow(
                formPanel,
                gbc,
                0,
                "Medicine Name:",
                nameField =
                        createTextField()
        );

        // COMPANY

        addFormRow(
                formPanel,
                gbc,
                1,
                "Company:",
                companyField =
                        createTextField()
        );

        // PRICE

        addFormRow(
                formPanel,
                gbc,
                2,
                "Price (₹):",
                priceField =
                        createTextField()
        );

        // QUANTITY

        addFormRow(
                formPanel,
                gbc,
                3,
                "Quantity:",
                quantityField =
                        createTextField()
        );

        // EXPIRY

        addFormRow(
                formPanel,
                gbc,
                4,
                "Expiry Date:",
                expiryField =
                        createTextField()
        );

        mainPanel.add(
                formPanel
        );

        mainPanel.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // DATE FORMAT INFO
        // =====================================================

        JLabel dateInfo =
                new JLabel(
                        "Expiry Date format: YYYY-MM-DD   (Example: 2027-12-31)"
                );

        dateInfo.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        dateInfo.setForeground(
                new Color(
                        120,
                        130,
                        140
                )
        );

        dateInfo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(
                dateInfo
        );

        add(
                mainPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM BUTTONS
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                15
                        )
                );

        bottomPanel.setBackground(
                BACKGROUND
        );

        JButton updateButton =
                createButton(
                        "✓  Update Medicine"
                );

        JButton clearButton =
                createButton(
                        "↻  Clear"
                );

        JButton closeButton =
                createButton(
                        "×  Close"
                );

        bottomPanel.add(
                updateButton
        );

        bottomPanel.add(
                clearButton
        );

        bottomPanel.add(
                closeButton
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        loadButton.addActionListener(
                e -> loadMedicine()
        );

        idField.addActionListener(
                e -> loadMedicine()
        );

        updateButton.addActionListener(
                e -> updateMedicine()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        setVisible(true);
    }

    // =========================================================
    // ADD FORM ROW
    // =========================================================

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            JTextField field
    ) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;

        JLabel label =
                new JLabel(labelText);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(TEXT);

        panel.add(
                label,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 0.7;

        panel.add(
                field,
                gbc
        );
    }

    // =========================================================
    // LOAD MEDICINE
    // =========================================================

    private void loadMedicine() {

        String idText =
                idField.getText().trim();

        if (idText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter Medicine ID.",
                    "Missing ID",
                    JOptionPane.WARNING_MESSAGE
            );

            idField.requestFocus();

            return;
        }

        int id;

        try {

            id = Integer.parseInt(idText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine ID must be a number.",
                    "Invalid ID",
                    JOptionPane.WARNING_MESSAGE
            );

            idField.requestFocus();

            return;
        }

        String sql =
                "SELECT name, company, price, quantity, " +
                        "expiry_date FROM medicine WHERE id=?";

        try (
                Connection con =
                        DatabaseConnection
                                .getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    id
            );

            ResultSet rs =
                    pst.executeQuery();

            if (rs.next()) {

                nameField.setText(
                        rs.getString("name")
                );

                companyField.setText(
                        rs.getString("company")
                );

                priceField.setText(
                        String.valueOf(
                                rs.getDouble("price")
                        )
                );

                quantityField.setText(
                        String.valueOf(
                                rs.getInt("quantity")
                        )
                );

                Date expiry =
                        rs.getDate(
                                "expiry_date"
                        );

                if (expiry != null) {

                    expiryField.setText(
                            expiry.toString()
                    );

                } else {

                    expiryField.setText("");
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine details loaded successfully!",
                        "Medicine Found",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine ID not found.",
                        "Not Found",
                        JOptionPane.WARNING_MESSAGE
                );

                clearMedicineFields();
            }

            rs.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // UPDATE MEDICINE
    // =========================================================

    private void updateMedicine() {

        // CHECK ID

        String idText =
                idField.getText().trim();

        if (idText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter Medicine ID.",
                    "Missing ID",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id;

        try {

            id =
                    Integer.parseInt(
                            idText
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine ID must be a number.",
                    "Invalid ID",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // CHECK NAME

        String name =
                nameField.getText().trim();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter medicine name.",
                    "Missing Name",
                    JOptionPane.WARNING_MESSAGE
            );

            nameField.requestFocus();

            return;
        }

        // CHECK COMPANY

        String company =
                companyField.getText().trim();

        if (company.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter company name.",
                    "Missing Company",
                    JOptionPane.WARNING_MESSAGE
            );

            companyField.requestFocus();

            return;
        }

        // CHECK PRICE

        double price;

        try {

            price =
                    Double.parseDouble(
                            priceField
                                    .getText()
                                    .trim()
                    );

            if (price < 0) {

                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid price.",
                    "Invalid Price",
                    JOptionPane.WARNING_MESSAGE
            );

            priceField.requestFocus();

            return;
        }

        // CHECK QUANTITY

        int quantity;

        try {

            quantity =
                    Integer.parseInt(
                            quantityField
                                    .getText()
                                    .trim()
                    );

            if (quantity < 0) {

                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid quantity.",
                    "Invalid Quantity",
                    JOptionPane.WARNING_MESSAGE
            );

            quantityField.requestFocus();

            return;
        }

        // CHECK EXPIRY

        String expiry =
                expiryField.getText().trim();

        Date expiryDate;

        try {

            expiryDate =
                    Date.valueOf(
                            expiry
                    );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid expiry date.\n\n" +
                            "Use format: YYYY-MM-DD\n" +
                            "Example: 2027-12-31",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            expiryField.requestFocus();

            return;
        }

        // CONFIRMATION

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to update this medicine?",
                        "Confirm Update",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                confirm !=
                        JOptionPane.YES_OPTION
        ) {

            return;
        }

        // DATABASE UPDATE

        String sql =
                "UPDATE medicine SET " +
                        "name=?, " +
                        "company=?, " +
                        "price=?, " +
                        "quantity=?, " +
                        "expiry_date=? " +
                        "WHERE id=?";

        try (
                Connection con =
                        DatabaseConnection
                                .getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setString(
                    1,
                    name
            );

            pst.setString(
                    2,
                    company
            );

            pst.setDouble(
                    3,
                    price
            );

            pst.setInt(
                    4,
                    quantity
            );

            pst.setDate(
                    5,
                    expiryDate
            );

            pst.setInt(
                    6,
                    id
            );

            int result =
                    pst.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "✓ Medicine Updated Successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine ID not found.",
                        "Update Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private void clearFields() {

        idField.setText("");

        clearMedicineFields();

        idField.requestFocus();
    }

    private void clearMedicineFields() {

        nameField.setText("");

        companyField.setText("");

        priceField.setText("");

        quantityField.setText("");

        expiryField.setText("");
    }

    // =========================================================
    // CREATE TEXT FIELD
    // =========================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        field.setPreferredSize(
                new Dimension(
                        250,
                        38
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        200,
                                        210,
                                        215
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        return field;
    }

    // =========================================================
    // CREATE BUTTON
    // =========================================================

    private JButton createButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                PRIMARY
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        16,
                        10,
                        16
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new UpdateMedicine()
        );
    }
}