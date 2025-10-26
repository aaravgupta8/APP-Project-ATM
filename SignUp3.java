import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

class SignUp3 extends JFrame implements ActionListener {

    JButton submit, cancel;
    JRadioButton savingAcc, currentAcc, fdAcc, reccAcc;
    JCheckBox atmCard, internetBanking, mobileBanking, alerts, checqueBook, Estatement, declaration;
    String formno;

    SignUp3(String formno) {
        this.formno = formno;
        setLayout(null);

        JLabel addDetails = new JLabel("Page 3: Additional Details");
        addDetails.setBounds(180, 30, 300, 40);
        addDetails.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(addDetails);

        JLabel accType = new JLabel("Account Type:");
        accType.setBounds(60, 80, 200, 30);
        accType.setFont(new Font("Calibri", Font.BOLD, 18));
        add(accType);

        savingAcc = new JRadioButton("Saving Account");
        savingAcc.setBounds(60, 120, 200, 30);
        savingAcc.setBackground(Color.white);
        savingAcc.setFont(new Font("Calibri", Font.PLAIN, 14));
        add(savingAcc);

        fdAcc = new JRadioButton("Fixed Deposit Account");
        fdAcc.setBackground(Color.white);
        fdAcc.setBounds(300, 120, 220, 30);
        fdAcc.setFont(new Font("Calibri", Font.PLAIN, 14));
        add(fdAcc);

        currentAcc = new JRadioButton("Current Account");
        currentAcc.setBackground(Color.white);
        currentAcc.setBounds(60, 150, 200, 30);
        currentAcc.setFont(new Font("Calibri", Font.PLAIN, 14));
        add(currentAcc);

        reccAcc = new JRadioButton("Recurring Deposit Account");
        reccAcc.setBackground(Color.white);
        reccAcc.setBounds(300, 150, 260, 30);
        reccAcc.setFont(new Font("Calibri", Font.PLAIN, 14));
        add(reccAcc);

        ButtonGroup accTypegrp = new ButtonGroup();
        accTypegrp.add(savingAcc);
        accTypegrp.add(currentAcc);
        accTypegrp.add(fdAcc);
        accTypegrp.add(reccAcc);

        JLabel cardNumeber = new JLabel("Card Number:");
        cardNumeber.setBounds(60, 190, 200, 30);
        cardNumeber.setFont(new Font("Calibri", Font.BOLD, 18));
        add(cardNumeber);

        JLabel dummyCardNum = new JLabel("XXXX-XXXX-XXXX-4563");
        dummyCardNum.setBounds(300, 190, 300, 30);
        dummyCardNum.setFont(new Font("Calibri", Font.BOLD, 18));
        add(dummyCardNum);

        JLabel digitCardNum = new JLabel("Your 16 digit Card Number");
        digitCardNum.setBounds(60, 210, 200, 30);
        digitCardNum.setFont(new Font("Calibri", Font.PLAIN, 10));
        add(digitCardNum);

        JLabel pinNumber = new JLabel("PIN:");
        pinNumber.setBounds(60, 250, 200, 30);
        pinNumber.setFont(new Font("Calibri", Font.BOLD, 18));
        add(pinNumber);

        JLabel dummypinNum = new JLabel("XXXX");
        dummypinNum.setBounds(300, 250, 300, 30);
        dummypinNum.setFont(new Font("Calibri", Font.BOLD, 18));
        add(dummypinNum);

        JLabel digitPinNum = new JLabel("Your 4 digit Pin Number");
        digitPinNum.setBounds(60, 270, 200, 30);
        digitPinNum.setFont(new Font("Calibri", Font.PLAIN, 10));
        add(digitPinNum);

        JLabel services = new JLabel("Services Required:");
        services.setBounds(60, 300, 200, 30);
        services.setFont(new Font("Calibri", Font.BOLD, 18));
        add(services);

        atmCard = new JCheckBox("ATM CARD");                atmCard.setBounds(60, 340, 200, 30);   atmCard.setBackground(Color.white);   add(atmCard);
        internetBanking = new JCheckBox("Internet Banking");internetBanking.setBounds(300, 340, 200, 30); internetBanking.setBackground(Color.white); add(internetBanking);
        mobileBanking = new JCheckBox("Mobile Banking");    mobileBanking.setBounds(60, 380, 200, 30);   mobileBanking.setBackground(Color.white);   add(mobileBanking);
        alerts = new JCheckBox("EMAIL & SMS Alerts");       alerts.setBounds(300, 380, 220, 30);  alerts.setBackground(Color.white);     add(alerts);
        checqueBook = new JCheckBox("Cheque Book");         checqueBook.setBounds(60, 420, 200, 30);     checqueBook.setBackground(Color.white);     add(checqueBook);
        Estatement = new JCheckBox("E-Statement");          Estatement.setBounds(300, 420, 200, 30);     Estatement.setBackground(Color.white);      add(Estatement);

        declaration = new JCheckBox("I hereby declare that the above entered details are correct.");
        declaration.setBounds(20, 480, 600, 30);
        declaration.setFont(new Font("Calibri", Font.PLAIN, 14));
        declaration.setBackground(Color.white);
        add(declaration);

        submit = new JButton("Submit");
        submit.setBackground(Color.black);
        submit.setForeground(Color.white);
        submit.setBounds(150, 520, 100, 30);
        submit.setBorder(null);
        submit.setFocusPainted(false);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBackground(Color.black);
        cancel.setForeground(Color.white);
        cancel.setBounds(300, 520, 100, 30);
        cancel.setBorder(null);
        cancel.setFocusPainted(false);
        cancel.addActionListener(this);
        add(cancel);

        setSize(600, 600);
        setVisible(true);
        setResizable(false);
        setLocation(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.white);
        setTitle("New Account Application form - Page 3");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            if (!declaration.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please accept the declaration.");
                return;
            }

            // pick account type
            String accountType = null;
            if (savingAcc.isSelected())      accountType = "Saving Account";
            else if (currentAcc.isSelected())accountType = "Current Account";
            else if (fdAcc.isSelected())     accountType = "Fixed Deposit Account";
            else if (reccAcc.isSelected())   accountType = "Recurring Deposit Account";

            // collect services as CSV
            StringBuilder sb = new StringBuilder();
            if (atmCard.isSelected())        sb.append("ATM Card,");
            if (internetBanking.isSelected())sb.append("Internet Banking,");
            if (mobileBanking.isSelected())  sb.append("Mobile Banking,");
            if (alerts.isSelected())         sb.append("Email & SMS Alerts,");
            if (checqueBook.isSelected())    sb.append("Cheque Book,");
            if (Estatement.isSelected())     sb.append("E-Statement,");
            String services = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";

            // generate card & pin
            Random random = new Random();
            String cardNo = "504093" + String.format("%010d", Math.abs(random.nextLong()) % 1_000_000_0000L);
            String pin    = String.format("%04d", random.nextInt(10000));

            try {
                mysql db = new mysql();
                db.c.setAutoCommit(false);

                // 1) signupthree (5 columns named!)
                String q3 = "INSERT INTO signupthree (formno, account_type, cardno, pin, services) " +
                            "VALUES (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE account_type=VALUES(account_type), cardno=VALUES(cardno), pin=VALUES(pin), services=VALUES(services)";
                try (PreparedStatement ps = db.c.prepareStatement(q3)) {
                    ps.setString(1, formno);
                    ps.setString(2, accountType);
                    ps.setString(3, cardNo);
                    ps.setString(4, pin);
                    ps.setString(5, services);
                    ps.executeUpdate();
                }

                // 2) login (cardno, pin) — 2 columns ONLY
                String qLogin = "INSERT INTO login (cardno, pin) VALUES (?, ?) " +
                                "ON DUPLICATE KEY UPDATE pin=VALUES(pin)";
                try (PreparedStatement ps = db.c.prepareStatement(qLogin)) {
                    ps.setString(1, cardNo);
                    ps.setString(2, pin);
                    ps.executeUpdate();
                }

                // 3) bank (ensure a balance row exists)
                String qBank = "INSERT INTO bank (cardno, balance) VALUES (?, 0) " +
                               "ON DUPLICATE KEY UPDATE balance=balance";
                try (PreparedStatement ps = db.c.prepareStatement(qBank)) {
                    ps.setString(1, cardNo);
                    ps.executeUpdate();
                }

                db.c.commit();

                JOptionPane.showMessageDialog(this, "Account created!\nCard: " + cardNo + "\nPIN: " + pin);
                setVisible(false);
                // if your deposit ctor expects PIN, keep this:
                new deposit(pin).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                try { new mysql().c.rollback(); } catch (Exception ignore) {}
                JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage());
            } finally {
                try { new mysql().c.setAutoCommit(true); } catch (Exception ignore) {}
            }

        } else { // cancel
            JOptionPane.showMessageDialog(this, "Going back to Login page.");
            setVisible(false);
            new LoginPage().setVisible(true);
        }
    }

    public static void main(String[] args) {
        new SignUp3("testForm");
    }
}
