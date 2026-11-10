package com.example.tsdmh_7s5_prac3_lfrm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrincipalActivity extends AppCompatActivity {
    TextView tvEstado, tvPorcentaje;
    ProgressBar progressBar;
    Button btnIniciar, btnCancelar;
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final Handler handler = new Handler(Looper.getMainLooper());
    Future<?> tarea;

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
        tvEstado = findViewById(R.id.tvEstado);
        tvPorcentaje = findViewById(R.id.tvPorcentaje);
        progressBar = findViewById(R.id.progressBar);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnIniciar.setOnClickListener(v-> IniciarTarea(100));
        btnCancelar.setOnClickListener(v -> {
            if(tarea != null && !tarea.isDone()){
                tarea.cancel(true);
            }
        });
    }

    //equivale a onPreExecute
    private void IniciarTarea(int pasos){
        tvEstado.setText("Iniciando Tarea");
        btnIniciar.setEnabled(false);
        btnCancelar.setEnabled(true);
        tvPorcentaje.setText("0%");
        //realizamos lo equivalente a doInBackground
        tarea = executor.submit(() -> {
            for(int i = 0; i <= pasos; i++){
                if(Thread.currentThread().isInterrupted()){
                    handler.post((this::alCancelar));
                    return;
                }
                try{
                    Thread.sleep(80);
                } catch (InterruptedException e){
                    handler.post(this::alCancelar);
                    return;
                }
                //realizamos lo equivalente a onProgressUpdate + publishProgress
                final int progreso = (i * 100) / pasos;
                handler.post(() -> {
                    tvPorcentaje.setText(progreso + "%");
                    progressBar.setProgress(progreso);
                });
            }
            //realizamos lo equivalente a onPostExecute
            handler.post(() -> alFinalizar("Tarea finalizada"));

        });

    }

    private  void alCancelar(){
        tvEstado.setText("Tarea Cancelada");
        tvPorcentaje.setText("0%");
        progressBar.setProgress(0);
        btnIniciar.setEnabled(true);
        btnCancelar.setEnabled(false);
    }

    private void alFinalizar(String resultado){
        tvEstado.setText(resultado);
        tvPorcentaje.setText("0%");
        progressBar.setProgress(0);
        btnIniciar.setEnabled(true);
        btnCancelar.setEnabled(false);

    }

    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        executor.shutdown();
    }

}