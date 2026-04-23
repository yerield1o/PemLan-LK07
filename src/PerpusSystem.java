import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PerpusSystem {
    public static void main(String[] args) {
        List<String> file = FileHandling.readFile("siswa.csv");
        if (file.isEmpty()) {
            System.out.println("No file found");
            return;
        }

        DefaultTableModel model = new DefaultTableModel();

        String header = file.get(0);
        String [] headers = header.split(",");

        for (String h : headers){
            model.addColumn(h);
        }

        for (int i = 1; i < file.size(); i++) {
            String [] row = file.get(i).split(",");
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(400, 150));
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonpanels = new JPanel();
        buttonpanels.setLayout(new GridLayout(0, 1));

        JButton addSiswa = new JButton("Add Siswa");
        addSiswa.addActionListener(e -> {
            ManagerSiswa.addSiswa(model);
        });

        JButton removeSiswa = new JButton("Remove Siswa");
        JButton updateSiswa = new JButton("Update Siswa");
        buttonpanels.add(addSiswa);
        buttonpanels.add(removeSiswa);
        buttonpanels.add(updateSiswa);

        JFrame frame = new JFrame("Perpus Systems");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.WEST);
        frame.add(buttonpanels, BorderLayout.EAST);
        frame.setVisible(true);
    }
}
