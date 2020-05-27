package primeiropp.studio.com.escannner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView btn_salvar;
    private EditText txt_novo_nome;
    private  String nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        final Bundle bundle = getIntent().getBundleExtra("bundle");
        String link =  bundle.getString("Link");
         nome = bundle.getString("Nome");

        webView = (WebView) findViewById(R.id.web_view);

        txt_novo_nome = (EditText) findViewById(R.id.txt_novo_nome);
        txt_novo_nome.setHint("Nome Atual: "+ nome);
        txt_novo_nome.setHintTextColor(Color.WHITE);

        btn_salvar = (ImageView) findViewById(R.id.img_salvar);
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_atual =  txt_novo_nome.getText().toString();
                if(nome_atual.length()>1){
                    txt_novo_nome.setHint("Nome Atual: "+nome_atual);
                    txt_novo_nome.setText("");
                    String resultado[] = lerArquivo().split(";");
                    for (int i =0;i<resultado.length;i++){
                        if(resultado[i].contains(nome)){
                            resultado[i] = nome_atual+"@"+resultado[i].split("@")[1];
                            break;
                        }
                    }
                    gravarArquivo(String.join(";",resultado));
                }
            }
        });
        webView.setWebViewClient(new WebViewClient(){
        });
        webView.setVisibility(View.VISIBLE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl(link);

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
    private void gravarArquivo(String txt) {
        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(MainActivity.arquivo, Context.MODE_PRIVATE));
            outputStreamWriter.write(txt);
            Toast toast = Toast.makeText(this, "Nome alterado", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.setVisibility(View.INVISIBLE);

        finish();


    }
}
