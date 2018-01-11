package com.example.barna.odusafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class Crimelog_Parser  {

    private void parsePDF(String pdf) throws IOException{
        File root = new File(Environment.getExternalStorageDirectory()+File.separator+"ODUSAFE");
        if (!root.exists())
        {
            root.mkdirs();
        }
        File txt = new File(root,"temp.txt");
        PdfReader reader = new PdfReader(pdf);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            out.println(strategy.getResultantText());
        }
        reader.close();
        out.flush();
        out.close();
    }

}
