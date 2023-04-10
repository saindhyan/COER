package coer.notification.ui.about_college;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import coer.notification.R;


public class about_fragment extends Fragment {

    WebView mWebview;
    ProgressBar pbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mWebview = view.findViewById(R.id.webview);
        pbar = view.findViewById(R.id.pbar);


        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.setWebViewClient( new  Callback());



        mWebview.loadUrl("https://www.coer.ac.in");
        pbar.setVisibility(View.INVISIBLE);


        return view;
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        pbar.setVisibility(View.VISIBLE);
    }
}