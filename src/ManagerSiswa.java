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

    public static void viewSiswa() {
        List<String> siswa = FileHandling.readFile(namaFile);

        if(siswa.isEmpty()){
            System.out.println("Tidak ada siswa");
            return;
        }

        System.out.println(" NIS Siswa | Nama Siswa | Alamat Siswa ");

        for (String k : siswa) {
            String[]info = k.split(",");
            if (info.length == 3){
                System.out.println(info[0] + " | " + info[1] + " | " + info[2]);
            }
        }
    }

    public static void updateSiswa() {
        System.out.print("Masukkan NIS siswa: ");
        String nis = scan.nextLine();

        List<String> siswa = FileHandling.readFile(namaFile);
        boolean ditemukan = false;
        for (int i = 0; i < siswa.size(); i++) {
            String namaBaru = "";
            String alamatBaru = "";
            String[] info = siswa.get(i).split(",");
            if (info.length == 3 && info[0].equals(nis)) {
                namaBaru = info[1];
                alamatBaru = info[2];
                ditemukan = true;
                System.out.println("1. Ubah nama");
                System.out.println("2. Ubah alamat");
                System.out.println("Pilihan: ");
                String pilihan = scan.nextLine();
                switch (pilihan) {
                    case "1":
                        System.out.println("Nama siswa baru: ");
                        namaBaru = scan.nextLine();
                        break;
                    case "2":
                        System.out.println("Alamat Siswa Baru: ");
                        alamatBaru = scan.nextLine();
                        break;
                    default:
                        System.out.println("Pilihan Invalid");
                        break;
                }
                String siswaBaru = nis + "," + namaBaru + "," + alamatBaru;
                siswa.set(i, siswaBaru);
                break;
            }
        }
        if (ditemukan){
            FileHandling.rewriteFile(namaFile, siswa);
            System.out.println("Siswa berhasil diperbaharui");
        }
        else{
            System.out.println("Siswa dengan NIS " + nis + " tidak ditemukan");
        }
    }

    public static void deleteSiswa(){
        System.out.print("Masukkan NIS siswa: ");
        String nis =  scan.nextLine();

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
            System.out.println("Siswa berhasil dihapus!");
        }
        else{
            System.out.println("Siswa dengan NIS " + nis + " tidak ditemukan");
        }
    }
}