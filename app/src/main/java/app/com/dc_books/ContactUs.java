package app.com.dc_books;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JINS on 11/23/2017.
 */

public class ContactUs extends AppCompatActivity {

    WebView contactus_webview;
    ProgressDialog pDialog;
    Toolbar toolbar;
    int NETCONNECTION;
    Typeface font;
    TextView t_header;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t_header=findViewById(R.id.textView12);
        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");
        t_header.setTypeface(font);

        isNetworkConnected();
        if(NETCONNECTION==0)
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Cross check your internet connectivity and try again")
                    .show();
        }

        contactus_webview=findViewById(R.id.webview1);
        WebClientClass webViewClient = new WebClientClass();
        contactus_webview.setWebViewClient(webViewClient);

        contactus_webview.getSettings().setJavaScriptEnabled(true);
        contactus_webview.loadUrl("http://www.l10.in/contactus.php");
    }
    public class WebClientClass extends WebViewClient {
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd = new ProgressDialog(ContactUs.this);
            pd.setMessage("Loading...");
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pd.dismiss();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            NETCONNECTION=0;
        }
        else
        {
            NETCONNECTION=1;
        }

        return true;
    }
}
