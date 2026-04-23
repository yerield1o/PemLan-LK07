import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PerpusSystem {
    public static void main(String[] args) {
        List<String> file = FileHandling.readFile("siswa.csv");

        DefaultTableModel model = new DefaultTableModel();

        if (!file.isEmpty()) {
            String header = file.get(0);
            String [] headers = header.split(",");
            for (String h : headers){
                model.addColumn(h);
            }
            for (int i = 1; i < file.size(); i++) {
                String [] row = file.get(i).split(",");
                model.addRow(row);
            }
        } else {
            model.addColumn("NIS");
            model.addColumn("Nama");
            model.addColumn("Alamat");
        }

        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 200));
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel entryPanel = new JPanel(new GridLayout(3, 2, 5, 15));
        entryPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JTextField nisField = new JTextField(10);
        JTextField namaField = new JTextField(10);
        JTextField alamatField = new JTextField(10);

        entryPanel.add(new JLabel("NIS Siswa:"));
        entryPanel.add(nisField);
        entryPanel.add(new JLabel("Nama Siswa:"));
        entryPanel.add(namaField);
        entryPanel.add(new JLabel("Alamat:"));
        entryPanel.add(alamatField);

        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                nisField.setText(model.getValueAt(selectedRow, 0).toString());
                namaField.setText(model.getValueAt(selectedRow, 1).toString());
                alamatField.setText(model.getValueAt(selectedRow, 2).toString());
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        JButton addSiswa = new JButton("Add Siswa");
        addSiswa.addActionListener(e -> {
            ManagerSiswa.addSiswa(model, nisField.getText().trim(), namaField.getText().trim(), alamatField.getText().trim());
        });

        JButton updateSiswa = new JButton("Update Siswa");
        updateSiswa.addActionListener(e -> {
            ManagerSiswa.updateSiswa(model, nisField.getText().trim(), namaField.getText().trim(), alamatField.getText().trim());
        });

        JButton removeSiswa = new JButton("Remove Siswa");
        removeSiswa.addActionListener(e -> {
            ManagerSiswa.deleteSiswa(model, nisField.getText().trim());
            nisField.setText("");
            namaField.setText("");
            alamatField.setText("");
        });

        JButton clearFields = new JButton("Clear Text");
        clearFields.addActionListener(e -> {
            nisField.setText("");
            namaField.setText("");
            alamatField.setText("");
            table.clearSelection();
        });

        buttonPanel.add(addSiswa);
        buttonPanel.add(updateSiswa);
        buttonPanel.add(removeSiswa);
        buttonPanel.add(clearFields);

        JPanel leftSidePanel = new JPanel(new BorderLayout());
        leftSidePanel.add(entryPanel, BorderLayout.NORTH);
        leftSidePanel.add(buttonPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Aplikasi CRUD Perpustakaan SMP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JFrame frame = new JFrame("Perpus Systems");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 450);
        frame.setLayout(new BorderLayout());

        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(leftSidePanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
