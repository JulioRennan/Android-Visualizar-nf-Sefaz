package primeiropp.studio.com.escannner.config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import primeiropp.studio.com.escannner.MainActivity;

public class Firebase {
    private DatabaseReference fb = FirebaseDatabase.getInstance().getReference().child("NFS");
    public void subirNf(Nf nf,String data,int id){
        fb.child(data+"@"+ MainActivity.destino).child(String.valueOf(id)).setValue(nf);
        String fbNf = nf.getNf();
        String fbNome = nf.getNome();
        DatabaseReference dowload = fb.child("Dowload");
    }

}
