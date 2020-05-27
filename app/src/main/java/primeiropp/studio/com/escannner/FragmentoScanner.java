package primeiropp.studio.com.escannner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoScanner extends Fragment  implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private LinearLayout linearLayout;
    private Button btn_salvar;
    private String chaveNota;
    private String URLnota;
    private String baseLink = "https://sistemas.sefaz.am.gov.br/nfceweb/consultarNFCe.jsp?p=";
    private WebView webView;
    private EditText editText;

    private ProgressDialog progDailog;
    public FragmentoScanner() {
        // Required empty public constructor
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_fragmento_scanner, container, false);
         linearLayout  = (LinearLayout) view.findViewById(R.id.linear_layout);



        webView = new WebView(getContext());
         webView.setWebViewClient(new WebViewClient(){



             @Override
             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                 progDailog.cancel();
                 super.onPageStarted(view, url, favicon);

             }
         });
         WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);


        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,950));


        editText = new EditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
        editText.setHint("Anotacao sobre a nota");
        editText.setHintTextColor(Color.WHITE);
        editText.setTextColor(Color.WHITE);
        editText.setGravity(Gravity.CENTER);
        btn_salvar = new Button(getContext());
        btn_salvar.setText("SALVAR");
        btn_salvar.setTextColor(Color.WHITE);

        btn_salvar.setBackgroundColor(Color.parseColor("#1C1C1C"));
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String salvar;
                String str = editText.getText().toString();
                String arq = lerArquivo();
                if(str.length()>1){
                    salvar = str+"@"+URLnota;
                    if(!arq.contains(salvar.split("@")[0])&&!arq.contains(salvar.split("@")[1])){
                        gravarArquivo(salvar);
                    }else if(arq.contains(salvar.split("@")[0])){
                        Toast.makeText(getContext(),"Ja existe uma nota com esse nome",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),"LINK JA TINHA SIDO SALVO",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Nota nao tem nome",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mScannerView = new ZXingScannerView(getContext());   // Programmatically initialize the scanner view
        linearLayout.addView(mScannerView);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        chaveNota = rawResult.getText().toString().replace(" ", "");
        if (chaveNota.contains("sistemas.sefaz.am.gov.br")) {
            progDailog = ProgressDialog.show(getContext(), "Loading","Please wait...", true);
            progDailog.setCancelable(false);
            URLnota =  chaveNota;
            iniciar_webview(URLnota);
        } else if (chaveNota.length() == 44) {
            progDailog = ProgressDialog.show(getContext(), "Loading","Please wait...", true);
            progDailog.setCancelable(false);
            URLnota = baseLink+chaveNota;
            iniciar_webview(URLnota);
        } else {
            Toast.makeText(getContext(),"ERRO AO LER O CODIGO",Toast.LENGTH_SHORT);
        }
        Log.v("RESULTADO", rawResult.getText()); // Prints scan results
        Log.v("RESULTADO", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:

        mScannerView.resumeCameraPreview(this);
    }
    public void iniciar_webview(String chaveNota){
        linearLayout.removeAllViews();
        linearLayout.addView(webView);
        webView.loadUrl(chaveNota);
        linearLayout.addView(editText);
        linearLayout.addView(btn_salvar);

    }

    private void gravarArquivo(String txt) {
        try {
            String resultado = lerArquivo();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput(MainActivity.arquivo, Context.MODE_PRIVATE));
            if(resultado.length()>3){
                txt = resultado+";"+txt;
            }
            outputStreamWriter.write(txt);
            Toast.makeText(getContext(), "Link da Nota salva", Toast.LENGTH_SHORT).show();
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }
    }
    private String lerArquivo() {
        String resultado = "";
        try {
            InputStream arq = (getActivity().openFileInput(MainActivity.arquivo));
            if (arq != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(arq);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String linha = "";
                while ((linha = bufferedReader.readLine()) != null) {
                    resultado += linha;
                }
                arq.close();

            }
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }
        return resultado;
    }


}


