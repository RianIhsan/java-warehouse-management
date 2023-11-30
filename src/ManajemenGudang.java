import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

class Barang {
    String code;
    String nama;
    int stok;
    double harga;

    public Barang(String nama, int stok, double harga) {
        this.code = generateRandomCode();
        this.nama = nama;
        this.stok = stok;
        this.harga = harga;
    }

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            codeBuilder.append(random.nextInt(10));
        }
        return codeBuilder.toString();
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }
}

public class ManajemenGudang {
    private static ArrayList<Barang> daftarBarang = new ArrayList<>();
    private static final String FILE_PATH = "daftar_barang.txt";

    public static void main(String[] args) {
        loadDaftarBarang();

        Scanner scanner = new Scanner(System.in);
        int pilihan;

        do {
            System.out.println("==== Manajemen Persediaan Gudang ====");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Edit Barang");
            System.out.println("3. Lihat Barang");
            System.out.println("4. Hapus Barang");
            System.out.println("5. Cari Barang");
            System.out.println("6. Urutkan Barang");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    tambahBarang(scanner);
                    saveToFile();
                    break;
                case 2:
                    editBarang(scanner);
                    saveToFile();
                    break;
                case 3:
                    lihatBarang();
                    break;
                case 4:
                    hapusBarang(scanner);
                    saveToFile();
                    break;
                case 5:
                    cariBarang(scanner);
                    break;
                case 6:
                    menuUrutkan(scanner);
                    break;
                case 0:
                    System.out.println("Keluar dari program.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (pilihan != 0);

        scanner.close();
    }

    private static void tambahBarang(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Masukkan nama barang: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan stok barang: ");
        int stok = scanner.nextInt();
        System.out.print("Masukkan harga barang: ");
        double harga = scanner.nextDouble();

        Barang barang = new Barang(nama, stok, harga);
        daftarBarang.add(barang);
        System.out.println("Barang berhasil ditambahkan.");
    }

    private static void editBarang(Scanner scanner) {
        lihatBarang();
        System.out.print("Masukkan nomor barang yang ingin diubah: ");
        int nomor = scanner.nextInt();

        if (nomor >= 1 && nomor <= daftarBarang.size()) {
            scanner.nextLine();
            System.out.print("Masukkan nama baru: ");
            String namaBaru = scanner.nextLine();
            System.out.print("Masukkan stok baru: ");
            int stokBaru = scanner.nextInt();
            System.out.print("Masukan harga baru: ");
            double hargaBaru = scanner.nextDouble();

            Barang barang = daftarBarang.get(nomor - 1);
            barang.nama = namaBaru;
            barang.stok = stokBaru;
            barang.harga = hargaBaru;

            System.out.println("Barang berhasil diubah.");
        } else {
            System.out.println("Nomor barang tidak valid.");
        }
    }

    private static void lihatBarang() {
        System.out.println("===============================DAFTAR BARANG===============================");
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("| %-3s | %-10s | %-20s | %-10s | %-10s |\n", "No", "Code", "Nama", "Stok", "Harga");
        System.out.println("======================================================================");
        for (int i = 0; i < daftarBarang.size(); i++) {
            Barang barang = daftarBarang.get(i);
            System.out.printf("| %-3d | %-10s | %-20s | %-10d | %-10.2f |\n", i + 1, barang.code, barang.nama,
                    barang.stok, barang.harga);
        }
        System.out.println("======================================================================");
    }

    private static void hapusBarang(Scanner scanner) {
        lihatBarang();
        System.out.print("Masukkan nomor barang yang ingin dihapus: ");
        int nomor = scanner.nextInt();

        if (nomor >= 1 && nomor <= daftarBarang.size()) {
            daftarBarang.remove(nomor - 1);
            System.out.println("Barang berhasil dihapus.");
        } else {
            System.out.println("Nomor barang tidak valid.");
        }
    }

    private static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("===============================DAFTAR BARANG===============================");
            writer.println("----------------------------------------------------------------------");
            writer.printf("| %-3s | %-10s | %-20s | %-10s | %-10s |\n", "No", "Code", "Nama", "Stok", "Harga");
            writer.println("======================================================================");
            for (int i = 0; i < daftarBarang.size(); i++) {
                Barang barang = daftarBarang.get(i);
                writer.printf("| %-3d | %-10s | %-20s | %-10d | %-10.2f |\n", i + 1, barang.code, barang.nama,
                        barang.stok, barang.harga);
            }
            writer.println("======================================================================");
            System.out.println("Data berhasil disimpan ke " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan data ke " + FILE_PATH);
            e.printStackTrace();
        }
    }

    private static void loadDaftarBarang() {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("|") && line.endsWith("|")) {
                    String[] parts = line.substring(1, line.length()).split("\\|");

                    if (parts.length >= 5) {
                        String nama = parts[2].trim();
                        int stok;

                        try {
                            stok = Integer.parseInt(parts[3].trim());
                        } catch (NumberFormatException e) {
                            continue;
                        }

                        double harga;

                        try {
                            harga = Double.parseDouble(parts[4].trim());
                        } catch (NumberFormatException e) {
                            continue;
                        }

                        daftarBarang.add(new Barang(nama, stok, harga));
                    } else {
                        System.out.println("Format data tidak sesuai. Melewati baris: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(FILE_PATH + " tidak ditemukan. Membuat file baru.");
        }
    }

    private static void cariBarang(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Masukkan kata kunci pencarian: ");
        String keyword = scanner.nextLine().toLowerCase();

        System.out.println("===============================HASIL PENCARIAN===============================");
        System.out.printf("| %-3s | %-10s | %-30s | %-10s | %-10s |\n", "No", "Code", "Nama", "Stok", "Harga");
        System.out.println("======================================================================");

        int resultCount = 0;
        for (int i = 0; i < daftarBarang.size(); i++) {
            Barang barang = daftarBarang.get(i);
            if (barang.code.toLowerCase().contains(keyword) ||
                    barang.nama.toLowerCase().contains(keyword) ||
                    String.valueOf(barang.harga).toLowerCase().contains(keyword)) {
                resultCount++;
                System.out.printf("| %-3d | %-10s | %-30s | %-10d | %-10.2f |\n", resultCount, barang.code, barang.nama,
                        barang.stok, barang.harga);
            }
        }

        if (resultCount == 0) {
            System.out.println("Tidak ditemukan barang dengan kata kunci tersebut.");
        }

        System.out.println("======================================================================");
    }

    private static void menuUrutkan(Scanner scanner) {
        System.out.println("==== Urutkan Barang ====");
        System.out.println("1. By Nama Abjad");
        System.out.println("2. By Harga Terkecil");
        System.out.println("3. By Harga Terbesar");
        System.out.println("4. By Stok Terkecil");
        System.out.println("5. By Stok Terbesar");
        System.out.print("Pilih jenis pengurutan: ");
        int pilihan = scanner.nextInt();

        switch (pilihan) {
            case 1:
                urutkanByNamaAbjad();
                break;
            case 2:
                urutkanByHargaTerkecil();
                break;
            case 3:
                urutkanByHargaTerbesar();
                break;
            case 4:
                urutkanByStokTerkecil();
                break;
            case 5:
                urutkanByStokTerbesar();
                break;
            default:
                System.out.println("Pilihan tidak valid.");
        }
        saveToFile();
    }

    private static void urutkanByNamaAbjad() {
        Collections.sort(daftarBarang, Comparator.comparing(Barang::getNama));
        System.out.println("Barang berhasil diurutkan berdasarkan nama abjad.");
        lihatBarang();
    }

    private static void urutkanByHargaTerkecil() {
        Collections.sort(daftarBarang, Comparator.comparing(Barang::getHarga));
        System.out.println("Barang berhasil diurutkan berdasarkan harga terkecil.");
        lihatBarang();
    }

    private static void urutkanByHargaTerbesar() {
        Collections.sort(daftarBarang, Comparator.comparing(Barang::getHarga).reversed());
        System.out.println("Barang berhasil diurutkan berdasarkan harga terbesar.");
        lihatBarang();
    }

    private static void urutkanByStokTerkecil() {
        Collections.sort(daftarBarang, Comparator.comparing(Barang::getStok));
        System.out.println("Barang berhasil diurutkan berdasarkan stok terkecil.");
        lihatBarang();
    }

    private static void urutkanByStokTerbesar() {
        Collections.sort(daftarBarang, Comparator.comparing(Barang::getStok).reversed());
        System.out.println("Barang berhasil diurutkan berdasarkan stok terbesar.");
        lihatBarang();
    }

}
