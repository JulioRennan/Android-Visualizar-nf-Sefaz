package primeiropp.studio.com.escannner;


import androidx.appcompat.view.menu.MenuAdapter;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricoFragmento extends Fragment {
    private LinearLayout  item;
    public static String historico;
    public HistoricoFragmento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_historico_fragmento, container, false);
        final List<String> list = new ArrayList<>();
        ExpandableListView listView = (ExpandableListView)view.findViewById(R.id.listview_historico);
        historico = lerHistorico();
        try {
            listView.setAdapter(new MeuAdapter(getContext()));
        }catch (Exception e){

        }
        return view;

    }

    private String lerHistorico() {
        try {
            InputStream arq = (getActivity().openFileInput("Historico_"+MainActivity.arquivo));
            String resultado = "";
            if (arq != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(arq);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String linha = "";
                while ((linha = bufferedReader.readLine()) != null) {
                    if (linha.contains("@")) {
                        resultado += linha;
                    }
                }
                arq.close();
                return resultado;
            }
        } catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }
        return null;
    }

}
