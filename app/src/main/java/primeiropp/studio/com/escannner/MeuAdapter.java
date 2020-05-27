package primeiropp.studio.com.escannner;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MeuAdapter extends BaseExpandableListAdapter {

    Context context;

// Definindo o conteúdo de nossa lista e sublista

    String[] listaPai ;
    String[][] listafilho;

    public MeuAdapter(Context context) {
        this.context = context;

        String aux []= HistoricoFragmento.historico.split("@@");
        for(String i:aux){
            Log.i("resultadoo",i);
        }
        listaPai = new String[aux.length];
        for(int i = 0;i<aux.length;i++){
            listaPai[i] = aux[i].split(";")[0];
        }
        listafilho = new String[aux.length][];
        for(int i = 0;i<aux.length;i++){

            listafilho[i] = aux[i].replace(listaPai[i]+";","").split(";");
        }

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
// TODO Auto-generated method stub
        return listafilho[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
// TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

// Criamos um TextView que conterá as informações da listafilho que
// criamos
        TextView textViewSubLista = new TextView(context);
        textViewSubLista.setTextSize(20);

        textViewSubLista.setText(listafilho[groupPosition][childPosition].split("@")[0]);
// Definimos um alinhamento
        textViewSubLista.setPadding(10, 5, 0, 5);

        return textViewSubLista;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
// TODO Auto-generated method stub
        return listafilho[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
// TODO Auto-generated method stub
        return listaPai[groupPosition];
    }

    @Override
    public int getGroupCount() {
// TODO Auto-generated method stub
        return listaPai.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
// TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

// Criamos um TextView que conterá as informações da listaPai que
// criamosReference:	@android:drawable/arrow_down_float
        TextView textViewCategorias = new TextView(context);
        textViewCategorias.setTextSize(50);
        textViewCategorias.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.arrow_down_float,0);
        textViewCategorias.setText(listaPai[groupPosition]);
// Definimos um alinhamento
        textViewCategorias.setPadding(30, 5, 0, 5);
// Definimos o tamanho do texto
        textViewCategorias.setTextSize(20);
// Definimos que o texto estará em negrito
        textViewCategorias.setTypeface(null, Typeface.BOLD);

        return textViewCategorias;
    }

    @Override
    public boolean hasStableIds() {
// TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
// Defina o return como sendo true se vc desejar que sua sublista seja selecionável
        return false;
    }

}