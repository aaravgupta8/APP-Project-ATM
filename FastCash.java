import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

class FastCash extends JFrame implements ActionListener {

    JButton rs100, rs500, rs1000, rs2000, rs5000, rs10000, back;
    String cardNo;

    FastCash(String cardNo) {
        this.cardNo = cardNo;
        setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("images/atm.jpg"));
        Image i1 = img.getImage().getScaledInstance(900,900, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i1));
        image.setBounds(0,0,900,800);
        add(image);

        JLabel heading = new JLabel("SELECT WITHDRAWAL AMOUNT");
        heading.setBounds(190, 250, 400,50);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 20));
        heading.setForeground(new Color(103, 155, 104));
        image.add(heading);

        rs100   = mk("Rs. 100",   180,370,image);
        rs500   = mk("Rs. 500",   400,370,image);
        rs1000  = mk("Rs. 1000",  180,400,image);
        rs2000  = mk("Rs. 2000",  400,400,image);
        rs5000  = mk("Rs. 5000",  180,430,image);
        rs10000 = mk("Rs. 10000", 400,430,image);

        back = new JButton("Back");
        back.setBounds(400,460,100,25);
        back.setBackground(new Color(103,155,104));
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

    private JButton mk(String t, int x, int y, JComponent p){
        JButton b = new JButton(t);
        b.setBounds(x,y,100,25);
        b.setBackground(new Color(103,155,104));
        b.setBorder(null);
        b.setFocusPainted(false);
        b.addActionListener(this);
        p.add(b);
        return b;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            setVisible(false);
            new transaction(cardNo).setVisible(true);
            return;
        }

        long amt = Long.parseLong(((JButton)ae.getSource()).getText().substring(4));
        try {
            mysql db = new mysql();
            db.c.setAutoCommit(false);

            long bal = 0;
            try (PreparedStatement chk = db.c.prepareStatement(
                    "SELECT balance FROM bank WHERE cardno=? FOR UPDATE")) {
                chk.setString(1, cardNo);
                ResultSet rs = chk.executeQuery();
                if (!rs.next()) { JOptionPane.showMessageDialog(this,"Account not found"); db.c.rollback(); return; }
                bal = rs.getLong(1);
            }
            if (bal < amt) { JOptionPane.showMessageDialog(this,"Insufficient balance"); db.c.rollback(); return; }

            try (PreparedStatement upd = db.c.prepareStatement(
                    "UPDATE bank SET balance = balance - ? WHERE cardno=?")) {
                upd.setLong(1, amt); upd.setString(2, cardNo); upd.executeUpdate();
            }

            long newBal = 0;
            try (PreparedStatement q = db.c.prepareStatement(
                    "SELECT balance FROM bank WHERE cardno=?")) {
                q.setString(1, cardNo);
                ResultSet rs = q.executeQuery();
                if (rs.next()) newBal = rs.getLong(1);
            }

            try (PreparedStatement ins = db.c.prepareStatement(
                    "INSERT INTO transactions (cardno, ttype, amount, balance_after, ts) VALUES (?, 'WITHDRAW', ?, ?, NOW())")) {
                ins.setString(1, cardNo);
                ins.setLong(2, amt);
                ins.setLong(3, newBal);
                ins.executeUpdate();
            }

            db.c.commit();
            JOptionPane.showMessageDialog(this, "Rs. " + amt + " withdrawn.");
            setVisible(false);
            new transaction(cardNo).setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
        }
    }

    public static void main(String[] a){ new FastCash("1234567890123456"); }
}
