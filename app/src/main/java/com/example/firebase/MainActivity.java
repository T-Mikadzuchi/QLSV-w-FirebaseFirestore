package com.example.firebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties

public class MainActivity extends AppCompatActivity {
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog pd = new ProgressDialog(this);
        EditText name = (EditText) findViewById(R.id.name);
        EditText mssv = (EditText) findViewById(R.id.id);
        Button add = (Button) findViewById(R.id.btnAdd);
        Button del = (Button) findViewById(R.id.btnDelete);
        Button update = (Button) findViewById(R.id.btnUpdate);
        ListView lv = (ListView) findViewById(R.id.listview);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ArrayList<SV> svList = new ArrayList<SV>();
        ArrayList<String> idList = new ArrayList<String>();
        UserAdapter adapter = new UserAdapter(this, svList);
        lv.setAdapter(adapter);

        pd.setTitle("Loading Data...");
        pd.show();
        db.collection("sv")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SV sv = new SV(document.getString("hoten"), document.getString("mssv"));
                                svList.add(sv);
                                idList.add(document.getId());
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                pd.dismiss();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            pd.dismiss();
                        }
                    }
                });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setTitle("Adding data...");
                pd.show();
                SV sv = new SV(name.getText().toString(), mssv.getText().toString());
                db.collection("sv")
                        .add(sv)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                pd.dismiss();
                                svList.add(sv);
                                idList.add(documentReference.getId());
                                adapter.notifyDataSetChanged();
                                name.setText("");
                                mssv.setText("");
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setTitle("Deleting data...");
                pd.show();
                SV sv = new SV(name.getText().toString(), mssv.getText().toString());

                db.collection("sv").document(idList.get(pos).toString())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                svList.remove(pos);
                                adapter.notifyDataSetChanged();
                                name.setText("");
                                mssv.setText("");
                                pd.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                    }
                });
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setTitle("Updating data...");
                pd.show();
                SV sv = new SV(name.getText().toString(), mssv.getText().toString());

                db.collection("sv").document(idList.get(pos).toString())
                        .update("hoten", name.getText().toString(), "mssv", mssv.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                svList.get(pos).setHoten(name.getText().toString());
                                svList.get(pos).setMssv(mssv.getText().toString());
                                adapter.notifyDataSetChanged();
                                name.setText("");
                                mssv.setText("");
                                pd.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                    }
                });
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ten = svList.get(i).getHoten();
                String ms = svList.get(i).getMssv();
                name.setText(ten);
                mssv.setText(ms);
                pos = i;
            }
        });
    }
}