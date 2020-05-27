package primeiropp.studio.com.escannner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Lido extends AppCompatActivity {
    private Bundle bundle;
    private WebView webView;
    private Button btn_salvar;
    private String chave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lido);
        bundle = getIntent().getBundleExtra("bundle");
        chave = bundle.getString("nota").replace(" ","");
        btn_salvar = (Button) findViewById(R.id.img_salvar);
        btn_salvar.setText("Salvar nf");
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = lerArquivo();
                if(result.contains(chave)){
                    Toast.makeText(getApplicationContext(),"Link ja foi salvo",Toast.LENGTH_SHORT).show();
                }else {
                    gravarArquivo(chave);
                }
            }
        });

        Log.i("RESULTADO CHAVE",chave);
        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl(chave);
    }
    private void gravarArquivo(String txt) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(MainActivity.arquivo, Context.MODE_PRIVATE));
            outputStreamWriter.write(txt+"\n");
            Toast.makeText(this, "Link da Nota salva", Toast.LENGTH_SHORT).show();
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }
    }
    private String lerArquivo() {
        String resultado = "";
        try {
            InputStream arq = openFileInput(MainActivity.arquivo);
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