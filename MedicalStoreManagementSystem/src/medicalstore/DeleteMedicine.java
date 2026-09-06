package medicalstore;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DeleteMedicine extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField companyField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField expiryField;

    // ================= COLORS =================

    private final Color PRIMARY = new Color(220, 70, 70);
    private final Color DARK = new Color(13, 71, 76);
    private final Color BACKGROUND = new Color(245, 248, 250);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(35, 45, 55);

    public DeleteMedicine() {

        setTitle("MediCare Pharmacy - Delete Medicine");
        setSize(700, 650);
        setMinimumSize(new Dimension(650, 600));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BACKGROUND);

        setLayout(new BorderLayout());

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(DARK);

        header.setPreferredSize(new Dimension(700, 95));

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 30, 18, 30
                )
        );

        JPanel titlePanel = new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title = new JLabel("MEDICARE PHARMACY");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(Color.WHITE);

        JLabel subtitle =
                new JLabel("Delete Medicine");

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                new Color(190, 225, 220)
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

        JLabel icon = new JLabel("✕");

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        48
                )
        );

        icon.setForeground(
                new Color(255, 120, 120)
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
        // MAIN PANEL
        // =====================================================

        JPanel mainPanel = new JPanel();

        mainPanel.setBackground(BACKGROUND);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 50, 20, 50
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
                        new BorderLayout(12, 0)
                );

        idPanel.setBackground(WHITE);

        idPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 235)
                        ),
                        BorderFactory.createEmptyBorder(
                                15, 18, 15, 18
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
                new JLabel("Medicine ID:");

        idLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        idLabel.setForeground(TEXT);

        idField = createTextField();

        JButton loadButton =
                createButton("🔍  Load Medicine");

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
        // MEDICINE DETAILS PANEL
        // =====================================================

        JPanel detailsPanel =
                new JPanel(
                        new GridBagLayout()
                );

        detailsPanel.setBackground(WHITE);

        detailsPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 235)
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 25, 20, 25
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // NAME
        addFormRow(
                detailsPanel,
                gbc,
                0,
                "Medicine Name:",
                nameField = createTextField()
        );

        // COMPANY
        addFormRow(
                detailsPanel,
                gbc,
                1,
                "Company:",
                companyField = createTextField()
        );

        // PRICE
        addFormRow(
                detailsPanel,
                gbc,
                2,
                "Price (₹):",
                priceField = createTextField()
        );

        // QUANTITY
        addFormRow(
                detailsPanel,
                gbc,
                3,
                "Quantity:",
                quantityField = createTextField()
        );

        // EXPIRY
        addFormRow(
                detailsPanel,
                gbc,
                4,
                "Expiry Date:",
                expiryField = createTextField()
        );

        // Make fields read-only
        nameField.setEditable(false);
        companyField.setEditable(false);
        priceField.setEditable(false);
        quantityField.setEditable(false);
        expiryField.setEditable(false);

        mainPanel.add(detailsPanel);

        mainPanel.add(
                Box.createVerticalStrut(15)
        );

        // =====================================================
        // WARNING
        // =====================================================

        JPanel warningPanel =
                new JPanel(
                        new BorderLayout()
                );

        warningPanel.setBackground(
                new Color(255, 242, 242)
        );

        warningPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(245, 180, 180)
                        ),
                        BorderFactory.createEmptyBorder(
                                10, 15, 10, 15
                        )
                )
        );

        JLabel warning =
                new JLabel(
                        "⚠  Warning: Deleted medicine cannot be recovered."
                );

        warning.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        warning.setForeground(
                new Color(190, 50, 50)
        );

        warningPanel.add(
                warning,
                BorderLayout.CENTER
        );

        mainPanel.add(warningPanel);

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

        bottomPanel.setBackground(BACKGROUND);

        JButton deleteButton =
                createButton(
                        "🗑  Delete Medicine"
                );

        JButton clearButton =
                createButton(
                        "↻  Clear"
                );

        JButton closeButton =
                createButton(
                        "×  Close"
                );

        bottomPanel.add(deleteButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(closeButton);

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

        deleteButton.addActionListener(
                e -> deleteMedicine()
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
                        DatabaseConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(1, id);

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
                        rs.getDate("expiry_date");

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
                    "Database Error:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // DELETE MEDICINE
    // =========================================================

    private void deleteMedicine() {

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

        // =====================================================
        // CHECK MEDICINE EXISTS
        // =====================================================

        String checkSql =
                "SELECT name FROM medicine WHERE id=?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement checkPst =
                        con.prepareStatement(checkSql)
        ) {

            checkPst.setInt(1, id);

            ResultSet rs =
                    checkPst.executeQuery();

            if (!rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine ID not found.",
                        "Not Found",
                        JOptionPane.WARNING_MESSAGE
                );

                clearMedicineFields();

                rs.close();

                return;
            }

            String medicineName =
                    rs.getString("name");

            rs.close();

            // =================================================
            // CONFIRM DELETE
            // =================================================

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete:\n\n"
                                    + "Medicine: "
                                    + medicineName
                                    + "\nID: "
                                    + id
                                    + "\n\n"
                                    + "This action cannot be undone.",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (
                    confirm !=
                            JOptionPane.YES_OPTION
            ) {

                return;
            }

            // =================================================
            // DELETE
            // =================================================

            String deleteSql =
                    "DELETE FROM medicine WHERE id=?";

            try (
                    PreparedStatement deletePst =
                            con.prepareStatement(deleteSql)
            ) {

                deletePst.setInt(1, id);

                int result =
                        deletePst.executeUpdate();

                if (result > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "✓ Medicine Deleted Successfully!",
                            "Delete Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    clearFields();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Medicine could not be deleted.",
                            "Delete Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CLEAR
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
                                new Color(200, 210, 215)
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 10, 5, 10
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

        button.setForeground(Color.WHITE);

        button.setBackground(PRIMARY);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 16, 10, 16
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
                () -> new DeleteMedicine()
        );
    }
}

