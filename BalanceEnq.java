import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

class BalanceEnq extends JFrame implements ActionListener {

    String cardNo;
    JButton back;

    BalanceEnq(String cardNo) {
        this.cardNo = cardNo;
        setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("images/atm.jpg"));
        Image i1 = img.getImage().getScaledInstance(900,900, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i1));
        image.setBounds(0,0,900,800);
        add(image);

        long balance = 0;
        try {
            mysql db = new mysql();
            PreparedStatement ps = db.c.prepareStatement("SELECT balance FROM bank WHERE cardno=?");
            ps.setString(1, cardNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) balance = rs.getLong(1);
        } catch (Exception e) { e.printStackTrace(); }

        JLabel balance_money = new JLabel("Your current account balance is:");
        balance_money.setBounds(160, 250, 600, 30);
        balance_money.setFont(new Font("Times New Roman", Font.BOLD, 18));
        balance_money.setForeground(new Color(103, 155, 104));
        image.add(balance_money);

        JLabel moneyDisplay = new JLabel("Rs. " + balance);
        moneyDisplay.setBounds(200, 260, 600, 100);
        moneyDisplay.setFont(new Font("Times New Roman", Font.BOLD, 18));
        moneyDisplay.setForeground(new Color(103, 155, 104));
        image.add(moneyDisplay);

        back = new JButton("Back");
        back.setBounds(400, 460, 100, 25);
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

    @Override public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new transaction(cardNo).setVisible(true);
    }

    public static void main(String[] a){ new BalanceEnq("1234567890123456"); }
}
