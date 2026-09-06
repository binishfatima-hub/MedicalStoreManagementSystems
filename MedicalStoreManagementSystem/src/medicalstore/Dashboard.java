package medicalstore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class Dashboard extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private final Color DARK = new Color(13, 71, 76);
    private final Color PRIMARY = new Color(19, 150, 137);
    private final Color BACKGROUND = new Color(245, 248, 250);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(35, 45, 55);
    private final Color MUTED = new Color(110, 120, 130);

    // Dashboard labels
    private JLabel totalMedicinesLabel;
    private JLabel lowStockLabel;
    private JLabel expiredLabel;
    private JLabel todaySalesLabel;
    private JLabel totalBillsLabel;

    public Dashboard() {

        setTitle("MediCare Pharmacy - Dashboard");

        setSize(1200, 750);

        setMinimumSize(
                new Dimension(1050, 650)
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        getContentPane().setBackground(
                BACKGROUND
        );

        setLayout(
                new BorderLayout()
        );

        // =====================================================
        // SIDEBAR
        // =====================================================

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(245, 750)
        );

        sidebar.setBackground(DARK);

        sidebar.setLayout(
                new BorderLayout()
        );

        // -----------------------------------------------------
        // LOGO
        // -----------------------------------------------------

        JPanel logoPanel =
                new JPanel();

        logoPanel.setBackground(DARK);

        logoPanel.setBorder(
                new EmptyBorder(
                        28,
                        20,
                        25,
                        20
                )
        );

        logoPanel.setLayout(
                new BoxLayout(
                        logoPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel logo =
                new JLabel("⚕");

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        42
                )
        );

        logo.setForeground(
                new Color(
                        70,
                        220,
                        200
                )
        );

        logo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel pharmacy =
                new JLabel(
                        "MEDICARE"
                );

        pharmacy.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23
                )
        );

        pharmacy.setForeground(
                Color.WHITE
        );

        pharmacy.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel pharmacy2 =
                new JLabel(
                        "PHARMACY"
                );

        pharmacy2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        pharmacy2.setForeground(
                new Color(
                        180,
                        220,
                        215
                )
        );

        pharmacy2.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        logoPanel.add(logo);

        logoPanel.add(
                Box.createVerticalStrut(3)
        );

        logoPanel.add(pharmacy);

        logoPanel.add(
                Box.createVerticalStrut(2)
        );

        logoPanel.add(pharmacy2);

        sidebar.add(
                logoPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // MENU
        // =====================================================

        JPanel menuPanel =
                new JPanel();

        menuPanel.setBackground(DARK);

        menuPanel.setBorder(
                new EmptyBorder(
                        5,
                        12,
                        10,
                        12
                )
        );

        menuPanel.setLayout(
                new BoxLayout(
                        menuPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel menuTitle =
                new JLabel(
                        "  MAIN MENU"
                );

        menuTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        menuTitle.setForeground(
                new Color(
                        140,
                        190,
                        185
                )
        );

        menuPanel.add(menuTitle);

        menuPanel.add(
                Box.createVerticalStrut(10)
        );

        // Dashboard
        JButton dashboardBtn =
                createMenuButton(
                        "▦   Dashboard"
                );

        dashboardBtn.setBackground(
                PRIMARY
        );

        menuPanel.add(dashboardBtn);

        // Add Medicine
        JButton addMedicineBtn =
                createMenuButton(
                        "＋   Add Medicine"
                );

        menuPanel.add(addMedicineBtn);

        // View Medicine
        JButton viewMedicineBtn =
                createMenuButton(
                        "▤   View Medicine"
                );

        menuPanel.add(viewMedicineBtn);

        // Search Medicine
        JButton searchMedicineBtn =
                createMenuButton(
                        "⌕   Search Medicine"
                );

        menuPanel.add(searchMedicineBtn);

        // Update Medicine
        JButton updateMedicineBtn =
                createMenuButton(
                        "✎   Update Medicine"
                );

        menuPanel.add(updateMedicineBtn);

        // Delete Medicine
        JButton deleteMedicineBtn =
                createMenuButton(
                        "✕   Delete Medicine"
                );

        menuPanel.add(deleteMedicineBtn);

        // Billing
        JButton billingBtn =
                createMenuButton(
                        "▣   Billing"
                );

        menuPanel.add(billingBtn);

        // Bill History
        JButton billHistoryBtn =
                createMenuButton(
                        "▥   Bill History"
                );

        menuPanel.add(billHistoryBtn);

        sidebar.add(
                menuPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // LOGOUT
        // =====================================================

        JPanel logoutPanel =
                new JPanel(
                        new BorderLayout()
                );

        logoutPanel.setBackground(DARK);

        logoutPanel.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        20,
                        12
                )
        );

        JButton logoutBtn =
                createMenuButton(
                        "↪   Logout"
                );

        logoutPanel.add(
                logoutBtn,
                BorderLayout.CENTER
        );

        sidebar.add(
                logoutPanel,
                BorderLayout.SOUTH
        );

        add(
                sidebar,
                BorderLayout.WEST
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                BACKGROUND
        );

        // =====================================================
        // TOP HEADER
        // =====================================================

        JPanel topHeader =
                new JPanel(
                        new BorderLayout()
                );

        topHeader.setBackground(
                WHITE
        );

        topHeader.setBorder(
                new EmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        JLabel welcome =
                new JLabel(
                        "Welcome to MediCare Pharmacy"
                );

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        welcome.setForeground(
                DARK
        );

        JLabel date =
                new JLabel(
                        "Medical Store Management System"
                );

        date.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        date.setForeground(MUTED);

        JPanel welcomePanel =
                new JPanel();

        welcomePanel.setOpaque(false);

        welcomePanel.setLayout(
                new BoxLayout(
                        welcomePanel,
                        BoxLayout.Y_AXIS
                )
        );

        welcomePanel.add(welcome);

        welcomePanel.add(
                Box.createVerticalStrut(5)
        );

        welcomePanel.add(date);

        topHeader.add(
                welcomePanel,
                BorderLayout.WEST
        );

        JLabel profile =
                new JLabel(
                        "ADMIN  ●"
                );

        profile.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        profile.setForeground(PRIMARY);

        topHeader.add(
                profile,
                BorderLayout.EAST
        );

        mainPanel.add(
                topHeader,
                BorderLayout.NORTH
        );

        // =====================================================
        // DASHBOARD CENTER
        // =====================================================

        JPanel content =
                new JPanel();

        content.setBackground(
                BACKGROUND
        );

        content.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // STAT CARDS
        // =====================================================

        JPanel statsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                5,
                                15,
                                0
                        )
                );

        statsPanel.setBackground(
                BACKGROUND
        );

        totalMedicinesLabel =
                new JLabel("0");

        lowStockLabel =
                new JLabel("0");

        expiredLabel =
                new JLabel("0");

        todaySalesLabel =
                new JLabel("₹0.00");

        totalBillsLabel =
                new JLabel("0");

        statsPanel.add(
                createStatCard(
                        "Total Medicines",
                        "▤",
                        totalMedicinesLabel,
                        PRIMARY
                )
        );

        statsPanel.add(
                createStatCard(
                        "Low Stock",
                        "⚠",
                        lowStockLabel,
                        new Color(230, 150, 40)
                )
        );

        statsPanel.add(
                createStatCard(
                        "Expired",
                        "✕",
                        expiredLabel,
                        new Color(220, 70, 70)
                )
        );

        statsPanel.add(
                createStatCard(
                        "Today's Sales",
                        "₹",
                        todaySalesLabel,
                        new Color(45, 125, 190)
                )
        );

        statsPanel.add(
                createStatCard(
                        "Total Bills",
                        "▣",
                        totalBillsLabel,
                        new Color(130, 90, 180)
                )
        );

        content.add(statsPanel);

        content.add(
                Box.createVerticalStrut(25)
        );

        // =====================================================
        // QUICK ACTIONS
        // =====================================================

        JLabel quickTitle =
                new JLabel(
                        "Quick Actions"
                );

        quickTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        quickTitle.setForeground(TEXT);

        content.add(quickTitle);

        content.add(
                Box.createVerticalStrut(12)
        );

        JPanel quickButtons =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                0
                        )
                );

        quickButtons.setBackground(
                BACKGROUND
        );

        JButton quickAdd =
                createQuickButton(
                        "＋  Add Medicine"
                );

        JButton quickView =
                createQuickButton(
                        "▤  View Medicines"
                );

        JButton quickBilling =
                createQuickButton(
                        "▣  Create Bill"
                );

        JButton quickHistory =
                createQuickButton(
                        "▥  Bill History"
                );

        quickButtons.add(quickAdd);
        quickButtons.add(quickView);
        quickButtons.add(quickBilling);
        quickButtons.add(quickHistory);

        content.add(quickButtons);

        content.add(
                Box.createVerticalStrut(25)
        );

        // =====================================================
        // INFORMATION PANELS
        // =====================================================

        JPanel infoPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                0
                        )
                );

        infoPanel.setBackground(
                BACKGROUND
        );

        infoPanel.add(
                createInfoPanel(
                        "Medicine Management",
                        "Manage your complete medicine inventory.",
                        new String[]{
                                "• Add new medicines",
                                "• View stock and expiry",
                                "• Search medicines",
                                "• Update medicine details",
                                "• Delete medicines"
                        }
                )
        );

        infoPanel.add(
                createInfoPanel(
                        "Billing & Sales",
                        "Manage customer bills and sales records.",
                        new String[]{
                                "• Create new bills",
                                "• Calculate GST & discount",
                                "• Generate bill",
                                "• View bill history",
                                "• Print previous bills"
                        }
                )
        );

        content.add(infoPanel);

        content.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // STATUS BAR
        // =====================================================

        JPanel statusPanel =
                new JPanel(
                        new BorderLayout()
                );

        statusPanel.setBackground(
                WHITE
        );

        statusPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        230,
                                        235
                                )
                        ),
                        new EmptyBorder(
                                12,
                                18,
                                12,
                                18
                        )
                )
        );

        JLabel status =
                new JLabel(
                        "●  System Connected  |  Database: medical_store"
                );

        status.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        status.setForeground(PRIMARY);

        JLabel footer =
                new JLabel(
                        "MediCare Pharmacy © 2026"
                );

        footer.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        footer.setForeground(MUTED);

        statusPanel.add(
                status,
                BorderLayout.WEST
        );

        statusPanel.add(
                footer,
                BorderLayout.EAST
        );

        content.add(statusPanel);

        mainPanel.add(
                content,
                BorderLayout.CENTER
        );

        add(
                mainPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        addMedicineBtn.addActionListener(
                e -> new AddMedicine()
        );

        viewMedicineBtn.addActionListener(
                e -> new ViewMedicine()
        );

        searchMedicineBtn.addActionListener(
                e -> new SearchMedicine()
        );

        updateMedicineBtn.addActionListener(
                e -> new UpdateMedicine()
        );

        deleteMedicineBtn.addActionListener(
                e -> new DeleteMedicine()
        );

        billingBtn.addActionListener(
                e -> new Billing()
        );

        billHistoryBtn.addActionListener(
                e -> new BillHistory()
        );

        quickAdd.addActionListener(
                e -> new AddMedicine()
        );

        quickView.addActionListener(
                e -> new ViewMedicine()
        );

        quickBilling.addActionListener(
                e -> new Billing()
        );

        quickHistory.addActionListener(
                e -> new BillHistory()
        );

        logoutBtn.addActionListener(
                e -> logout()
        );

        // =====================================================
        // LOAD DASHBOARD DATA
        // =====================================================

        loadDashboardData();

        setVisible(true);
    }

    // =========================================================
    // LOAD DATABASE DATA
    // =========================================================

    private void loadDashboardData() {

        String medicineSql =
                "SELECT " +
                        "COUNT(*) AS total, " +
                        "SUM(CASE WHEN quantity <= 5 THEN 1 ELSE 0 END) AS low_stock, " +
                        "SUM(CASE WHEN expiry_date < CURDATE() THEN 1 ELSE 0 END) AS expired " +
                        "FROM medicine";

        String salesSql =
                "SELECT COALESCE(SUM(total), 0) " +
                        "FROM bills " +
                        "WHERE DATE(bill_date) = CURDATE()";

        String billsSql =
                "SELECT COUNT(*) FROM bills";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement medicinePst =
                        con.prepareStatement(medicineSql);

                PreparedStatement salesPst =
                        con.prepareStatement(salesSql);

                PreparedStatement billsPst =
                        con.prepareStatement(billsSql)
        ) {

            // -------------------------------------------------
            // MEDICINE DATA
            // -------------------------------------------------

            ResultSet medicineRs =
                    medicinePst.executeQuery();

            if (medicineRs.next()) {

                totalMedicinesLabel.setText(
                        String.valueOf(
                                medicineRs.getInt("total")
                        )
                );

                lowStockLabel.setText(
                        String.valueOf(
                                medicineRs.getInt("low_stock")
                        )
                );

                expiredLabel.setText(
                        String.valueOf(
                                medicineRs.getInt("expired")
                        )
                );
            }

            medicineRs.close();

            // -------------------------------------------------
            // TODAY SALES
            // -------------------------------------------------

            ResultSet salesRs =
                    salesPst.executeQuery();

            if (salesRs.next()) {

                double sales =
                        salesRs.getDouble(1);

                todaySalesLabel.setText(
                        String.format(
                                "₹%.2f",
                                sales
                        )
                );
            }

            salesRs.close();

            // -------------------------------------------------
            // TOTAL BILLS
            // -------------------------------------------------

            ResultSet billsRs =
                    billsPst.executeQuery();

            if (billsRs.next()) {

                totalBillsLabel.setText(
                        String.valueOf(
                                billsRs.getInt(1)
                        )
                );
            }

            billsRs.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Dashboard database error:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            String iconText,
            JLabel valueLabel,
            Color accent
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        230,
                                        235
                                )
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        // -----------------------------------------------------
        // ICON
        // -----------------------------------------------------

        JLabel icon =
                new JLabel(iconText);

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        icon.setForeground(accent);

        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        icon.setPreferredSize(
                new Dimension(
                        45,
                        45
                )
        );

        card.add(
                icon,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // TEXT
        // -----------------------------------------------------

        JPanel textPanel =
                new JPanel();

        textPanel.setOpaque(false);

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        titleLabel.setForeground(MUTED);

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        valueLabel.setForeground(TEXT);

        textPanel.add(titleLabel);

        textPanel.add(
                Box.createVerticalStrut(5)
        );

        textPanel.add(valueLabel);

        card.add(
                textPanel,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // INFO PANEL
    // =========================================================

    private JPanel createInfoPanel(
            String title,
            String description,
            String[] items
    ) {

        JPanel panel =
                new JPanel();

        panel.setBackground(WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        230,
                                        235
                                )
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        titleLabel.setForeground(DARK);

        panel.add(titleLabel);

        panel.add(
                Box.createVerticalStrut(5)
        );

        JLabel desc =
                new JLabel(description);

        desc.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        desc.setForeground(MUTED);

        panel.add(desc);

        panel.add(
                Box.createVerticalStrut(12)
        );

        for (String item : items) {

            JLabel itemLabel =
                    new JLabel(item);

            itemLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            13
                    )
            );

            itemLabel.setForeground(TEXT);

            panel.add(itemLabel);

            panel.add(
                    Box.createVerticalStrut(5)
            );
        }

        return panel;
    }

    // =========================================================
    // MENU BUTTON
    // =========================================================

    private JButton createMenuButton(
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
                new Color(
                        225,
                        240,
                        238
                )
        );

        button.setBackground(DARK);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        15,
                        12,
                        10
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        46
                )
        );

        return button;
    }

    // =========================================================
    // QUICK BUTTON
    // =========================================================

    private JButton createQuickButton(
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
                DARK
        );

        button.setBackground(WHITE);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        225,
                                        230
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                10,
                                15,
                                10
                        )
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
    // LOGOUT
    // =========================================================

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                choice ==
                        JOptionPane.YES_OPTION
        ) {

            dispose();

            new Login();
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new Dashboard()
        );
    }
}
