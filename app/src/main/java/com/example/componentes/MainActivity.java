package com.example.componentes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText searchEditText;
    private Button wikipediaButton;
    private Button googleButton;
    private WebView contentWebView;
    private ProgressBar progressBar;
    private CardView searchCardView;
    private CardView resultCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes
        toolbar = findViewById(R.id.toolbar);
        searchEditText = findViewById(R.id.searchEditText);
        wikipediaButton = findViewById(R.id.wikipediaButton);
        googleButton = findViewById(R.id.googleButton);
        contentWebView = findViewById(R.id.contentWebView);
        progressBar = findViewById(R.id.progressBar);
        searchCardView = findViewById(R.id.searchCardView);
        resultCardView = findViewById(R.id.resultCardView);

        // Configurar Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setElevation(4f);
        //toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_info_details);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Acerca de InfoConsulta", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setOverflowIcon(getDrawable(android.R.drawable.ic_menu_more));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setContentInsetsRelative(16, 0);
        toolbar.setContentInsetStartWithNavigation(8);

        // Configurar SearchEditText
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchWikipedia(v.getText().toString());
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
        searchEditText.setSelectAllOnFocus(true);
        searchEditText.setTextIsSelectable(true);
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        searchEditText.setPadding(16, 12, 16, 12);
        searchEditText.setCompoundDrawablePadding(16);
        searchEditText.setMaxLines(1);
        searchEditText.setSingleLine(true);
        searchEditText.setCursorVisible(true);
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEditText.setElevation(2f);

        // Configurar CardView para búsqueda
        searchCardView.setRadius(8f);
        searchCardView.setCardElevation(4f);
        searchCardView.setMaxCardElevation(6f);
        searchCardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        searchCardView.setUseCompatPadding(true);
        searchCardView.setPreventCornerOverlap(true);
        searchCardView.setContentPadding(16, 16, 16, 16);
        searchCardView.setClickable(true);
        searchCardView.setFocusable(true);
        searchCardView.setForeground(getDrawable(android.R.drawable.list_selector_background));

        // Configurar CardView para resultados
        resultCardView.setRadius(8f);
        resultCardView.setCardElevation(4f);
        resultCardView.setMaxCardElevation(6f);
        resultCardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        resultCardView.setUseCompatPadding(true);
        resultCardView.setPreventCornerOverlap(true);
        resultCardView.setContentPadding(0, 0, 0, 0);
        resultCardView.setClickable(true);
        resultCardView.setFocusable(true);
        resultCardView.setForeground(getDrawable(android.R.drawable.list_selector_background));

        // Configurar WebView
        WebSettings webSettings = contentWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setTextZoom(100);

        contentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });

        contentWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // Configurar botones
        wikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchWikipedia(query);
                } else {
                    Toast.makeText(MainActivity.this, "Ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchGoogle(query);
                } else {
                    Toast.makeText(MainActivity.this, "Ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar FAB
        findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHomePage();
            }
        });

        // Cargar página inicial
        loadHomePage();
    }

    private void loadHomePage() {
        contentWebView.loadUrl("https://www.wikipedia.org");
    }

    private void searchWikipedia(String query) {
        String url = "https://es.wikipedia.org/wiki/Special:Search?search=" + query.replace(" ", "+");
        contentWebView.loadUrl(url);
    }

    private void searchGoogle(String query) {
        String url = "https://www.google.com/search?q=" + query.replace(" ", "+");
        contentWebView.loadUrl(url);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            contentWebView.reload();
            return true;
        } else if (id == R.id.action_forward) {
            if (contentWebView.canGoForward()) {
                contentWebView.goForward();
            }
            return true;
        } else if (id == R.id.action_share) {
            Toast.makeText(this, "Compartir contenido", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (contentWebView.canGoBack()) {
            contentWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}