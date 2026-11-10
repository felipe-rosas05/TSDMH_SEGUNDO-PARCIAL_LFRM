package com.example.tsdmh_7s5_prac1_lfrm;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {
    Spinner p1sp1, p1sp2;
    ArrayList<String> milista = new ArrayList<>();
    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        milista.add("Manzana");
        milista.add("Pera");
        milista.add("Fresa");
        milista.add("Mango");
        milista.add("Melon");
        p1sp1 = findViewById(R.id.p1sp1);
        p1sp2 = findViewById(R.id.p1sp2);
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,milista);
        p1sp2.setAdapter(adaptador);
        p1sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valor = parent.getItemAtPosition(position).toString();
                Toast.makeText(PrincipalActivity.this, "Valor Seleccionado: " + valor, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        p1sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cadena = parent.getItemAtPosition(position).toString();
                Toast.makeText(PrincipalActivity.this, "Fruta Seleccionada: " + cadena, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void Btnclick(View v){
        if(v.getId() == R.id.p1btn1){
            Toast.makeText(this, "Se presiono el boton 1", Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.p1btn2) {
            Toast.makeText(this, "Se presiono el boton 2", Toast.LENGTH_SHORT).show();

        }
    }
}