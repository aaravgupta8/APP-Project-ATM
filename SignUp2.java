import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

class SignUp2 extends JFrame implements ActionListener {

    JTextField aadharfield, panfield;
    JRadioButton seniorYes, seniorNo, existYes, existNo;
    JButton next;
    JComboBox<String> religiondbox, categorydbox, incomedbox, qualdbox, occupationdbox;
    String formNo;

    SignUp2(String formNo) {
        this.formNo = formNo;
        setLayout(null);

        JLabel addDetails = new JLabel("Page 2: Additional Details");
        addDetails.setBounds(180, 30, 300, 40);
        addDetails.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        add(addDetails);

        // Religion
        JLabel religion = new JLabel("Religion:");
        religion.setBounds(60, 100, 200, 30);
        religion.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(religion);

        String[] valReligion = { "Hindu", "Muslim", "Sikh", "Christian", "Others" };
        religiondbox = new JComboBox<>(valReligion);
        religiondbox.setBounds(200, 100, 300, 20);
        religiondbox.setBackground(Color.white);
        add(religiondbox);

        // Category
        JLabel category = new JLabel("Category:");
        category.setBounds(60, 140, 200, 30);
        category.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(category);

        String[] valCategory = { "General", "SC", "ST", "OBC", "Others" };
        categorydbox = new JComboBox<>(valCategory);
        categorydbox.setBounds(200, 140, 300, 20);
        categorydbox.setBackground(Color.white);
        add(categorydbox);

        // Income
        JLabel income = new JLabel("Income:");
        income.setBounds(60, 180, 200, 30);
        income.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(income);

        String[] valIncome = { "Null", "< 1,50,000>", "< 2,50,000>", "< 5,00,000", "Upto 10,00,000" };
        incomedbox = new JComboBox<>(valIncome);
        incomedbox.setBounds(200, 180, 300, 20);
        incomedbox.setBackground(Color.white);
        add(incomedbox);

        // Qualification
        JLabel qualification = new JLabel("Qualification:");
        qualification.setBounds(60, 220, 200, 30);
        qualification.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(qualification);

        String[] valQual = { "Non-Graduation", "Graduate", "Post-Graduate", "Doctorate", "Others" };
        qualdbox = new JComboBox<>(valQual);
        qualdbox.setBounds(200, 220, 300, 20);
        qualdbox.setBackground(Color.white);
        add(qualdbox);

        // Occupation
        JLabel occupation = new JLabel("Occupation:");
        occupation.setBounds(60, 260, 200, 30);
        occupation.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(occupation);

        String[] valOccup = { "Salaried", "Self-Employed", "Business", "Student", "Retired", "Others" };
        occupationdbox = new JComboBox<>(valOccup);
        occupationdbox.setBounds(200, 260, 300, 20);
        occupationdbox.setBackground(Color.white);
        add(occupationdbox);

        // PAN
        JLabel pan = new JLabel("PAN Number:");
        pan.setBounds(60, 300, 200, 30);
        pan.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(pan);

        panfield = new JTextField();
        panfield.setBounds(200, 300, 300, 20);
        add(panfield);

        // Aadhar
        JLabel aadhar = new JLabel("Aadhar Number:");
        aadhar.setBounds(60, 340, 200, 30);
        aadhar.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(aadhar);

        aadharfield = new JTextField();
        aadharfield.setBounds(200, 340, 300, 20);
        add(aadharfield);

        // Senior citizen
        JLabel senior = new JLabel("Senior Citizen:");
        senior.setBounds(60, 380, 200, 30);
        senior.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(senior);

        seniorYes = new JRadioButton("Yes"); seniorYes.setBackground(Color.white); seniorYes.setBounds(200, 380, 100, 30);
        seniorNo  = new JRadioButton("No");  seniorNo.setBackground(Color.white);  seniorNo.setBounds(300, 380, 100, 30);
        ButtonGroup seniorgrp = new ButtonGroup(); seniorgrp.add(seniorYes); seniorgrp.add(seniorNo);
        add(seniorYes); add(seniorNo);

        // Existing account
        JLabel existing = new JLabel("Existing Account:");
        existing.setBounds(60, 420, 200, 30);
        existing.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(existing);

        existYes = new JRadioButton("Yes"); existYes.setBackground(Color.white); existYes.setBounds(200, 420, 100, 30);
        existNo  = new JRadioButton("No");  existNo.setBackground(Color.white);  existNo.setBounds(300, 420, 100, 30);
        ButtonGroup existinggrp = new ButtonGroup(); existinggrp.add(existYes); existinggrp.add(existNo);
        add(existYes); add(existNo);

        // Next
        next = new JButton("Next");
        next.setBackground(Color.black);
        next.setForeground(Color.white);
        next.setFont(new Font("Raleway", Font.BOLD, 14));
        next.setBounds(400, 500, 100, 30);
        next.setBorder(null);
        next.setFocusPainted(false);
        next.addActionListener(this);
        add(next);

        setSize(600, 600);
        setVisible(true);
        setResizable(false);
        setLocation(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.white);
        setTitle("New Account Application form - Page 2");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // read inputs
        String religion   = (String) religiondbox.getSelectedItem();
        String category   = (String) categorydbox.getSelectedItem();
        String income     = (String) incomedbox.getSelectedItem();
        String education  = (String) qualdbox.getSelectedItem();
        String occupation = (String) occupationdbox.getSelectedItem();
        String pan        = panfield.getText();
        String aadhar     = aadharfield.getText();

        // map radios to 1/0 for TINYINT columns
        int senior   = seniorYes.isSelected() ? 1 : 0;
        int existing = existYes.isSelected()  ? 1 : 0;

        try {
            mysql db = new mysql();

            String sql = "INSERT INTO signuptwo " +
                         "(formno, religion, category, income, education, occupation, pan, aadhar, senior, existing) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = db.c.prepareStatement(sql);
            ps.setString(1,  formNo);
            ps.setString(2,  religion);
            ps.setString(3,  category);
            ps.setString(4,  income);
            ps.setString(5,  education);
            ps.setString(6,  occupation);
            ps.setString(7,  pan);
            ps.setString(8,  aadhar);
            ps.setInt(9,    senior);
            ps.setInt(10,   existing);

            ps.executeUpdate();
            ps.close();

            setVisible(false);
            new SignUp3(formNo);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage());
        }
    }

    // (Optional) for isolated testing:
    public static void main(String[] args) {
        new SignUp2("testFormNo");
    }
}
