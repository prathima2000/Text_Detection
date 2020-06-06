package com.example.textdetect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;

public class createdocument extends AppCompatActivity {
    private static final int STORAGE_CODE=1000;
    Button share,save;
    EditText t1;
    String editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createdocument);


        share = (Button)findViewById(R.id.share);
        save = (Button)findViewById(R.id.save);
        t1 = (EditText) findViewById(R.id.data);
        Intent i = getIntent();

        editText = i.getStringExtra("text_key");
        t1.setText(editText);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntend=new Intent(Intent.ACTION_SEND);
                myIntend.setType("Text/plain");
                String sharebody="Data in image";
                String sharesub=editText;
                myIntend.putExtra(Intent.EXTRA_SUBJECT,sharebody);
                myIntend.putExtra(Intent.EXTRA_TEXT,sharesub);
                startActivity(Intent.createChooser(myIntend,"share Using"));
            }});

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        String permissions=(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        requestPermissions(new String[]{permissions},STORAGE_CODE);


                    } else {
                        savePdf();


                    }
                }
                    else
                    {
                      savePdf();
                    }


            }});


        }

    private void savePdf() {
        Document mDoc=new Document();
        String mFileName=new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath= Environment.getExternalStorageDirectory() +"/" + mFileName+".pdf";
        try{
            PdfWriter.getInstance(mDoc,new FileOutputStream(mFilePath));
            mDoc.open();
            String mText=t1.getText().toString();
           // mDoc.addAuthor("prathima");
            mDoc.add(new Paragraph(mText));
            mDoc.close();
            Toast.makeText(this,mFileName+".pfd\n is saves to \n"+mFilePath,Toast.LENGTH_SHORT).show();

        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case STORAGE_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    savePdf();

                }
                else
                {
                    Toast.makeText(this,"permission denayed!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}





