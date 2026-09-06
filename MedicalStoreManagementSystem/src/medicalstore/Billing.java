package medicalstore;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Billing extends JFrame {

    JComboBox<MedicineItem> medicineBox;
    JTextField customerField, mobileField, priceField, quantityField, discountField;
    JLabel stockLabel, subtotalLabel, gstLabel, discountLabel, totalLabel;

    public Billing() {

        setTitle("MediCare Pharmacy - Billing");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 248, 250));

        // ================= HEADER =================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(13, 71, 76));
        header.setPreferredSize(new Dimension(950, 80));

        JLabel title = new JLabel("  MEDICARE PHARMACY");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitle = new JLabel("Professional Billing System  ");
        subtitle.setForeground(new Color(200, 230, 230));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        header.add(title, BorderLayout.WEST);
        header.add(subtitle, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // ================= CENTER =================

        JPanel center = new JPanel(new GridLayout(1, 2, 20, 0));
        center.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        center.setBackground(new Color(245, 248, 250));

        // ================= LEFT PANEL =================

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Color.WHITE);

        left.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 228)),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel billTitle = new JLabel("Create New Bill");
        billTitle.setFont(new Font("Segoe UI", Font.BOLD, 21));
        billTitle.setForeground(new Color(13, 71, 76));

        left.add(billTitle);
        left.add(Box.createVerticalStrut(20));

        customerField = createField();
        mobileField = createField();
        priceField = createField();
        quantityField = createField();
        discountField = createField();

        medicineBox = new JComboBox<>();
        medicineBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        medicineBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        stockLabel = new JLabel("Available Stock: -");
        stockLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        stockLabel.setForeground(new Color(46, 125, 50));

        addLabel(left, "Customer Name");
        left.add(customerField);
        left.add(Box.createVerticalStrut(12));

        addLabel(left, "Mobile Number");
        left.add(mobileField);
        left.add(Box.createVerticalStrut(12));

        addLabel(left, "Select Medicine");
        left.add(medicineBox);
        left.add(Box.createVerticalStrut(5));

        left.add(stockLabel);
        left.add(Box.createVerticalStrut(12));

        addLabel(left, "Price");

        priceField.setEditable(false);

        left.add(priceField);
        left.add(Box.createVerticalStrut(12));

        addLabel(left, "Quantity");
        left.add(quantityField);
        left.add(Box.createVerticalStrut(12));

        addLabel(left, "Discount");
        left.add(discountField);

        center.add(left);

        // ================= RIGHT PANEL =================

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(Color.WHITE);

        right.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 228)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel summaryTitle = new JLabel("Bill Summary");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 21));
        summaryTitle.setForeground(new Color(13, 71, 76));

        right.add(summaryTitle);
        right.add(Box.createVerticalStrut(35));

        subtotalLabel = createAmountLabel();
        gstLabel = createAmountLabel();
        discountLabel = createAmountLabel();
        totalLabel = createAmountLabel();

        addSummaryRow(right, "Subtotal", subtotalLabel);
        addSummaryRow(right, "GST (5%)", gstLabel);
        addSummaryRow(right, "Discount", discountLabel);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        right.add(separator);
        right.add(Box.createVerticalStrut(20));

        JLabel totalText = new JLabel("TOTAL AMOUNT");
        totalText.setFont(new Font("Segoe UI", Font.BOLD, 17));
        totalText.setForeground(new Color(13, 71, 76));

        right.add(totalText);
        right.add(Box.createVerticalStrut(5));

        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        totalLabel.setForeground(new Color(0, 137, 123));

        right.add(totalLabel);
        right.add(Box.createVerticalGlue());

        JButton calculateButton = createButton("Calculate Bill");
        JButton generateButton = createButton("Generate Bill");
        JButton clearButton = createButton("Clear");

        right.add(calculateButton);
        right.add(Box.createVerticalStrut(10));

        right.add(generateButton);
        right.add(Box.createVerticalStrut(10));

        right.add(clearButton);

        center.add(right);

        mainPanel.add(center, BorderLayout.CENTER);

        add(mainPanel);

        // ================= LOAD MEDICINES =================

        loadMedicines();

        // ================= MEDICINE SELECTION =================

        medicineBox.addActionListener(e -> medicineSelected());

        // ================= BUTTONS =================

        calculateButton.addActionListener(e -> calculateBill());

        generateButton.addActionListener(e -> generateBill());

        clearButton.addActionListener(e -> clearFields());

        setVisible(true);
    }

    // =========================================================
    // LOAD MEDICINES
    // =========================================================

    private void loadMedicines() {

        try (Connection con = DatabaseConnection.getConnection()) {

            String sql =
                    "SELECT id, name, price, quantity " +
                            "FROM medicine ORDER BY name";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            medicineBox.removeAllItems();

            while (rs.next()) {

                MedicineItem item = new MedicineItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );

                medicineBox.addItem(item);
            }

            rs.close();
            ps.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine loading error:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // MEDICINE SELECTED
    // =========================================================

    private void medicineSelected() {

        MedicineItem item =
                (MedicineItem) medicineBox.getSelectedItem();

        if (item != null) {

            priceField.setText(
                    String.format("%.2f", item.price)
            );

            stockLabel.setText(
                    "Available Stock: " + item.quantity
            );

            if (item.quantity <= 5) {

                stockLabel.setForeground(
                        new Color(230, 81, 0)
                );

            } else {

                stockLabel.setForeground(
                        new Color(46, 125, 50)
                );
            }
        }
    }

    // =========================================================
    // CALCULATE BILL
    // =========================================================

    private boolean calculateBill() {

        try {

            MedicineItem item =
                    (MedicineItem) medicineBox.getSelectedItem();

            if (item == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select medicine."
                );

                return false;
            }

            int quantity =
                    Integer.parseInt(
                            quantityField.getText().trim()
                    );

            double discount =
                    discountField.getText().trim().isEmpty()
                            ? 0
                            : Double.parseDouble(
                            discountField.getText().trim()
                    );

            if (quantity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity must be greater than 0."
                );

                return false;
            }

            if (quantity > item.quantity) {

                JOptionPane.showMessageDialog(
                        this,
                        "Only " + item.quantity +
                                " units are available in stock.",
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE
                );

                return false;
            }

            if (discount < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Discount cannot be negative."
                );

                return false;
            }

            double subtotal = item.price * quantity;

            double gst = subtotal * 0.05;

            double total = subtotal + gst - discount;

            if (total < 0) {
                total = 0;
            }

            subtotalLabel.setText(
                    "₹ " + String.format("%.2f", subtotal)
            );

            gstLabel.setText(
                    "₹ " + String.format("%.2f", gst)
            );

            discountLabel.setText(
                    "₹ " + String.format("%.2f", discount)
            );

            totalLabel.setText(
                    "₹ " + String.format("%.2f", total)
            );

            return true;

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid quantity and discount."
            );

            return false;
        }
    }

    // =========================================================
    // GENERATE BILL
    // =========================================================

    private void generateBill() {

        if (customerField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter customer name."
            );

            return;
        }

        if (mobileField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter mobile number."
            );

            return;
        }

        if (!calculateBill()) {
            return;
        }

        MedicineItem item =
                (MedicineItem) medicineBox.getSelectedItem();

        int quantity;

        double discount;

        try {

            quantity =
                    Integer.parseInt(
                            quantityField.getText().trim()
                    );

            discount =
                    discountField.getText().trim().isEmpty()
                            ? 0
                            : Double.parseDouble(
                            discountField.getText().trim()
                    );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid quantity and discount."
            );

            return;
        }

        double subtotal = item.price * quantity;

        double gst = subtotal * 0.05;

        double total = subtotal + gst - discount;

        if (total < 0) {
            total = 0;
        }

        Connection con = null;

        try {

            con = DatabaseConnection.getConnection();

            if (con == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Database connection failed."
                );

                return;
            }

            con.setAutoCommit(false);

            // =================================================
            // RE-CHECK STOCK
            // =================================================

            String checkSql =
                    "SELECT quantity, price, name " +
                            "FROM medicine WHERE id=? FOR UPDATE";

            PreparedStatement checkPs =
                    con.prepareStatement(checkSql);

            checkPs.setInt(1, item.id);

            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {

                con.rollback();

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine not found."
                );

                return;
            }

            int currentStock =
                    rs.getInt("quantity");

            double currentPrice =
                    rs.getDouble("price");

            String medicineName =
                    rs.getString("name");

            if (quantity > currentStock) {

                con.rollback();

                JOptionPane.showMessageDialog(
                        this,
                        "Stock changed!\nOnly " +
                                currentStock +
                                " units are available.",
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE
                );

                loadMedicines();

                return;
            }

            // =================================================
            // REDUCE STOCK
            // =================================================

            String updateSql =
                    "UPDATE medicine " +
                            "SET quantity = quantity - ? " +
                            "WHERE id=?";

            PreparedStatement updatePs =
                    con.prepareStatement(updateSql);

            updatePs.setInt(1, quantity);
            updatePs.setInt(2, item.id);

            updatePs.executeUpdate();

            updatePs.close();

            // =================================================
            // BILL NUMBER
            // =================================================

            String billNumber =
                    "BILL-" +
                            new SimpleDateFormat(
                                    "yyyyMMddHHmmss"
                            ).format(new Date());

            // =================================================
            // DATE
            // =================================================

            String date =
                    new SimpleDateFormat(
                            "dd-MM-yyyy HH:mm:ss"
                    ).format(new Date());

            // =================================================
            // SAVE BILL IN DATABASE
            // =================================================

            String billDate =
                    new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss"
                    ).format(new Date());

            String insertSql =
                    "INSERT INTO bills " +
                            "(bill_number, customer_name, mobile, " +
                            "medicine_name, price, quantity, subtotal, " +
                            "gst, discount, total, bill_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertPs =
                    con.prepareStatement(insertSql);

            insertPs.setString(
                    1,
                    billNumber
            );

            insertPs.setString(
                    2,
                    customerField.getText().trim()
            );

            insertPs.setString(
                    3,
                    mobileField.getText().trim()
            );

            insertPs.setString(
                    4,
                    medicineName
            );

            insertPs.setDouble(
                    5,
                    currentPrice
            );

            insertPs.setInt(
                    6,
                    quantity
            );

            insertPs.setDouble(
                    7,
                    subtotal
            );

            insertPs.setDouble(
                    8,
                    gst
            );

            insertPs.setDouble(
                    9,
                    discount
            );

            insertPs.setDouble(
                    10,
                    total
            );

            insertPs.setString(
                    11,
                    billDate
            );

            insertPs.executeUpdate();

            insertPs.close();

            // =================================================
            // COMMIT EVERYTHING
            // =================================================

            con.commit();

            // =================================================
            // BILL DISPLAY
            // =================================================

            String bill =
                    "========================================\n" +
                            "          MEDICARE PHARMACY\n" +
                            "        PROFESSIONAL INVOICE\n" +
                            "========================================\n\n" +

                            "Bill No.   : " + billNumber + "\n" +
                            "Date       : " + date + "\n" +

                            "Customer   : " +
                            customerField.getText().trim() + "\n" +

                            "Mobile     : " +
                            mobileField.getText().trim() + "\n\n" +

                            "----------------------------------------\n" +

                            "Medicine   : " + medicineName + "\n" +

                            "Price      : ₹ " +
                            String.format(
                                    "%.2f",
                                    currentPrice
                            ) + "\n" +

                            "Quantity   : " +
                            quantity + "\n" +

                            "----------------------------------------\n\n" +

                            "Subtotal   : ₹ " +
                            String.format(
                                    "%.2f",
                                    subtotal
                            ) + "\n" +

                            "GST (5%)   : ₹ " +
                            String.format(
                                    "%.2f",
                                    gst
                            ) + "\n" +

                            "Discount   : ₹ " +
                            String.format(
                                    "%.2f",
                                    discount
                            ) + "\n" +

                            "----------------------------------------\n" +

                            "TOTAL      : ₹ " +
                            String.format(
                                    "%.2f",
                                    total
                            ) + "\n" +

                            "========================================\n\n" +

                            "        Thank You! Visit Again.\n" +

                            "========================================";

            JTextArea billArea =
                    new JTextArea(bill);

            billArea.setFont(
                    new Font(
                            "Monospaced",
                            Font.PLAIN,
                            14
                    )
            );

            billArea.setEditable(false);

            billArea.setMargin(
                    new Insets(
                            15,
                            15,
                            15,
                            15
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(billArea),
                    "Bill Generated Successfully",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

            loadMedicines();

        } catch (Exception ex) {

            try {

                if (con != null) {
                    con.rollback();
                }

            } catch (Exception ignored) {
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Bill generation failed:\n" +
                            ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            try {

                if (con != null) {

                    con.setAutoCommit(true);

                    con.close();
                }

            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private void clearFields() {

        customerField.setText("");

        mobileField.setText("");

        quantityField.setText("");

        discountField.setText("");

        subtotalLabel.setText("₹ 0.00");

        gstLabel.setText("₹ 0.00");

        discountLabel.setText("₹ 0.00");

        totalLabel.setText("₹ 0.00");

        if (medicineBox.getItemCount() > 0) {

            medicineBox.setSelectedIndex(0);
        }
    }

    // =========================================================
    // CREATE FIELD
    // =========================================================

    private JTextField createField() {

        JTextField field = new JTextField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        210,
                                        215,
                                        218
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        return field;
    }

    // =========================================================
    // AMOUNT LABEL
    // =========================================================

    private JLabel createAmountLabel() {

        JLabel label =
                new JLabel("₹ 0.00");

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        label.setForeground(
                new Color(
                        70,
                        70,
                        70
                )
        );

        return label;
    }

    // =========================================================
    // ADD LABEL
    // =========================================================

    private void addLabel(
            JPanel panel,
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                new Color(
                        70,
                        70,
                        70
                )
        );

        panel.add(label);

        panel.add(
                Box.createVerticalStrut(5)
        );
    }

    // =========================================================
    // SUMMARY ROW
    // =========================================================

    private void addSummaryRow(
            JPanel panel,
            String name,
            JLabel value
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout()
                );

        row.setBackground(
                Color.WHITE
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        35
                )
        );

        JLabel label =
                new JLabel(name);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        row.add(
                label,
                BorderLayout.WEST
        );

        row.add(
                value,
                BorderLayout.EAST
        );

        panel.add(row);

        panel.add(
                Box.createVerticalStrut(15)
        );
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
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                new Color(
                        0,
                        121,
                        107
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        20,
                        12,
                        20
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
    // MEDICINE CLASS
    // =========================================================

    static class MedicineItem {

        int id;

        String name;

        double price;

        int quantity;

        MedicineItem(
                int id,
                String name,
                double price,
                int quantity
        ) {

            this.id = id;

            this.name = name;

            this.price = price;

            this.quantity = quantity;
        }

        @Override
        public String toString() {

            return name;
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                Billing::new
        );
    }
}