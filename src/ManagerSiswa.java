import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagerSiswa {
    public static String namaFile = "siswa.csv";
    private static Scanner scan = new Scanner(System.in);

    public static void addSiswa(DefaultTableModel model, String nis, String nama, String alamat) {
        if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua kolom harus diisi!", "Input Error", JOptionPane.WARNING_MESSAGE);
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

        try {
            if (nissama){
                throw new Exception("NIS '" + nis + "' sudah ada di dalam database!");
            }

            String siswa = nis + "," + nama + "," + alamat;
            FileHandling.writeFile(namaFile, siswa, true);
            model.addRow(new String[]{nis, nama, alamat});
            JOptionPane.showMessageDialog(null, "Data siswa berhasil ditambahkan!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "NIS Sama", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void updateSiswa(DefaultTableModel model, String nis, String newNama, String newAlamat) {
        if (nis.isEmpty() || newNama.isEmpty() || newAlamat.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua kolom harus diisi untuk update!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> siswa = FileHandling.readFile(namaFile);
        boolean ditemukan = false;
        int index = -1;

        for (int i = 0; i < siswa.size(); i++) {
            String line = siswa.get(i);
            if (line.trim().isEmpty()) continue;
            String[] info = line.split(",");
            if (info.length == 3 && info[0].equalsIgnoreCase(nis)) {
                ditemukan = true;
                index = i;
                break;
            }
        }

        if (!ditemukan) {
            JOptionPane.showMessageDialog(null, "Siswa dengan NIS '" + nis + "' tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updatedSiswa = nis + "," + newNama + "," + newAlamat;
        siswa.set(index, updatedSiswa);
        FileHandling.rewriteFile(namaFile, siswa);

        for (int i = 0; i < model.getRowCount(); i++) {
            String tableNis = model.getValueAt(i, 0).toString();
            if (tableNis.equalsIgnoreCase(nis)) {
                model.setValueAt(newNama, i, 1);
                model.setValueAt(newAlamat, i, 2);
                break;
            }
        }
        JOptionPane.showMessageDialog(null, "Data siswa berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void deleteSiswa(DefaultTableModel model, String nis) {
        if (nis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "NIS harus diisi untuk menghapus data!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List <String> siswa = FileHandling.readFile(namaFile);
        boolean ditemukan = false;

        for (int i = siswa.size() - 1; i >= 0; i--) {
            String[] info = siswa.get(i).split(",");
            if (info.length == 3 && info[0].equalsIgnoreCase(nis)) {
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
            JOptionPane.showMessageDialog(null, "Data siswa berhasil dihapus!", "Menghapus Data", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Siswa dengan NIS '" + nis + "' tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}