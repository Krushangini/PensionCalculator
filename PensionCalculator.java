import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PensionCalculator extends JFrame {
    private JTextField dobEntry, dojEntry, retEntry, basicEntry, daEntry, hraEntry, ccaEntry, maEntry;
    private JLabel pensionResultLabel, commutationResultLabel, pafCommutationResultLabel, elEncashmentResultLabel;
    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);

     public PensionCalculator() {
        setTitle("Pension Calculator");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel homePanel = new JPanel();
        homePanel.setLayout(new GridLayout(20, 1, 5, 5));

        addLabelAndInput(homePanel, "Date of Birth (dd/mm/yyyy):", dobEntry = new JTextField());
        addLabelAndInput(homePanel, "Date of Joining (dd/mm/yyyy):", dojEntry = new JTextField());
        addLabelAndInput(homePanel, "Retirement Date (dd/mm/yyyy):", retEntry = new JTextField());
        addLabelAndInput(homePanel, "Basic pay on retirement:", basicEntry = new JTextField());
        addLabelAndInput(homePanel, "DA on retirement:", daEntry = new JTextField());
        addLabelAndInput(homePanel, "HRA on retirement:", hraEntry = new JTextField());
        addLabelAndInput(homePanel, "CCA on retirement:", ccaEntry = new JTextField());
        addLabelAndInput(homePanel, "MA on retirement:", maEntry = new JTextField());

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    calculatePension();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        homePanel.add(calculateButton);
        homePanel.add(clearButton);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(8, 1));

        pensionResultLabel = new JLabel("Full Pension:");
        pensionResultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        commutationResultLabel = new JLabel("Commutation Amount:");
        commutationResultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pafCommutationResultLabel = new JLabel("Pension after Commutation:");
        pafCommutationResultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        elEncashmentResultLabel = new JLabel("EL Encashment:");
        elEncashmentResultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JButton back = new JButton("Back");

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                cardLayout.show(cardPanel,"home");
            }
        });

        resultPanel.add(pensionResultLabel);
        resultPanel.add(new JLabel());
        resultPanel.add(commutationResultLabel);
        resultPanel.add(new JLabel());
        resultPanel.add(pafCommutationResultLabel);
        resultPanel.add(new JLabel());
        resultPanel.add(elEncashmentResultLabel);
        resultPanel.add(new JLabel());
        resultPanel.add(back);

        cardPanel.add(homePanel, "home");
        cardPanel.add(resultPanel, "result");

        cardLayout.show(cardPanel, "home");

        add(cardPanel);
        setVisible(true);
    }

    private void addLabelAndInput(JPanel panel, String labelText, JComponent component) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel label = new JLabel(labelText);

        row.add(label, BorderLayout.WEST);
        int width = 100;
        int height = 20;

        component.setPreferredSize(new Dimension(width, height));

        row.add(component, BorderLayout.CENTER);
        panel.add(row);
    }

    private void calculatePension() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dob = dateFormat.parse(dobEntry.getText());
        Date doj = dateFormat.parse(dojEntry.getText());
        Date dor = dateFormat.parse(retEntry.getText());

        // Perform the pension calculations here
        int serviceYears = (int) ((dor.getTime() - doj.getTime()) / (1000 * 60 * 60 * 24 * 365.25));
        int basic = Integer.parseInt(basicEntry.getText());
        double fullPension = (basic / 2.0) * (serviceYears / 60.0);
        double oneThirdPension = fullPension / 3.0;
        double pensionAfterCommutation = fullPension - oneThirdPension;
        double da = Double.parseDouble(daEntry.getText());
        double hra = Double.parseDouble(hraEntry.getText());
        double cca = Double.parseDouble(ccaEntry.getText());
        double ma = Double.parseDouble(maEntry.getText());
        int retMonth = dor.getMonth() + 1;
        int daysInMonth = getDaysInMonth(retMonth, dor.getYear() + 1900);
        double payPerDay = (basic + da + hra + cca + ma) / daysInMonth;
        double elEncashment = 240 * payPerDay;

        // Update the result labels accordingly
        pensionResultLabel.setText("Full Pension: " + String.format("%.2f", fullPension));
        commutationResultLabel.setText("Commutation Amount: " + String.format("%.2f", oneThirdPension));
        pafCommutationResultLabel.setText("Pension after Commutation: " + String.format("%.2f", pensionAfterCommutation));
        elEncashmentResultLabel.setText("EL Encashment: " + String.format("%.2f", elEncashment));

        // Switch to the result panel
        cardLayout.show(cardPanel, "result");
    }

    private void clearFields() {
        dobEntry.setText("");
        dojEntry.setText("");
        retEntry.setText("");
        basicEntry.setText("");
        daEntry.setText("");
        hraEntry.setText("");
        ccaEntry.setText("");
        maEntry.setText("");
        pensionResultLabel.setText("Full Pension:");
        commutationResultLabel.setText("Commutation Amount:");
        pafCommutationResultLabel.setText("Pension after Commutation:");
        elEncashmentResultLabel.setText("EL Encashment:");
    }

    private int getDaysInMonth(int month, int year) {
        int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month == 2 && (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))) {
            return 29; // Leap year
        }
        return daysInMonth[month];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PensionCalculator());
    }
}
