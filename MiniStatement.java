import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class MiniStatement extends JFrame {

    private final String cardNo;

    public MiniStatement(String cardNo) {
        this.cardNo = cardNo;

        // Date/time for the header (use java.util.Date explicitly)
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String now = df.format(new java.util.Date());
        String str_date = now.substring(0, 10);
        String str_time = now.substring(11);

        setLayout(null);

        JLabel bankName = new JLabel("King's Bank");
        bankName.setBounds(180, 20, 200, 25);
        bankName.setFont(new Font("Times New Roman", Font.BOLD, 20));
        bankName.setForeground(new Color(156, 91, 194));
        add(bankName);

        JLabel d_date = new JLabel("DATE");
        d_date.setBounds(100, 50, 100, 20);
        d_date.setFont(new Font("Times New Roman", Font.BOLD, 18));
        d_date.setForeground(new Color(156, 91, 194));
        add(d_date);

        JLabel d_time = new JLabel("TIME");
        d_time.setBounds(300, 50, 100, 20);
        d_time.setFont(new Font("Times New Roman", Font.BOLD, 18));
        d_time.setForeground(new Color(156, 91, 194));
        add(d_time);

        JLabel dis_date = new JLabel(" " + str_date + " ");
        dis_date.setBounds(80, 80, 200, 20);
        dis_date.setFont(new Font("Times New Roman", Font.BOLD, 18));
        add(dis_date);

        JLabel dis_time = new JLabel(str_time);
        dis_time.setBounds(280, 80, 100, 20);
        dis_time.setFont(new Font("Times New Roman", Font.BOLD, 18));
        add(dis_time);

        JLabel cardNumDisp = new JLabel();
        cardNumDisp.setBounds(70, 120, 420, 30);
        cardNumDisp.setFont(new Font("Times New Roman", Font.BOLD, 18));
        add(cardNumDisp);

        JLabel types = new JLabel("DATE                         TYPE        AMOUNT");
        types.setBounds(60, 220, 420, 30);
        types.setFont(new Font("Times New Roman", Font.BOLD, 16));
        types.setForeground(new Color(156, 91, 194));
        add(types);

        JLabel details = new JLabel();
        details.setBounds(50, 100, 420, 500);
        details.setFont(new Font("Times New Roman", Font.BOLD, 16));
        add(details);

        JLabel amount = new JLabel();
        amount.setBounds(50, 500, 350, 20);
        amount.setFont(new Font("Times New Roman", Font.BOLD, 20));
        amount.setForeground(new Color(156, 91, 194));
        add(amount);

        // Masked card number
        String masked = (cardNo != null && cardNo.length() >= 16)
                ? cardNo.substring(0, 4) + "XXXXXXXX" + cardNo.substring(12)
                : String.valueOf(cardNo);
        cardNumDisp.setText("CARD NO.  :     " + masked);

        // Fetch last 5 transactions
        try {
            mysql db = new mysql();
            PreparedStatement ps = db.c.prepareStatement(
                "SELECT ts, ttype, amount FROM transactions " +
                "WHERE cardno=? ORDER BY ts DESC LIMIT 5");
            ps.setString(1, cardNo);
            ResultSet rs = ps.executeQuery();

            StringBuilder html = new StringBuilder("<html>");
            SimpleDateFormat out = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss");
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("ts");
                String ttype  = rs.getString("ttype");
                long amt      = rs.getLong("amount");

                html.append(out.format(ts))
                    .append("&nbsp;&nbsp;&nbsp;&nbsp;")
                    .append(ttype.toLowerCase())
                    .append("&nbsp;&nbsp;&nbsp;&nbsp;")
                    .append(amt)
                    .append("<br><br>");
            }
            html.append("</html>");
            details.setText(html.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Txn fetch error: " + e.getMessage());
        }

        // Current balance
        try {
            mysql db = new mysql();
            PreparedStatement ps = db.c.prepareStatement(
                "SELECT balance FROM bank WHERE cardno=?");
            ps.setString(1, cardNo);
            ResultSet rs = ps.executeQuery();
            long bal = 0;
            if (rs.next()) bal = rs.getLong(1);
            amount.setText("Balance:        Rs. " + bal);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Balance fetch error: " + e.getMessage());
        }

        setSize(500, 600);
        setLocation(300, 50);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MiniStatement("1234567890123456");
    }
}
