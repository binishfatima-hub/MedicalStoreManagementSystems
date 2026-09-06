package medicalstore;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    // ================= COLORS =================

    private final Color DARK = new Color(13, 71, 76);
    private final Color PRIMARY = new Color(19, 150, 137);
    private final Color BACKGROUND = new Color(245, 248, 250);
    private final Color TEXT = new Color(35, 45, 55);
    private final Color MUTED = new Color(110, 120, 130);

    public Login() {

        setTitle("MediCare Pharmacy - Login");
        setSize(900, 580);

        setMinimumSize(
                new Dimension(800, 520)
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        getContentPane().setBackground(
                BACKGROUND
        );

        setLayout(
                new GridLayout(1, 2)
        );

        // =====================================================
        // LEFT SIDE - BRANDING
        // =====================================================

        JPanel leftPanel = new JPanel();

        leftPanel.setBackground(DARK);

        leftPanel.setLayout(
                new BoxLayout(
                        leftPanel,
                        BoxLayout.Y_AXIS
                )
        );

        leftPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        55, 40, 40, 40
                )
        );

        JLabel logo = new JLabel("⚕");

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        75
                )
        );

        logo.setForeground(
                new Color(70, 220, 200)
        );

        logo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        leftPanel.add(logo);

        leftPanel.add(
                Box.createVerticalStrut(8)
        );

        JLabel brand =
                new JLabel("MEDICARE");

        brand.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        34
                )
        );

        brand.setForeground(Color.WHITE);

        brand.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        leftPanel.add(brand);

        JLabel pharmacy =
                new JLabel("PHARMACY");

        pharmacy.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        pharmacy.setForeground(
                new Color(180, 220, 215)
        );

        pharmacy.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        leftPanel.add(pharmacy);

        leftPanel.add(
                Box.createVerticalStrut(35)
        );

        JLabel welcome =
                new JLabel(
                        "Your Health, Our Priority"
                );

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        welcome.setForeground(Color.WHITE);

        welcome.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        leftPanel.add(welcome);

        leftPanel.add(
                Box.createVerticalStrut(12)
        );

        JLabel description =
                new JLabel(
                        "<html><div style='text-align:center;'>"
                                + "Manage medicines, inventory,<br>"
                                + "billing and sales easily."
                                + "</div></html>"
                );

        description.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        description.setForeground(
                new Color(200, 225, 222)
        );

        description.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        leftPanel.add(description);

        leftPanel.add(
                Box.createVerticalGlue()
        );

        JLabel copyright =
                new JLabel(
                        "MediCare Pharmacy © 2026"
                );

        copyright.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        copyright.setForeground(
                new Color(150, 190, 185)
        );

        copyright.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        leftPanel.add(copyright);

        add(leftPanel);

        // =====================================================
        // RIGHT SIDE - LOGIN
        // =====================================================

        JPanel rightPanel = new JPanel();

        rightPanel.setBackground(BACKGROUND);

        rightPanel.setLayout(
                new GridBagLayout()
        );

        JPanel loginCard = new JPanel();

        loginCard.setBackground(Color.WHITE);

        loginCard.setPreferredSize(
                new Dimension(390, 410)
        );

        loginCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225, 230, 235
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                30, 35, 30, 35
                        )
                )
        );

        loginCard.setLayout(
                new BoxLayout(
                        loginCard,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // LOGIN TITLE
        // =====================================================

        JLabel loginTitle =
                new JLabel("Welcome Back!");

        loginTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        loginTitle.setForeground(DARK);

        loginTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginCard.add(loginTitle);

        loginCard.add(
                Box.createVerticalStrut(7)
        );

        JLabel subtitle =
                new JLabel(
                        "Sign in to access your dashboard"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(MUTED);

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginCard.add(subtitle);

        loginCard.add(
                Box.createVerticalStrut(30)
        );

        // =====================================================
        // USERNAME
        // =====================================================

        JLabel usernameLabel =
                new JLabel("Username");

        usernameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        usernameLabel.setForeground(TEXT);

        usernameLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginCard.add(usernameLabel);

        loginCard.add(
                Box.createVerticalStrut(7)
        );

        usernameField =
                createTextField();

        loginCard.add(usernameField);

        loginCard.add(
                Box.createVerticalStrut(18)
        );

        // =====================================================
        // PASSWORD
        // =====================================================

        JLabel passwordLabel =
                new JLabel("Password");

        passwordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        passwordLabel.setForeground(TEXT);

        passwordLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginCard.add(passwordLabel);

        loginCard.add(
                Box.createVerticalStrut(7)
        );

        passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        passwordField.setPreferredSize(
                new Dimension(300, 40)
        );

        passwordField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        passwordField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        200, 210, 215
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 10, 5, 10
                        )
                )
        );

        loginCard.add(passwordField);

        loginCard.add(
                Box.createVerticalStrut(25)
        );

        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        JButton loginButton =
                new JButton("LOGIN  →");

        loginButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        loginButton.setForeground(Color.WHITE);

        loginButton.setBackground(PRIMARY);

        loginButton.setFocusPainted(false);

        loginButton.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 20, 12, 20
                )
        );

        loginButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        loginButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        loginCard.add(loginButton);

        loginCard.add(
                Box.createVerticalStrut(18)
        );

        // =====================================================
        // SECURITY INFO
        // =====================================================

        JLabel info =
                new JLabel(
                        "🔒  Secure Pharmacy Management System"
                );

        info.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        info.setForeground(MUTED);

        info.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginCard.add(info);

        rightPanel.add(loginCard);

        add(rightPanel);

        // =====================================================
        // LOGIN ACTION
        // =====================================================

        loginButton.addActionListener(
                e -> login()
        );

        passwordField.addActionListener(
                e -> login()
        );

        setVisible(true);
    }

    // =========================================================
    // TEXT FIELD
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
                new Dimension(300, 40)
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
                                        200, 210, 215
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 10, 5, 10
                        )
                )
        );

        return field;
    }

    // =========================================================
    // LOGIN METHOD
    // =========================================================

    private void login() {

        String usernameText =
                usernameField.getText().trim();

        String passwordText =
                new String(
                        passwordField.getPassword()
                );

        // EMPTY USERNAME

        if (usernameText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
            );

            usernameField.requestFocus();

            return;
        }

        // EMPTY PASSWORD

        if (passwordText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter password.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.requestFocus();

            return;
        }

        // =====================================================
        // LOGIN CREDENTIALS  -  checked against the admin table
        // in MySQL. This is the SAME admin row that the website
        // admin panel uses, so there is only one admin password
        // for the whole project and nothing is hardcoded here.
        // =====================================================

        if (checkAdmin(usernameText, passwordText)) {

            JOptionPane.showMessageDialog(
                    this,
                    "✓ Login Successful!",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            new Dashboard();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Username or Password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            passwordField.setText("");

            passwordField.requestFocus();
        }
    }

    // =========================================================
    // Reads the admin row from MySQL and compares the password
    // hash. Returns false if the admin does not exist, the
    // password is wrong, or the database is not reachable.
    // =========================================================

    private boolean checkAdmin(String username, String password) {

        String sql = "SELECT password FROM admin WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cannot connect to the MySQL database.\n" +
                                "Please start MySQL and run database/schema.sql.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return false;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, username);

                try (ResultSet result = statement.executeQuery()) {

                    if (!result.next()) {
                        return false;            // no such admin
                    }

                    String storedHash = result.getString("password");

                    return PasswordUtil.matches(password, storedHash);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Login failed: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new Login()
        );
    }
}

