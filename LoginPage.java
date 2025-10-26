import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener {

    private JButton signin, signup, clear;
    private JTextField cardfield;
    private JPasswordField pinfield;

    public LoginPage() {
        setLayout(null);

        // (Optional) bank icon
        try {
            ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("images/bank1.png"));
            Image i2 = img.getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT);
            JLabel label = new JLabel(new ImageIcon(i2));
            label.setBounds(100,40,80,80);
            add(label);
        } catch (Exception e) {
            System.out.println("Image not found!");
        }

        JLabel text  = new JLabel("Welcome to King_ATM");
        text.setBounds(250,60,1000,60);
        text.setFont(new Font("Times New Roman", Font.BOLD, 30));
        add(text);

        JLabel card  = new JLabel("Card Number:");
        card.setBounds(170,200,400,30);
        card.setFont(new Font("Times New Roman", Font.BOLD, 26));
        add(card);

        cardfield = new JTextField();
        cardfield.setBounds(400,200,200,30);
        add(cardfield);

        JLabel pin  = new JLabel("Pin:");
        pin.setBounds(170,250,400,30);
        pin.setFont(new Font("Times New Roman", Font.BOLD, 26));
        add(pin);

        pinfield = new JPasswordField();
        pinfield.setBounds(400,250,200,30);
        add(pinfield);

        signin = new JButton("SIGN IN");
        signin.setBounds(300,350,100,30);
        signin.setBackground(Color.black);
        signin.setForeground(Color.white);
        signin.setBorderPainted(false);
        signin.setFocusPainted(false);
        signin.addActionListener(this);
        add(signin);

        clear = new JButton("CLEAR");
        clear.setBounds(420,350,100,30);
        clear.setBackground(Color.black);
        clear.setForeground(Color.white);
        clear.setBorderPainted(false);
        clear.setFocusPainted(false);
        clear.addActionListener(this);
        add(clear);

        signup = new JButton("SIGN UP");
        signup.setBounds(300,400,220,30);
        signup.setBackground(Color.black);
        signup.setForeground(Color.white);
        signup.setBorderPainted(false);
        signup.setFocusPainted(false);
        signup.addActionListener(this);
        add(signup);

        setSize(800,600);
        setResizable(false);
        setLocation(400,100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.white);
        setTitle("ATM Management System");
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == signin) {
            String enteredCard = cardfield.getText().replaceAll("[^0-9]", "").trim();
            String enteredPin  = new String(pinfield.getPassword()).trim();
            if (enteredCard.isEmpty() || enteredPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter card number and PIN");
                return;
            }
            try {
                mysql db = new mysql();
                String sql = "SELECT pin FROM login WHERE cardno = ?";
                PreparedStatement ps = db.c.prepareStatement(sql);
                ps.setString(1, enteredCard);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Card not found");
                    return;
                }
                String dbPin = rs.getString(1);
                if (!enteredPin.equals(dbPin)) {
                    JOptionPane.showMessageDialog(this, "Invalid PIN");
                    return;
                }
                setVisible(false);
                new transaction(enteredCard).setVisible(true); // pass CARD NO forward
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage());
            }
        } else if (ae.getSource() == clear) {
            cardfield.setText("");
            pinfield.setText("");
        } else if (ae.getSource() == signup) {
            try {
                // Debug toast (can remove later): proves the click is wired
                // JOptionPane.showMessageDialog(this, "Opening SignUp1...");
                setVisible(false);
                new SignUp1().setVisible(true);
            } catch (Throwable t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to open SignUp1:\n" + t.getClass().getName() + ": " + t.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
