import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagerSiswa {
    public static String namaFile = "siswa.csv";
    private static Scanner scan = new Scanner(System.in);

    public static void addSiswa(DefaultTableModel model) {
        JPanel addPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField nissiswa = new JTextField(10);
        JTextField namasiswa = new JTextField(10);
        JTextField alamatsiswa = new JTextField(10);


        addPanel.add(new JLabel("Masukkan NIS siswa: "));
        addPanel.add(nissiswa);

        addPanel.add(new JLabel("Masukkan nama siswa: "));
        addPanel.add(namasiswa);

        addPanel.add(new JLabel("Masukkan alamat siswa: "));
        addPanel.add(alamatsiswa);

        int result = JOptionPane.showConfirmDialog(
                null, addPanel, "Tambahkan data siswa baru?", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION){
                String nis =  nissiswa.getText().trim();
                String nama = namasiswa.getText().trim();
                String alamat = alamatsiswa.getText().trim();

                if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Semua kolom harus diisi!",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                boolean nissama = false;
                List<String> existingData = FileHandling.readFile(namaFile);

                for (String line : existingData) {
                    if (line.trim().isEmpty()) {continue;}
                    String [] info = line.split(",");
                    if (info.length > 0 && info[0].equalsIgnoreCase(nis)) {
                        nissama = true;
                        break;
                    }
                }

                if (nissama){
                    JOptionPane.showMessageDialog(
                            null,
                            "Error: NIS sudah ada!",
                            "NIS yang sama",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                else {
                    String siswa = nis + "," + nama + "," + alamat;
                    FileHandling.writeFile(namaFile, siswa, true);
                    model.addRow(new String[]{nis, nama, alamat});
                    JOptionPane.showMessageDialog(null, "Data siswa berhasil ditambahkan!");
                }
        }
    }

    public static void updateSiswa(DefaultTableModel model) {
        String nis = JOptionPane.showInputDialog(
                null,
                "Masukkan NIS siswa yang ingin diupdate:",
                "Update Data Siswa",
                JOptionPane.QUESTION_MESSAGE
        );

        if (nis == null || nis.trim().isEmpty()) {
            return;
        }
        nis = nis.trim();

        List<String> siswa = FileHandling.readFile(namaFile);
        boolean ditemukan = false;
        String namabaru = "";
        String alamatabaru = "";
        int index = 0;
        for (int i = siswa.size() - 1; i >= 0; i--) {
            String line = siswa.get(i);
            if (line.trim().isEmpty()) continue;
            String[] info = siswa.get(i).split(",");
            if (info.length == 3 && info[0].equals(nis)) {
                ditemukan = true;
                index = i;
                break;
            }
        }
        if (!ditemukan) {
            JOptionPane.showMessageDialog(
                    null,
                    "Siswa dengan NIS '" + nis + "' tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JPanel updatePanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField namaField = new JTextField();
        JTextField alamatField = new JTextField();
        updatePanel.add(new JLabel("Ubah Nama:"));
        updatePanel.add(namaField);
        updatePanel.add(new JLabel("Ubah Alamat:"));
        updatePanel.add(alamatField);

        int result = JOptionPane.showConfirmDialog(
                null,
                updatePanel,
                "Update Data Siswa",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String newNama = namaField.getText().trim();
            String newAlamat = alamatField.getText().trim();

            if (newNama.isEmpty() || newAlamat.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kolom tidak boleh kosong!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String updatedSiswa = nis + "," + newNama + "," + newAlamat;
            siswa.set(index, updatedSiswa); // Replaces the old line with the new one
            FileHandling.rewriteFile(namaFile, siswa);
            for (int i = 0; i < model.getRowCount(); i++) {
                String tableNis = model.getValueAt(i, 0).toString();
                if (tableNis.equalsIgnoreCase(nis)) {
                    model.setValueAt(newNama, i, 1);   // Column 1 is Nama
                    model.setValueAt(newAlamat, i, 2); // Column 2 is Alamat
                    break;
                }
            }
            JOptionPane.showMessageDialog(null,
                    "Data siswa berhasil diperbarui!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void deleteSiswa(DefaultTableModel model) {
        String nis = JOptionPane.showInputDialog(
                null,
                "Masukkan NIS siswa yang ingin dihapus:",
                "Hapus Siswa",
                JOptionPane.QUESTION_MESSAGE
        );

        if (nis == null || nis.trim().isEmpty()) {
            return;
        }
        nis = nis.trim();



        List <String> siswa = FileHandling.readFile(namaFile);
        boolean ditemukan = false;
        for (int i = siswa.size() - 1; i >= 0; i--) {
            String[] info = siswa.get(i).split(",");
            if (info.length == 3 && info[0].equals(nis)) {
                ditemukan = true;
                siswa.remove(i);
                break;
            }
        }
        if (ditemukan){
            FileHandling.rewriteFile(namaFile, siswa);
            for (int i = 0; i < model.getRowCount(); i++) {
                String nistabel = model.getValueAt(i, 0).toString();

                if (nistabel.equalsIgnoreCase(nis)) {
                    model.removeRow(i);
                    break;
                }
            }
            JOptionPane.showMessageDialog(
                    null,
                    "Data siswa berhasil dihapus!",
                    "Menghapus Data",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        else{
            JOptionPane.showMessageDialog(
                    null,
                    "Siswa dengan NIS '" + nis + "' tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}