package primeiropp.studio.com.escannner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import primeiropp.studio.com.escannner.config.Firebase;
import primeiropp.studio.com.escannner.config.Nf;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListarNfs extends Fragment {
    private ArrayList<String[] > lista_nf_com_link = new ArrayList<>();
    private ArrayList<String> lista_nfs = new ArrayList<>();
    private ListView listView;
    private String baseLink = "https://sistemas.sefaz.am.gov.br/nfceweb/consultarNFCe.jsp?p=";
    private int pos;
    LinearLayout btn_exportar;
    String dataFormatada;
    public ListarNfs() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_nfs, container, false);
        listView = (ListView) view.findViewById(R.id.listview_historico);
        gerarListView(getContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item[] = lista_nf_com_link.get(i);
                Bundle bundle = new Bundle();
                bundle.putString("Nome",item[0]);
                bundle.putString("Link",item[1]);
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                AlertDialog.Builder alertDialog= gerarAlert();
                alertDialog.show();

            return true;
            }
        });


        btn_exportar = (LinearLayout) view.findViewById(R.id.btn_exportar);
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        dataFormatada = formataData.format(date)+" as "+sdf.format(hora);
        btn_exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Firebase fb = new Firebase();
                for(int i =0;i<lista_nf_com_link.size();i++){
                    Nf nf = new Nf();
                    nf.setNome(lista_nf_com_link.get(i)[0]);
                    nf.setNf(lista_nf_com_link.get(i)[1]);
                    fb.subirNf(nf,dataFormatada,i);
                    gravarHistorico();
                }
                Toast.makeText(getContext(),"Upando Arquivos...",Toast.LENGTH_LONG).show();


            }
        });

        return view;
    }
    private void gravarHistorico() {
            String str =dataFormatada+";"+lerArquivo();
        try {
            InputStream arq = (getActivity().openFileInput("Historico_"+MainActivity.arquivo));
            String resultado = "";
            if (arq != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(arq);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String linha = "";
                while ((linha = bufferedReader.readLine()) != null) {
                    if (linha.contains("@")) {
                        Log.i("resultadoo",linha);
                        resultado += linha;
                    }
                }
                Log.i("resultadoo",resultado);
                str+="@@"+resultado;
                arq.close();
            }
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput("Historico_"+MainActivity.arquivo, Context.MODE_PRIVATE));
            outputStreamWriter.write(str);

            outputStreamWriter.close();
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput(MainActivity.arquivo, Context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.close();

            gerarListView(getContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Arquivos repassados para o histórico");
            builder.create();
            builder.show();
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
                    if (linha.contains("@")) {
                        resultado += linha;
                    }
                }
                String str[] = resultado.split(";");
                for(String i:str){
                    Log.i("linhas",i);
                }
                arq.close();
            }
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }

        return resultado;
    }

    private void gerarListView(Context context){
        lista_nf_com_link.clear();
        lista_nfs.clear();
        String[] arq = lerArquivo().split(";");
        if(arq.length >0){
            for (String i: arq){
                String[]aux = i.split("@");
                if(aux.length>1){
                    lista_nf_com_link.add(i.split("@"));
                }
            }

            for(String[]i:lista_nf_com_link){
                String nomeFormatado = "Nome: "+i[0];
                lista_nfs.add(nomeFormatado);
            }

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context
                , android.R.layout.simple_expandable_list_item_2
                , android.R.id.text1
                , lista_nfs);
        listView.setAdapter(adapter);
    }
    private AlertDialog.Builder gerarAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        alertDialog.setMessage("Certeza que deseja remover essa nota?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                lista_nf_com_link.get(pos);
                excluirItem();
                gerarListView(getContext());

            }
        });
        alertDialog.setNegativeButton("Nao",null);
        alertDialog.create();
        return alertDialog;
    }
    private void excluirItem() {
        try {
            lista_nf_com_link.remove(pos);
            String resultado = "";
            for(String[] i:lista_nf_com_link){
                resultado+=String.join("@",i)+";";
            }


            Log.i("resultadoo",String.join("\n",resultado));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput(MainActivity.arquivo, Context.MODE_PRIVATE));
            outputStreamWriter.write(resultado);
            Toast toast = Toast.makeText(getContext(), "Item excluido", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }
    }

}
