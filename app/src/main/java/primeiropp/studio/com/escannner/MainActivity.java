package primeiropp.studio.com.escannner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> lista_links_str;
    private BottomNavigationView navigationMenu;
    private LinearLayout linearLayoutFragmentos;
    public static String destino;
    public static String arquivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getBundleExtra("destino");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},1
               );

        destino = bundle.getString("destino");
        arquivo = MainActivity.destino+" - link_notas_sefaz.txt";
        Toast.makeText(this,destino,Toast.LENGTH_LONG ).show();
        setContentView(R.layout.activity_main);
        navigationMenu = (BottomNavigationView) findViewById(R.id.bottom_navegacao);
        navigationMenu.setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.list_view);
        linearLayoutFragmentos = (LinearLayout) findViewById(R.id.layout_fragmentos);
        linearLayoutFragmentos.removeAllViews();
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragmentos,new ListarNfs()).commit();
        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:{
                        linearLayoutFragmentos.removeAllViews();
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragmentos,new ListarNfs()).commit();

                        break;
                    }
                    case R.id.navigation_scanner:{
                        linearLayoutFragmentos.removeAllViews();
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragmentos,new FragmentoScanner()).commit();
                        break;
                    }case R.id.historico:{
                        linearLayoutFragmentos.removeAllViews();
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragmentos,new HistoricoFragmento()).commit();
                        break;
                    }

                }
                return true;
            }
        });


}

    public  void montarList(Context context){
        String str = lerArquivo();
        if(str.length()>1){
            String []vetor_str = str.split("\n");
            for (String i:vetor_str){
                lista_links_str.add(i);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context
                , android.R.layout.simple_selectable_list_item
                , android.R.id.text1
                , lista_links_str);
        listView.setAdapter(adapter);
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
                    if(resultado.contains("@")) {
                        Log.i("linhass",resultado);
                        resultado += linha;
                    }
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
                Toast toast = Toast.makeText(this, "Item excluido", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.v("MainActivity", e.toString());
            }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Inicio.class);
        startActivity(intent);
    }


}
