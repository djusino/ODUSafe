package com.example.barna.odusafe;

import android.app.Activity;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import android.content.ContentResolver;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * Created by barna on 10/8/2017.
 */

public class Crimelog_Downloader extends AppCompatActivity {
    private long enqueue;
    private DownloadManager dm;
    ImageView view;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crimelog_download);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Query query = new Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            Toast.makeText(getApplicationContext(), "Download Completed ", Toast.LENGTH_SHORT).show();

                            view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            view.setImageURI(Uri.parse(uriString));

                            Uri path = Uri.parse(uriString);

                            try {
                                openPDF(path);
                                Toast.makeText(Crimelog_Downloader.this,"See Full Report in Downloads",Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(Crimelog_Downloader.this, "Something Wrong: " + e.toString(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Download Not Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void onClick(View view) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(
                Uri.parse("http://ww2.odu.edu/police/safety/crimelog/ODU%20Crime%20Log%20Last%2060%20Days.pdf"));
        enqueue = dm.enqueue(request);

    }

    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    private void openPDF(Uri pdf) throws IOException {
        File file = new File(pdf.toString());

        ParcelFileDescriptor fileDescriptor = null;
        fileDescriptor = getContentResolver().openFileDescriptor(pdf,"r");

        //min. API Level 21
        PdfRenderer pdfRenderer = null;
        pdfRenderer = new PdfRenderer(fileDescriptor);

        final int pageCount = pdfRenderer.getPageCount();
        Toast.makeText(this,
                "pageCount = " + pageCount,
                Toast.LENGTH_LONG).show();

        //Display page 0
        PdfRenderer.Page rendererPage = pdfRenderer.openPage(0);
        int rendererPageWidth = rendererPage.getWidth();
        int rendererPageHeight = rendererPage.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(
                rendererPageWidth,
                rendererPageHeight,
                Bitmap.Config.ARGB_8888);
        rendererPage.render(bitmap, null, null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        view.setImageBitmap(bitmap);
        rendererPage.close();

        pdfRenderer.close();
        fileDescriptor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goHome) {
            Intent home = new Intent(this,Dashboard.class);
            startActivity(home);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
