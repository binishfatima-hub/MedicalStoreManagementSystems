package medicalstore;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ViewMedicine extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JLabel medicineCount;
    private JTextField searchField;

    // Colors
    private final Color DARK = new Color(20, 47, 65);
    private final Color PRIMARY = new Color(19, 150, 137);
    private final Color BACKGROUND = new Color(245, 248, 250);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(40, 50, 60);

    public ViewMedicine() {

        setTitle("MediCare Pharmacy - Medicine Inventory");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK);
        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(DARK);
        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title = new JLabel(
                "Medicine Inventory"
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(WHITE);

        JLabel subtitle = new JLabel(
                "Manage and monitor your pharmacy medicines"
        );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                new Color(190, 205, 215)
        );

        titlePanel.add(title);
        titlePanel.add(
                Box.createVerticalStrut(4)
        );
        titlePanel.add(subtitle);

        // Medicine count
        medicineCount = new JLabel(
                "0 Medicines"
        );

        medicineCount.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        medicineCount.setForeground(
                new Color(57, 210, 190)
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                medicineCount,
                BorderLayout.EAST
        );

        add(header, BorderLayout.NORTH);

        // =====================================================
        // TOP TOOLBAR
        // =====================================================

        JPanel toolbar = new JPanel(
                new BorderLayout(
                        15,
                        0
                )
        );

        toolbar.setBackground(BACKGROUND);

        toolbar.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 15, 25
                )
        );

        // Search area
        JPanel searchPanel =
                new JPanel(new BorderLayout());

        searchPanel.setBackground(WHITE);

        searchField = new JTextField();

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 222, 227)
                        ),
                        BorderFactory.createEmptyBorder(
                                8, 12, 8, 12
                        )
                )
        );

        searchField.setToolTipText(
                "Search by medicine name or company"
        );

        JButton searchButton =
                new JButton("⌕  Search");

        styleButton(
                searchButton,
                PRIMARY,
                WHITE
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        searchPanel.add(
                searchButton,
                BorderLayout.EAST
        );

        toolbar.add(
                searchPanel,
                BorderLayout.CENTER
        );

        // Refresh button
        JButton refreshButton =
                new JButton("⟳  Refresh");

        styleButton(
                refreshButton,
                DARK,
                WHITE
        );

        toolbar.add(
                refreshButton,
                BorderLayout.EAST
        );

        add(
                toolbar,
                BorderLayout.BEFORE_FIRST_LINE
        );

        // =====================================================
        // TABLE
        // =====================================================

        model = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Medicine Name",
                        "Company",
                        "Price",
                        "Quantity",
                        "Expiry Date",
                        "Stock Status",
                        "Expiry Status"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        table = new JTable(model);

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        table.setRowHeight(42);

        table.setShowGrid(false);

        table.setIntercellSpacing(
                new Dimension(0, 0)
        );

        table.setSelectionBackground(
                new Color(225, 244, 241)
        );

        table.setSelectionForeground(TEXT);

        table.setAutoCreateRowSorter(true);

        // Header
        table.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        table.getTableHeader().setBackground(
                DARK
        );

        table.getTableHeader().setForeground(
                WHITE
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(0, 45)
        );

        // Center alignment
        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        table.getColumnModel()
                .getColumn(0)
                .setCellRenderer(center);

        table.getColumnModel()
                .getColumn(3)
                .setCellRenderer(center);

        table.getColumnModel()
                .getColumn(4)
                .setCellRenderer(center);

        table.getColumnModel()
                .getColumn(5)
                .setCellRenderer(center);

        // Status renderers
        table.getColumnModel()
                .getColumn(6)
                .setCellRenderer(
                        new StatusRenderer()
                );

        table.getColumnModel()
                .getColumn(7)
                .setCellRenderer(
                        new ExpiryRenderer()
                );

        // Column widths
        table.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        table.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(160);

        table.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(130);

        table.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(80);

        table.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(80);

        table.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(110);

        table.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(120);

        table.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(130);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 25, 10, 25
                )
        );

        scrollPane.getViewport()
                .setBackground(WHITE);

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM LEGEND
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                18,
                                8
                        )
                );

        bottomPanel.setBackground(BACKGROUND);

        JLabel normal =
                createLegend(
                        "● Normal",
                        new Color(50, 160, 100)
                );

        JLabel low =
                createLegend(
                        "● Low Stock",
                        new Color(230, 150, 45)
                );

        JLabel expired =
                createLegend(
                        "● Expired",
                        new Color(205, 65, 70)
                );

        JLabel expiring =
                createLegend(
                        "● Expiring Soon",
                        new Color(230, 125, 45)
                );

        bottomPanel.add(normal);
        bottomPanel.add(low);
        bottomPanel.add(expired);
        bottomPanel.add(expiring);

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        searchButton.addActionListener(
                e -> loadMedicines(
                        searchField.getText().trim()
                )
        );

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadMedicines("");
                }
        );

        searchField.addActionListener(
                e -> loadMedicines(
                        searchField.getText().trim()
                )
        );

        // =====================================================
        // LOAD DATA
        // =====================================================

        loadMedicines("");

        setVisible(true);
    }

    // =========================================================
    // LOAD MEDICINES
    // =========================================================

    private void loadMedicines(
            String search
    ) {

        model.setRowCount(0);

        String sql;

        if (search.isEmpty()) {

            sql =
                    "SELECT id, name, company, price, "
                            + "quantity, expiry_date "
                            + "FROM medicine "
                            + "ORDER BY id DESC";

        } else {

            sql =
                    "SELECT id, name, company, price, "
                            + "quantity, expiry_date "
                            + "FROM medicine "
                            + "WHERE name LIKE ? "
                            + "OR company LIKE ? "
                            + "ORDER BY id DESC";
        }

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            if (!search.isEmpty()) {

                ps.setString(
                        1,
                        "%" + search + "%"
                );

                ps.setString(
                        2,
                        "%" + search + "%"
                );
            }

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                int count = 0;

                while (rs.next()) {

                    int id =
                            rs.getInt("id");

                    String name =
                            rs.getString("name");

                    String company =
                            rs.getString("company");

                    double price =
                            rs.getDouble("price");

                    int quantity =
                            rs.getInt("quantity");

                    Date expiry =
                            rs.getDate("expiry_date");

                    String stockStatus;

                    if (quantity <= 5) {

                        stockStatus =
                                "LOW STOCK";

                    } else {

                        stockStatus =
                                "NORMAL";
                    }

                    String expiryStatus =
                            getExpiryStatus(
                                    expiry
                            );

                    model.addRow(
                            new Object[]{
                                    id,
                                    name,
                                    company,
                                    "₹" + price,
                                    quantity,
                                    expiry,
                                    stockStatus,
                                    expiryStatus
                            }
                    );

                    count++;
                }

                medicineCount.setText(
                        count + " Medicines"
                );
            }

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
    }

    // =========================================================
    // EXPIRY STATUS
    // =========================================================

    private String getExpiryStatus(
            Date expiryDate
    ) {

        if (expiryDate == null) {
            return "UNKNOWN";
        }

        LocalDate expiry =
                expiryDate.toLocalDate();

        LocalDate today =
                LocalDate.now();

        long days =
                ChronoUnit.DAYS.between(
                        today,
                        expiry
                );

        if (days < 0) {

            return "EXPIRED";

        } else if (days <= 30) {

            return "EXPIRING SOON";

        } else {

            return "SAFE";
        }
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
                        10, 18, 10, 18
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =========================================================
    // LEGEND
    // =========================================================

    private JLabel createLegend(
            String text,
            Color color
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(color);

        return label;
    }

    // =========================================================
    // STOCK STATUS RENDERER
    // =========================================================

    class StatusRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column
                    );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            11
                    )
            );

            if (!isSelected) {

                if ("LOW STOCK".equals(value)) {

                    label.setForeground(
                            new Color(
                                    210,
                                    125,
                                    20
                            )
                    );

                    label.setBackground(
                            new Color(
                                    255,
                                    243,
                                    218
                            )
                    );

                } else {

                    label.setForeground(
                            new Color(
                                    40,
                                    145,
                                    90
                            )
                    );

                    label.setBackground(
                            new Color(
                                    232,
                                    248,
                                    238
                            )
                    );
                }
            }

            return label;
        }
    }

    // =========================================================
    // EXPIRY RENDERER
    // =========================================================

    class ExpiryRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column
                    );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            11
                    )
            );

            if (!isSelected) {

                if ("EXPIRED".equals(value)) {

                    label.setForeground(
                            new Color(
                                    190,
                                    45,
                                    50
                            )
                    );

                    label.setBackground(
                            new Color(
                                    255,
                                    228,
                                    230
                            )
                    );

                } else if ("EXPIRING SOON".equals(value)) {

                    label.setForeground(
                            new Color(
                                    210,
                                    110,
                                    20
                            )
                    );

                    label.setBackground(
                            new Color(
                                    255,
                                    240,
                                    220
                            )
                    );

                } else {

                    label.setForeground(
                            new Color(
                                    40,
                                    145,
                                    90
                            )
                    );

                    label.setBackground(
                            new Color(
                                    232,
                                    248,
                                    238
                            )
                    );
                }
            }

            return label;
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new ViewMedicine()
        );
    }
}