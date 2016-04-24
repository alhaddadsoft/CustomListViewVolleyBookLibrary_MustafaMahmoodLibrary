package com.alhaddadsoft.librarymustafamahmood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.alhaddadsoft.librarymustafamahmood.model.Movie;

public class DownloadActivity extends Activity {
    String pdfUrlString,saveAsString, imageUrl, titleName ,ratingInt, releaseYeariInt, generstring;

    int downloadedSize = 0;
    int totalSize = 0;
    int per = 0;

    private List<Movie> movieList2 = new ArrayList<>();

    Activity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        new Thread(new Runnable() {
            public void run() {

                //downloadFile();

            }
        }).start();


    }
    void downloadFile(){
        Intent intent = getIntent();
         pdfUrlString = intent.getExtras().getString("pdfUrlString");
         saveAsString = intent.getExtras().getString("saveAsString");
         titleName = intent.getExtras().getString("titleName");

        try {
            URL url = new URL(pdfUrlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            //connect
            urlConnection.connect();
            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"downloadManager/");
            if (!root.exists())
            {
                Toast.makeText(DownloadActivity.this, "doesnt exist", Toast.LENGTH_LONG).show();

                root.mkdirs();
            }
            File file = new File(SDCardRoot,"/downloadManager/" + saveAsString);

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();


            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //

                boolean showMinMax = true;
                final MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title("جاري تحميل كتاب :"+ titleName)
                        .content("الرجاء الإنتظار")
                        .progress(false, 100, showMinMax)
                        .show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int  per = (downloadedSize/totalSize) * 100;

                        // Loop until the dialog's progress value reaches the max (150)
                        while (dialog.getCurrentProgress() != dialog.getMaxProgress()) {
                            // If the progress dialog is cancelled (the user closes it before it's done), break the loop
                            if (dialog.isCancelled()) break;
                            // Wait 50 milliseconds to simulate doing work that requires progress
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                break;
                            }
                            // Increment the dialog's progress by 1 after sleeping for 50ms
                            dialog.incrementProgress(per);
                        }

                        // When the loop exits, set the dialog content to a string that equals "Done"
                        dialog.setContent((R.string.done));

                    }
                });
            }

            //close the output stream when complete //
            fileOutput.close();

            Toast.makeText(DownloadActivity.this, "finished", Toast.LENGTH_SHORT).show();


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
            @Override
            public void run() {
                Toast.makeText(DownloadActivity.this, err, Toast.LENGTH_LONG).show();

            }
        });
    }
    }
   /* public void addbooktolibrary(){
        File sdcard = Environment.getExternalStorageDirectory();
        File oldFileName = new File( sdcard,"downloadManager/json.txt");
        File tmpFileName = new File( sdcard,"downloadManager/json.tmp.txt");

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(oldFileName));
            bw = new BufferedWriter(new FileWriter(tmpFileName));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"data\": [\n" +
                        "{"))
                    line = line.replace("\"data\": [\n" +
                            "{","\"data\": [\n" +
                            "{\n" +
                            "   \"title\": \"على حافة الزلزال\",\n" +
                            "        \"image\": \"http://mostafamahmoud.net/wp-content/uploads/2015/09/431794_42795b9f1c9a577a8bc88e55e8827aad.jpg\",\n" +
                            "\t\t\"pdfUrl\": \"http://al-hakawati.net/arabic/civilizations/diwanindex6a22.pdf\",\n" +
                            "\t\t\"SaveAs\": \"book1\",\n" +
                            "        \"rating\": 8.3,\n" +
                            "        \"releaseYear\": 2014,\n" +
                            "        \"genre\": \"إنه وباء القرن الواحد والعشرين وطاعون المستقبل والمسلمون المستهدفون في كل مكان عليهم أن يضموا الأيدي ليصدوا هذا الخطر المتسلل عبر الحدود المفتوحة في كل مكان ، وعبر الهواء والفضائيات والإعلام وعبر الصحيفة والخبر والفيلم والكتاب ووسائل غسيل المخ التي تعبر الحدود وتنساح في كل بيت ، فالكلمة هي الطلقة الخفية وهي القذيفة والصاروخ وهي الجاسوس الخفي الذي يغتال القلب ويلوث الضمير بكل ألوان الفساد ، وبروتوكولات حكماء آل صهيون هي ما تفتقت عنه أذهان دهاقنة الصهاينة في إفساد شعوب العالم وحكوماته ، وقد لعنهم أنبياؤهم في التوراة كما لعنهم المسيح في الأناجيل الثلاثة كما لعنهم الله في قرآنه .\"\n" +
                            "    },\n" +
                            "    {");
                bw.write(line+"\n");
            }

        } catch (Exception e) {
            return;
        } finally {
            try {
                if(br != null)
                    br.close();
            } catch (IOException e) {
                //
            }
            try {
                if(bw != null)
                    bw.close();
            } catch (IOException e) {
                //
            }
        }
        boolean deleted = false,rename = false;
        if(oldFileName.exists()){

            deleted = oldFileName.delete(); //this returns a boolean variable.
        }
        if(deleted){
            rename = tmpFileName.renameTo(oldFileName);

        }
        if(!deleted||!rename){
            // log some type of warning here or even throw an exception
        }

    }*/ // replace jsonmethode

