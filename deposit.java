import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

class deposit extends JFrame implements ActionListener {

    JTextField amount;
    JButton back, deposit;
    String cardNo;

    deposit(String cardNo) {
        this.cardNo = cardNo;
        setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("images/atm.jpg"));
        Image i1 = img.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i1));
        image.setBounds(0, 0, 900, 800);
        add(image);

        JLabel heading = new JLabel("Enter the amount to deposit");
        heading.setBounds(220, 250, 400, 50);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 20));
        heading.setForeground(new Color(103, 155, 104));
        image.add(heading);

        amount = new JTextField();
        amount.setFont(new Font("Times New Roman", Font.BOLD, 18));
        amount.setBounds(200, 300, 280, 30);
        image.add(amount);

        deposit = new JButton("Deposit");
        deposit.setBounds(400, 430, 100, 25);
        deposit.setBackground(new Color(103, 155, 104));
        deposit.setBorder(null);
        deposit.setFocusPainted(false);
        deposit.addActionListener(this);
        image.add(deposit);

        back = new JButton("Back");
        back.setBounds(400, 460, 100, 25);
        back.setBackground(new Color(103, 155, 104));
        back.setBorder(null);
        back.setFocusPainted(false);
        back.addActionListener(this);
        image.add(back);

        setSize(900, 800);
        setLocation(300, 50);
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

        if (ae.getSource() == deposit) {
            String text = amount.getText().trim();
            long money;
            try { money = Long.parseLong(text); if (money <= 0) throw new NumberFormatException(); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Enter a valid positive amount."); return; }

            try {
                mysql db = new mysql();
                db.c.setAutoCommit(false);

                // ensure a bank row exists
                try (PreparedStatement p = db.c.prepareStatement(
                        "INSERT INTO bank (cardno, balance) VALUES (?,0) ON DUPLICATE KEY UPDATE balance=balance")) {
                    p.setString(1, cardNo); p.executeUpdate();
                }

                // add money
                try (PreparedStatement upd = db.c.prepareStatement(
                        "UPDATE bank SET balance = balance + ? WHERE cardno = ?")) {
                    upd.setLong(1, money); upd.setString(2, cardNo); upd.executeUpdate();
                }

                long newBal = 0;
                try (PreparedStatement q = db.c.prepareStatement(
                        "SELECT balance FROM bank WHERE cardno = ?")) {
                    q.setString(1, cardNo);
                    ResultSet rs = q.executeQuery();
                    if (rs.next()) newBal = rs.getLong(1);
                }

                // INSERT transaction (if your table has no balance_after, remove that column and param)
                try (PreparedStatement ins = db.c.prepareStatement(
                        "INSERT INTO transactions (cardno, ttype, amount, balance_after, ts) " +
                        "VALUES (?, 'DEPOSIT', ?, ?, NOW())")) {
                    ins.setString(1, cardNo);
                    ins.setLong(2, money);
                    ins.setLong(3, newBal);
                    ins.executeUpdate();
                }

                db.c.commit();
                JOptionPane.showMessageDialog(this, "Rs. " + money + " deposited successfully.");
                setVisible(false);
                new transaction(cardNo).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new deposit("1234567890123456");
    }
}
