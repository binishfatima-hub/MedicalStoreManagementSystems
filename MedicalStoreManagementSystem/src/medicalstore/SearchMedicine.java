package medicalstore;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SearchMedicine extends JFrame {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    // ================= COLORS =================

    private final Color PRIMARY = new Color(19, 150, 137);
    private final Color DARK = new Color(13, 71, 76);
    private final Color BACKGROUND = new Color(245, 248, 250);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(35, 45, 55);

    public SearchMedicine() {

        setTitle("MediCare Pharmacy - Search Medicine");
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(DARK);

        header.setPreferredSize(
                new Dimension(1000, 90)
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 25, 15, 25
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

        JLabel title =
                new JLabel("MEDICARE PHARMACY");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(Color.WHITE);

        JLabel subtitle =
                new JLabel(
                        "Search & Find Medicines"
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
                new JLabel("⌕");

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
        // SEARCH PANEL
        // =====================================================

        JPanel searchPanel =
                new JPanel(
                        new BorderLayout()
                );

        searchPanel.setBackground(WHITE);

        searchPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        230,
                                        235
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                25,
                                18,
                                25
                        )
                )
        );

        JPanel searchLeft =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        searchLeft.setBackground(WHITE);

        JLabel searchLabel =
                new JLabel(
                        "Search Medicine:"
                );

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        searchLabel.setForeground(TEXT);

        searchField =
                new JTextField();

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        searchField.setPreferredSize(
                new Dimension(
                        400,
                        40
                )
        );

        searchField.setBorder(
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
                                12,
                                5,
                                12
                        )
                )
        );

        searchLeft.add(
                searchLabel,
                BorderLayout.WEST
        );

        searchLeft.add(
                searchField,
                BorderLayout.CENTER
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setBackground(WHITE);

        JButton searchButton =
                createButton(
                        "⌕  Search"
                );

        JButton refreshButton =
                createButton(
                        "⟳  Refresh"
                );

        JButton closeButton =
                createButton(
                        "×  Close"
                );

        buttons.add(searchButton);
        buttons.add(refreshButton);
        buttons.add(closeButton);

        searchPanel.add(
                searchLeft,
                BorderLayout.CENTER
        );

        searchPanel.add(
                buttons,
                BorderLayout.EAST
        );

        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
                "ID",
                "Medicine Name",
                "Company",
                "Price",
                "Quantity",
                "Expiry Date",
                "Status"
        };

        model =
                new DefaultTableModel(
                        columns,
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

        table =
                new JTable(model);

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        table.setRowHeight(34);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setGridColor(
                new Color(
                        225,
                        230,
                        235
                )
        );

        table.setShowVerticalLines(false);

        table.setBackground(WHITE);

        table.setForeground(TEXT);

        // TABLE HEADER

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
                Color.WHITE
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(
                        0,
                        40
                )
        );

        // COLUMN WIDTHS

        table.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        table.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(180);

        table.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(150);

        table.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(100);

        table.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(90);

        table.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(120);

        table.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(120);

        // CENTER ALIGNMENT

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        for (
                int i = 0;
                i < table.getColumnCount();
                i++
        ) {

            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(
                            centerRenderer
                    );
        }

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        25,
                        10,
                        25
                )
        );

        // =====================================================
        // CENTER AREA
        // =====================================================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout()
                );

        centerPanel.setBackground(
                BACKGROUND
        );

        centerPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM STATUS
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setBackground(
                BACKGROUND
        );

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        25,
                        15,
                        25
                )
        );

        JLabel info =
                new JLabel(
                        "●  Search medicines by name"
                );

        info.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        info.setForeground(
                new Color(
                        80,
                        130,
                        100
                )
        );

        JLabel version =
                new JLabel(
                        "Medical Store v1.0"
                );

        version.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        version.setForeground(
                new Color(
                        140,
                        145,
                        150
                )
        );

        bottomPanel.add(
                info,
                BorderLayout.WEST
        );

        bottomPanel.add(
                version,
                BorderLayout.EAST
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        searchButton.addActionListener(
                e -> searchMedicine()
        );

        searchField.addActionListener(
                e -> searchMedicine()
        );

        refreshButton.addActionListener(
                e -> {

                    searchField.setText("");

                    searchMedicine();
                }
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        // LOAD ALL MEDICINES INITIALLY

        searchMedicine();

        setVisible(true);
    }

    // =========================================================
    // SEARCH MEDICINE
    // =========================================================

    private void searchMedicine() {

        model.setRowCount(0);

        String searchName =
                searchField.getText().trim();

        String sql =
                "SELECT * FROM medicine " +
                        "WHERE name LIKE ? " +
                        "ORDER BY name ASC";

        try (
                Connection con =
                        DatabaseConnection
                                .getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setString(
                    1,
                    "%" + searchName + "%"
            );

            ResultSet rs =
                    pst.executeQuery();

            int count = 0;

            while (rs.next()) {

                int quantity =
                        rs.getInt("quantity");

                Date expiryDate =
                        rs.getDate(
                                "expiry_date"
                        );

                String status =
                        getStatus(
                                quantity,
                                expiryDate
                        );

                model.addRow(
                        new Object[]{

                                rs.getInt("id"),

                                rs.getString(
                                        "name"
                                ),

                                rs.getString(
                                        "company"
                                ),

                                "₹ " +
                                        String.format(
                                                "%.2f",
                                                rs.getDouble(
                                                        "price"
                                                )
                                        ),

                                quantity,

                                expiryDate,

                                status
                        }
                );

                count++;
            }

            rs.close();

            if (count == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No medicine found.",
                        "Search Result",
                        JOptionPane.INFORMATION_MESSAGE
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
    // MEDICINE STATUS
    // =========================================================

    private String getStatus(
            int quantity,
            Date expiryDate
    ) {

        if (expiryDate != null) {

            long today =
                    System.currentTimeMillis();

            long expiry =
                    expiryDate.getTime();

            long days =
                    (expiry - today)
                            /
                            (1000L * 60 * 60 * 24);

            if (days < 0) {

                return "EXPIRED";
            }

            if (days <= 30) {

                return "EXPIRING SOON";
            }
        }

        if (quantity <= 5) {

            return "LOW STOCK";
        }

        return "AVAILABLE";
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
                () -> new SearchMedicine()
        );
    }
}

