package com.example.uts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uts.adapter.ReservationAdapter;
import com.example.uts.fragment.FragmentDetail;
import com.example.uts.model.Reservation;
import com.example.uts.preferences.UserPreferences;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Detail extends AppCompatActivity
{
    public RecyclerView rv_reservasi;
    public UserPreferences userPreferences;

    public List<Reservation> reservationList;
    public ReservationAdapter reservationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        changeFragment(new FragmentDetail());
        setTitle("Detail Reservasi");
    }

    public void changeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment,fragment)
                .commit();
    }

    private void cetakPdf() throws FileNotFoundException, DocumentException {
 /*
 * Untuk Android 11 nanti file pdf tidak bisa diakses lewat file
manager HP langsung
 * jadi harus konek lewat PC gara gara implementasi Scoped Storage.
 *
 * Kalau mau biar bisa di android 11 bisa pelajari sendiri tentang
penggunaan MediaStorage
 * */
        File folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date currentTime = Calendar.getInstance().getTime();
        String pdfName = currentTime.getTime() + ".pdf";
        File pdfFile = new File(folder.getAbsolutePath(), pdfName);
        OutputStream outputStream = new FileOutputStream(pdfFile);
        com.itextpdf.text.Document document = new
                com.itextpdf.text.Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        // bagian header
        Paragraph judul = new Paragraph("NOTA RESERVASI \n\n",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 16,
                        Font.BOLD, BaseColor.BLACK));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        // Buat tabel
        PdfPTable tables = new PdfPTable(new float[]{16, 8});
        // Settingan ukuran tabel
        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);
        Paragraph kepada = new Paragraph(
                "Kepada Yth: \n" + userPreferences.getUserLogin().getName() + "\n",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 10,
                        Font.NORMAL, BaseColor.BLACK));
        cellSupplier.addElement(kepada);
        tables.addCell(cellSupplier);
        Paragraph NomorTanggal = new Paragraph(
                "No : " + "123456789" + "\n\n" + "Tanggal : " + new SimpleDateFormat("dd/MM/yyyy",
                        Locale.getDefault()).format(currentTime) + "\n",
                new
                        com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                        com.itextpdf.text.Font.NORMAL, BaseColor.BLACK));
        NomorTanggal.setPaddingTop(5);
        tables.addCell(NomorTanggal);
        document.add(tables);
        com.itextpdf.text.Font f = new

                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\nBerikut merupakan daftar pegawai teladan: \n\n", f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{5, 5, 5, 5, 5});

        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        // Setup Column
        PdfPCell h1 = new PdfPCell(new Phrase("Nama Paket"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Tanggal Reservasi"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h3 = new PdfPCell(new Phrase("Note"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);
        PdfPCell h4 = new PdfPCell(new Phrase("Harga"));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);
        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h3);
        tableHeader.addCell(h4);
        // Beri warna untuk kolumn
        for (PdfPCell cells : tableHeader.getRow(0).getCells()) {
            cells.setBackgroundColor(BaseColor.PINK);
        }
        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{5, 5, 5, 5});

        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        // masukan data pegawai jadi baris
        for (Reservation R : reservationList) {
            tableData.addCell(R.getPaket());
            tableData.addCell(R.getTanggal());
            tableData.addCell(R.getNote());
            tableData.addCell(String.valueOf(R.getHarga()));
        }
        document.add(tableData);
        com.itextpdf.text.Font h = new

                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL);
        String tglDicetak = currentTime.toLocaleString();
        Paragraph P = new Paragraph("\nDicetak tanggal " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P);
        document.close();
        previewPdf(pdfFile);
        Toast.makeText(this, "PDF berhasil dibuat", Toast.LENGTH_SHORT).show();
    }

    private void previewPdf(File pdfFile) {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(testIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Uri uri;
            uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            this.grantUriPermission(getPackageName(), uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(pdfIntent);
        }
    }
}
