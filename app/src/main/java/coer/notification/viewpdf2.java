package coer.notification;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class viewpdf2 extends AppCompatActivity {

    String urls;
    PDFView pdfView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpdf2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        pdfView = findViewById(R.id.abc);
        String filename=getIntent().getStringExtra("filename");


        // Firstly we are showing the progress
        // dialog when we are loading the pdf
        dialog = new ProgressDialog(this);
        dialog.setTitle(filename);
        dialog.setMessage("Loading....");
        dialog.show();

        // getting url of pdf using getItentExtra
        urls = getIntent().getStringExtra("fileurl");
        new RetrivePdfStream().execute(urls);
    }

    // Retrieving the pdf file using url
    class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {

                // adding url
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // if url connection response code is 200 means ok the execute
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            // if error return null
            catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        // Here load the pdf and dismiss the dialog box
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream)
                    .load();
            dialog.dismiss();
        }

    }

}
