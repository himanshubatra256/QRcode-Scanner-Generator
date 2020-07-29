package com.Master_QR.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class MainActivity extends AppCompatActivity {
    private Button sc,gen;
    private ImageView i1;
    private EditText t1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sc = (Button) findViewById(R.id.scan);
        gen =(Button) findViewById(R.id.gen);
        i1=(ImageView)findViewById(R.id.i1);
        t1=(EditText)findViewById(R.id.t1);
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str =t1.getText().toString();
                if(str!=null && !str.isEmpty() ){
                try{
                    MultiFormatWriter mw=new MultiFormatWriter();
                    BitMatrix bm=mw.encode(str, BarcodeFormat.QR_CODE,120,120);
                    BarcodeEncoder bencode =new BarcodeEncoder();
                    Bitmap bitmap= bencode.createBitmap(bm);
                    i1.setImageBitmap(bitmap);
                }
                catch(WriterException e)
                {
                    e.printStackTrace();
                }
            }}
        });

        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator ig= new IntentIntegrator(MainActivity.this);
                ig.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                ig.setCameraId(0);
                ig.setOrientationLocked((false));
                ig.setPrompt("Verifying");
                ig.setBeepEnabled(true);
                ig.setBarcodeImageEnabled(true);
                ig.initiateScan();
            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result =IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Scan Completed")
            .setMessage(result.getContents())
                    .setPositiveButton("copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager manager=(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData data= ClipData.newPlainText("result",result.getContents());
                            manager.setPrimaryClip(data);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
