import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class transaction extends JFrame implements ActionListener {

    JButton deposit, fastCash, withdraw, statement, pinChange, balance, exit;
    String cardNo;   // use card number everywhere

    transaction(String cardNo) {
        this.cardNo = cardNo;
        setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("images/atm.jpg"));
        Image i1 = img.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i1));
        image.setBounds(0, 0, 900, 800);
        add(image);

        JLabel heading = new JLabel("Please select a Transaction");
        heading.setBounds(230, 250, 400, 50);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 18));
        heading.setForeground(Color.white);
        image.add(heading);

        deposit = mkBtn("Deposit", 180, 370, image);
        withdraw = mkBtn("Cash Withdraw", 400, 370, image);
        fastCash = mkBtn("Fast Cash", 180, 400, image);
        statement = mkBtn("Mini Statement", 400, 400, image);
        pinChange = mkBtn("Pin Change", 180, 430, image);
        balance = mkBtn("Balance Enquiry", 400, 430, image);

        exit = mkBtn("Exit", 400, 460, image);

        setSize(900, 800);
        setLocation(300, 50);
        setUndecorated(true);
        setVisible(true);
    }

    private JButton mkBtn(String t, int x, int y, JComponent p) {
        JButton b = new JButton(t);
        b.setBounds(x, y, 120, 25);
        b.setBackground(new Color(103, 155, 104));
        b.setBorder(null);
        b.setFocusPainted(false);
        b.addActionListener(this);
        p.add(b);
        return b;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == exit) {
            System.exit(0);
        } else if (src == deposit) {
            setVisible(false); new deposit(cardNo).setVisible(true);
        } else if (src == withdraw) {
            setVisible(false); new withdraw(cardNo).setVisible(true);
        } else if (src == fastCash) {
            setVisible(false); new FastCash(cardNo).setVisible(true);
        } else if (src == pinChange) {
            setVisible(false); new PinChange(cardNo).setVisible(true); // if you have it
        } else if (src == balance) {
            setVisible(false); new BalanceEnq(cardNo).setVisible(true);
        } else if (src == statement) {
            new MiniStatement(cardNo).setVisible(true);
        }
    }

    public static void main(String[] args) {
        new transaction("1234567890123456");
    }
}
