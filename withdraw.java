import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

class withdraw extends JFrame implements ActionListener {

    JTextField amount;
    JButton back, withdraw;
    String cardNo;

    // turn this off after things work
    private static final boolean DEBUG = false;

    withdraw(String cardNo) {
        this.cardNo = cardNo;
        setLayout(null);

        // if (DEBUG) JOptionPane.showMessageDialog(this, "withdraw got cardNo = " + cardNo);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("images/atm.jpg"));
        Image i1 = img.getImage().getScaledInstance(900,900, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i1));
        image.setBounds(0,0,900,800);
        add(image);

        JLabel heading = new JLabel("Enter the amount to withdraw");
        heading.setBounds(220, 250, 400,50);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 20));
        heading.setForeground(new Color(103, 155, 104));
        image.add(heading);

        amount = new JTextField();
        amount.setFont(new Font("Times New Roman", Font.BOLD, 18));
        amount.setBounds(200, 300, 280, 30);
        image.add(amount);

        withdraw = new JButton("Withdraw");
        withdraw.setBounds(400, 430, 100, 25);
        withdraw.setBackground(new Color(103, 155, 104));
        withdraw.setBorder(null);
        withdraw.setFocusPainted(false);
        withdraw.addActionListener(this);
        image.add(withdraw);

        back = new JButton("Back");
        back.setBounds(400, 460, 100, 25);
        back.setBackground(new Color(103, 155, 104));
        back.setBorder(null);
        back.setFocusPainted(false);
        back.addActionListener(this);
        image.add(back);

        setSize(900,800);
        setLocation(300,50);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            setVisible(false);
            new transaction(cardNo).setVisible(true);
            return;
        }

        if (ae.getSource() == withdraw) {
            long amt;
            try {
                amt = Long.parseLong(amount.getText().trim());
                if (amt <= 0) throw new NumberFormatException();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid positive amount.");
                return;
            }

            try {
                mysql db = new mysql();
                db.c.setAutoCommit(false);

                // ensure a row exists (prevents “account not found”)
                try (PreparedStatement p = db.c.prepareStatement(
                        "INSERT INTO bank (cardno, balance) VALUES (?,0) ON DUPLICATE KEY UPDATE balance=balance")) {
                    p.setString(1, cardNo);
                    p.executeUpdate();
                }

                long before = 0;
                try (PreparedStatement chk = db.c.prepareStatement(
                        "SELECT balance FROM bank WHERE cardno=? FOR UPDATE")) {
                    chk.setString(1, cardNo);
                    ResultSet rs = chk.executeQuery();
                    if (!rs.next()) {
                        db.c.rollback();
                        JOptionPane.showMessageDialog(this, "Account row still missing for card " + cardNo);
                        return;
                    }
                    before = rs.getLong(1);
                }

                if (DEBUG) JOptionPane.showMessageDialog(this, "Balance BEFORE withdraw = " + before);

                if (before < amt) {
                    db.c.rollback();
                    JOptionPane.showMessageDialog(this, "Insufficient balance. Have: " + before + ", need: " + amt);
                    return;
                }

                try (PreparedStatement upd = db.c.prepareStatement(
                        "UPDATE bank SET balance = balance - ? WHERE cardno=?")) {
                    upd.setLong(1, amt);
                    upd.setString(2, cardNo);
                    upd.executeUpdate();
                }

                long after = 0;
                try (PreparedStatement q = db.c.prepareStatement(
                        "SELECT balance FROM bank WHERE cardno=?")) {
                    q.setString(1, cardNo);
                    ResultSet rs = q.executeQuery();
                    if (rs.next()) after = rs.getLong(1);
                }

                if (DEBUG) JOptionPane.showMessageDialog(this, "Balance AFTER withdraw = " + after);

                // if your transactions table doesn't have balance_after, remove that column/param
                try (PreparedStatement ins = db.c.prepareStatement(
                        "INSERT INTO transactions (cardno, ttype, amount, balance_after, ts) " +
                        "VALUES (?, 'WITHDRAW', ?, ?, NOW())")) {
                    ins.setString(1, cardNo);
                    ins.setLong(2, amt);
                    ins.setLong(3, after);
                    ins.executeUpdate();
                }

                db.c.commit();
                JOptionPane.showMessageDialog(this, "Rs. " + amt + " withdrawn successfully.");
                setVisible(false);
                new transaction(cardNo).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] a) {
        new withdraw("1234567890123456");
    }
}
