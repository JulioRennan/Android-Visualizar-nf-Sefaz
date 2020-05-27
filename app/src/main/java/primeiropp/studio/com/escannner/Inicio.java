package primeiropp.studio.com.escannner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.sleep;

public class Inicio extends AppCompatActivity {
    TextView txt_btn;

    List<TextView> lista_txt_view = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        txt_btn = (TextView) findViewById(R.id.txt_kendjam);
        lista_txt_view.add(txt_btn);
        txt_btn = (TextView) findViewById(R.id.txt_pirarucu);
        lista_txt_view.add(txt_btn);
        txt_btn = (TextView) findViewById(R.id.txt_marie);
        lista_txt_view.add(txt_btn);
        txt_btn = (TextView) findViewById(R.id.txt_uamazon);
        lista_txt_view.add(txt_btn);
        txt_btn = (TextView) findViewById(R.id.txt_Xingu);
        lista_txt_view.add(txt_btn);
        for(final TextView i:lista_txt_view){
            i.setClickable(true);
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i.setBackgroundColor(Color.WHITE);
                    i.setTextColor(Color.GRAY);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("destino",i.getText().toString());
                    intent.putExtra("destino",bundle);
                    Toast.makeText(getApplicationContext(),i.getText().toString(),Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

}
