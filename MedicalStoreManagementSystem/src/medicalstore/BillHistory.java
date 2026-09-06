package medicalstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.*;

public class BillHistory extends JFrame {

    private JTable billTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public BillHistory() {

        setTitle("MediCare Pharmacy - Bill History");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 248, 250));

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(13, 71, 76));
        header.setPreferredSize(new Dimension(1100, 85));

        JLabel title = new JLabel("  MEDICARE PHARMACY");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));

        JLabel subtitle = new JLabel("Bill History  ");
        subtitle.setForeground(new Color(200, 230, 230));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        header.add(title, BorderLayout.WEST);
        header.add(subtitle, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // =====================================================
        // SEARCH PANEL
        // =====================================================

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(
                20, 25, 15, 25
        ));

        JLabel searchLabel = new JLabel("Search Bill:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton searchButton = createButton("Search");
        JButton refreshButton = createButton("Refresh");

        JPanel searchBox = new JPanel(new BorderLayout(10, 0));
        searchBox.setBackground(Color.WHITE);

        searchBox.add(searchLabel, BorderLayout.WEST);
        searchBox.add(searchField, BorderLayout.CENTER);

        JPanel searchButtons = new JPanel(new FlowLayout(
                FlowLayout.RIGHT, 5, 0
        ));

        searchButtons.setBackground(Color.WHITE);
        searchButtons.add(searchButton);
        searchButtons.add(refreshButton);

        searchPanel.add(searchBox, BorderLayout.CENTER);
        searchPanel.add(searchButtons, BorderLayout.EAST);

        mainPanel.add(searchPanel, BorderLayout.PAGE_START);

        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
                "ID",
                "Bill Number",
                "Customer",
                "Mobile",
                "Medicine",
                "Price",
                "Qty",
                "Subtotal",
                "GST",
                "Discount",
                "Total",
                "Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        billTable = new JTable(tableModel);

        billTable.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        billTable.setRowHeight(32);

        billTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        billTable.getTableHeader().setBackground(
                new Color(13, 71, 76)
        );

        billTable.getTableHeader().setForeground(Color.WHITE);

        billTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(billTable);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 25, 0, 25
                )
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // BUTTON PANEL
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
                new Color(245, 248, 250)
        );

        JButton viewButton =
                createButton("View Bill");

        JButton printButton =
                createButton("Print Bill");

        JButton closeButton =
                createButton("Close");

        bottomPanel.add(viewButton);
        bottomPanel.add(printButton);
        bottomPanel.add(closeButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        // =====================================================
        // LOAD DATA
        // =====================================================

        loadBills("");

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        searchButton.addActionListener(
                e -> searchBills()
        );

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadBills("");
                }
        );

        searchField.addActionListener(
                e -> searchBills()
        );

        viewButton.addActionListener(
                e -> viewSelectedBill()
        );

        printButton.addActionListener(
                e -> printSelectedBill()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        setVisible(true);
    }

    // =========================================================
    // LOAD BILLS
    // =========================================================

    private void loadBills(String search) {

        tableModel.setRowCount(0);

        String sql;

        if (search == null || search.trim().isEmpty()) {

            sql =
                    "SELECT id, bill_number, customer_name, mobile, " +
                            "medicine_name, price, quantity, subtotal, gst, " +
                            "discount, total, bill_date " +
                            "FROM bills ORDER BY id DESC";

        } else {

            sql =
                    "SELECT id, bill_number, customer_name, mobile, " +
                            "medicine_name, price, quantity, subtotal, gst, " +
                            "discount, total, bill_date " +
                            "FROM bills " +
                            "WHERE bill_number LIKE ? " +
                            "OR customer_name LIKE ? " +
                            "OR mobile LIKE ? " +
                            "OR medicine_name LIKE ? " +
                            "ORDER BY id DESC";
        }

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            if (search != null &&
                    !search.trim().isEmpty()) {

                String value =
                        "%" + search.trim() + "%";

                ps.setString(1, value);
                ps.setString(2, value);
                ps.setString(3, value);
                ps.setString(4, value);
            }

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                Object[] row = {

                        rs.getInt("id"),

                        rs.getString("bill_number"),

                        rs.getString("customer_name"),

                        rs.getString("mobile"),

                        rs.getString("medicine_name"),

                        "₹ " + String.format(
                                "%.2f",
                                rs.getDouble("price")
                        ),

                        rs.getInt("quantity"),

                        "₹ " + String.format(
                                "%.2f",
                                rs.getDouble("subtotal")
                        ),

                        "₹ " + String.format(
                                "%.2f",
                                rs.getDouble("gst")
                        ),

                        "₹ " + String.format(
                                "%.2f",
                                rs.getDouble("discount")
                        ),

                        "₹ " + String.format(
                                "%.2f",
                                rs.getDouble("total")
                        ),

                        rs.getTimestamp("bill_date")
                };

                tableModel.addRow(row);
            }

            rs.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bill history loading error:\n" +
                            ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private void searchBills() {

        String search =
                searchField.getText().trim();

        loadBills(search);
    }

    // =========================================================
    // VIEW BILL
    // =========================================================

    private void viewSelectedBill() {

        int selectedRow =
                billTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a bill first.",
                    "No Bill Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String billNumber =
                billTable.getValueAt(
                        selectedRow,
                        1
                ).toString();

        showBill(billNumber, false);
    }

    // =========================================================
    // PRINT BILL
    // =========================================================

    private void printSelectedBill() {

        int selectedRow =
                billTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a bill first.",
                    "No Bill Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String billNumber =
                billTable.getValueAt(
                        selectedRow,
                        1
                ).toString();

        showBill(billNumber, true);
    }

    // =========================================================
    // GET BILL FROM DATABASE
    // =========================================================

    private void showBill(
            String billNumber,
            boolean print
    ) {

        String sql =
                "SELECT * FROM bills WHERE bill_number=?";

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, billNumber);

            ResultSet rs =
                    ps.executeQuery();

            if (!rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bill not found."
                );

                return;
            }

            String bill =
                    createBillText(rs);

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
                            20,
                            20,
                            20,
                            20
                    )
            );

            if (print) {

                try {

                    boolean printed =
                            billArea.print();

                    if (printed) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Bill sent to printer successfully.",
                                "Print Successful",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                } catch (PrinterException ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Printing failed:\n" +
                                    ex.getMessage(),
                            "Print Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } else {

                JScrollPane scrollPane =
                        new JScrollPane(billArea);

                scrollPane.setPreferredSize(
                        new Dimension(
                                550,
                                550
                        )
                );

                JOptionPane.showMessageDialog(
                        this,
                        scrollPane,
                        "Bill Details - " + billNumber,
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            rs.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open bill:\n" +
                            ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CREATE BILL TEXT
    // =========================================================

    private String createBillText(
            ResultSet rs
    ) throws SQLException {

        String billNumber =
                rs.getString("bill_number");

        String customer =
                rs.getString("customer_name");

        String mobile =
                rs.getString("mobile");

        String medicine =
                rs.getString("medicine_name");

        double price =
                rs.getDouble("price");

        int quantity =
                rs.getInt("quantity");

        double subtotal =
                rs.getDouble("subtotal");

        double gst =
                rs.getDouble("gst");

        double discount =
                rs.getDouble("discount");

        double total =
                rs.getDouble("total");

        Timestamp timestamp =
                rs.getTimestamp("bill_date");

        return
                "========================================\n" +
                        "          MEDICARE PHARMACY\n" +
                        "          BILL INVOICE\n" +
                        "========================================\n\n" +

                        "Bill No.   : " + billNumber + "\n" +

                        "Date       : " + timestamp + "\n" +

                        "Customer   : " + customer + "\n" +

                        "Mobile     : " + mobile + "\n\n" +

                        "----------------------------------------\n" +

                        "Medicine   : " + medicine + "\n" +

                        "Price      : ₹ " +
                        String.format("%.2f", price) + "\n" +

                        "Quantity   : " + quantity + "\n" +

                        "----------------------------------------\n\n" +

                        "Subtotal   : ₹ " +
                        String.format("%.2f", subtotal) + "\n" +

                        "GST (5%)   : ₹ " +
                        String.format("%.2f", gst) + "\n" +

                        "Discount   : ₹ " +
                        String.format("%.2f", discount) + "\n" +

                        "----------------------------------------\n" +

                        "TOTAL      : ₹ " +
                        String.format("%.2f", total) + "\n" +

                        "========================================\n\n" +

                        "        Thank You! Visit Again.\n" +

                        "========================================";
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
                        10,
                        20,
                        10,
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
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                BillHistory::new
        );
    }
}


