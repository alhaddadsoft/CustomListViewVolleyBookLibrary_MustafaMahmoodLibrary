package com.alhaddadsoft.librarymustafamahmood;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DownloadBook extends Activity {
    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    int per = 0;
    TextView cur_val;
    Context context;

    ImageView imageView;
    TextView textView;

    TextView loadingcomplete ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadgbran);


        cur_val = (TextView) findViewById(R.id.cur_pg_tv);
        cur_val.setText("جاري بدء التحميل");

        loadingcomplete = (TextView)findViewById(R.id.textView7);
        loadingcomplete.setText("جاري تحميل كتاب" );

       // imageView = (ImageView)findViewById(R.id.imageView7);
        textView = (TextView)findViewById(R.id.textView8);

        pb = (ProgressBar)findViewById(R.id.progress_bar);
        pb.setProgress(0);
     //   pb.setProgressDrawable(ContextCompat.getDrawable(DownloadBook.this, R.drawable.green_progress));


        new Thread(new Runnable() {
            public void run() {

                downloadFile();

            }
        }).start();


    }
    void downloadFile(){

        Intent intent = getIntent();
        final String save_file_As = intent.getExtras().getString("save_file_As");
        final String dwnload_file_path = intent.getExtras().getString("dwnload_file_path");
        //final String imageViewD = intent.getExtras().getString("imageViewD");
        final String total_size = intent.getExtras().getString("total_size");
        final String getTitle = intent.getExtras().getString("getTitle");


        textView.setText(getTitle);

        File sdcard = Environment.getExternalStorageDirectory();
        File filepath = new File( sdcard,"Download/Mustafa_Mahmood/"+save_file_As+".png");
        if(filepath.exists()){
//            imageView.setImageDrawable(Drawable.createFromPath(filepath.getAbsolutePath()));
          //  Bitmap myBitmap = BitmapFactory.decodeFile(sdcard+"/downloadManager/book1.pdf.png");
//            Drawable myDrawable = new BitmapDrawable(DownloadBook.this.getResources(), filepath.getAbsolutePath());
//            imageView.setImageDrawable(myDrawable);
        }


        try {
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"/Download/", "Mustafa_Mahmood");
            if(!root.exists()){
                root.mkdirs();
            }
            File file = new File(SDCardRoot,"/Download/Mustafa_Mahmood/" + save_file_As);

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                         int  per = (downloadedSize/10240);
                        cur_val.setText("جاري التحميل الرجاء الإنتظار سيتم إبلاغك بعد الإنتهاء "  +(downloadedSize));
                        pb.setProgress(downloadedSize);

                    }
                });
            }

            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    cur_val.setText("انتهى التحميل");
                    Toast.makeText(DownloadBook.this, "تم إظافة  الكتاب للمكتبة تستطيع الآن فتح الكتاب ", Toast.LENGTH_SHORT).show();


                    new SweetAlertDialog(DownloadBook.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("انتهى التحميل!")
                            .setContentText("")
                            .setConfirmText("موافق")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent d = new Intent(DownloadBook.this, MainActivity.class);
                                    d.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(d);
                                    finish();
                                }
                            })
                            .show();
                }

            });



            runOnUiThread(new Runnable() {
                public void run() {
                    // pb.dismiss(); // if you want close it..
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : الرجاء التأكد من اتصالك بالإنترنت " + e);
        }
    }
    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(DownloadBook.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void openactivity(){


            // pb.dismiss(); // if you want close it..

    }
}






