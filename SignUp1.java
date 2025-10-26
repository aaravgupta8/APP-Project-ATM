import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;

public class SignUp1 extends JFrame implements ActionListener {

    private JTextField namefield, fnamefield, emailfield, addressfield, cityfield, statefield, pincodefield;
    private JButton next;
    private JRadioButton malebtn, femalebtn, othersbtn, married, notMarried, otherMarried;
    private JDateChooser dobfield;
    private String formNo;

    public SignUp1() {
        // JOptionPane.showMessageDialog(this, "SignUp1 opened"); // (optional debug)
        formNo = java.util.UUID.randomUUID().toString().substring(0, 8);

        setLayout(null);

        JLabel heading = new JLabel("Application Form No. " + formNo);
        heading.setBounds(120, 20, 500, 40);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 26));
        add(heading);

        JLabel pageno = new JLabel("Page 1: Personal Details");
        pageno.setBounds(180, 50, 300, 40);
        pageno.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        add(pageno);

        // Name
        JLabel name = new JLabel("Name:");
        name.setBounds(60, 100, 200, 30);
        name.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(name);

        namefield = new JTextField();
        namefield.setBounds(200, 100, 300, 22);
        add(namefield);

        // Father's Name
        JLabel fname = new JLabel("Father's Name:");
        fname.setBounds(60, 140, 200, 30);
        fname.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(fname);

        fnamefield = new JTextField();
        fnamefield.setBounds(200, 140, 300, 22);
        add(fnamefield);

        // DOB
        JLabel dob = new JLabel("Date of Birth:");
        dob.setBounds(60, 180, 200, 30);
        dob.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(dob);

        dobfield = new JDateChooser();
        dobfield.setBounds(200, 180, 300, 22);
        dobfield.setForeground(Color.black);
        dobfield.setDateFormatString("yyyy-MM-dd"); // MySQL DATE format
        add(dobfield);

        // Gender
        JLabel gender = new JLabel("Gender:");
        gender.setBounds(60, 220, 200, 30);
        gender.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(gender);

        malebtn   = new JRadioButton("Male");   malebtn.setBackground(Color.white);
        femalebtn = new JRadioButton("Female"); femalebtn.setBackground(Color.white);
        othersbtn = new JRadioButton("Others"); othersbtn.setBackground(Color.white);

        malebtn.setBounds(200, 220, 80, 20);
        femalebtn.setBounds(300, 220, 100, 20);
        othersbtn.setBounds(400, 220, 100, 20);

        ButtonGroup gendergrp = new ButtonGroup();
        gendergrp.add(malebtn); gendergrp.add(femalebtn); gendergrp.add(othersbtn);

        add(malebtn); add(femalebtn); add(othersbtn);

        // Email
        JLabel email = new JLabel("Email Address:");
        email.setBounds(60, 260, 200, 30);
        email.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(email);

        emailfield = new JTextField();
        emailfield.setBounds(200, 260, 300, 22);
        add(emailfield);

        // Marital Status
        JLabel marital = new JLabel("Marital Status:");
        marital.setBounds(60, 300, 200, 30);
        marital.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(marital);

        married     = new JRadioButton("Married");   married.setBackground(Color.white);
        notMarried  = new JRadioButton("Unmarried"); notMarried.setBackground(Color.white);
        otherMarried= new JRadioButton("Others");    otherMarried.setBackground(Color.white);

        married.setBounds(200, 300, 100, 20);
        notMarried.setBounds(300, 300, 110, 20);
        otherMarried.setBounds(420, 300, 100, 20);

        ButtonGroup marriedgrp = new ButtonGroup();
        marriedgrp.add(married); marriedgrp.add(notMarried); marriedgrp.add(otherMarried);

        add(married); add(notMarried); add(otherMarried);

        // Address
        JLabel address = new JLabel("Address:");
        address.setBounds(60, 340, 200, 30);
        address.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(address);

        addressfield = new JTextField();
        addressfield.setBounds(200, 340, 300, 22);
        add(addressfield);

        // City
        JLabel city = new JLabel("City:");
        city.setBounds(60, 380, 200, 30);
        city.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(city);

        cityfield = new JTextField();
        cityfield.setBounds(200, 380, 300, 22);
        add(cityfield);

        // State
        JLabel state = new JLabel("State:");
        state.setBounds(60, 420, 200, 30);
        state.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(state);

        statefield = new JTextField();
        statefield.setBounds(200, 420, 300, 22);
        add(statefield);

        // Pincode
        JLabel pincode = new JLabel("Pin Code:");
        pincode.setBounds(60, 460, 200, 30);
        pincode.setFont(new Font("Calibri", Font.PLAIN, 18));
        add(pincode);

        pincodefield = new JTextField();
        pincodefield.setBounds(200, 460, 300, 22);
        add(pincodefield);

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
        setResizable(false);
        setLocation(400,100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.white);
        setTitle("Sign Up page");
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // Gather inputs
        String formno = formNo;
        String name   = namefield.getText();
        String fname  = fnamefield.getText();

        String gender = malebtn.isSelected() ? "Male" :
                        (femalebtn.isSelected() ? "Female" : "Others");

        String email   = emailfield.getText();
        String marital = married.isSelected() ? "Married" :
                         (notMarried.isSelected() ? "UnMarried" : "Other");

        String address = addressfield.getText();
        String city    = cityfield.getText();
        String state   = statefield.getText();
        String pincode = pincodefield.getText();

        // Basic validation (optional)
        if (name.trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Enter Name"); return; }
        if (fname.trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Enter Father's Name"); return; }

        try {
            // Convert DOB
            java.util.Date d = dobfield.getDate();
            if (d == null) {
                JOptionPane.showMessageDialog(this, "Please select Date of Birth");
                return;
            }
            java.sql.Date dobSql = new java.sql.Date(d.getTime());

            // Insert into signup table
            mysql db = new mysql();
            String sql = "INSERT INTO signup " +
                         "(formno, name, fname, dob, gender, email, marital, address, city, state, pincode) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = db.c.prepareStatement(sql);
            ps.setString(1,  formno);
            ps.setString(2,  name);
            ps.setString(3,  fname);
            ps.setDate(4,    dobSql);
            ps.setString(5,  gender);
            ps.setString(6,  email);
            ps.setString(7,  marital);
            ps.setString(8,  address);
            ps.setString(9,  city);
            ps.setString(10, state);
            ps.setString(11, pincode);
            ps.executeUpdate();
            ps.close();

            // Move to page 2
            setVisible(false);
            try {
                new SignUp2(formNo).setVisible(true);
            } catch (Throwable t) {
                // helpful message if SignUp2 is missing or has compile errors
                JOptionPane.showMessageDialog(this, "Could not open SignUp2:\n" + t.getClass().getName() + ": " + t.getMessage());
                // fall back to return to login
                new LoginPage().setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new SignUp1();
    }
}
