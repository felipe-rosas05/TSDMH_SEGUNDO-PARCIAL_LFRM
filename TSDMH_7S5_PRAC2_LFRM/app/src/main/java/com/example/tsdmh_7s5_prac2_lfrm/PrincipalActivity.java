package com.example.tsdmh_7s5_prac2_lfrm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.content.AsyncTaskLoader;

import java.lang.ref.WeakReference;

public class PrincipalActivity extends AppCompatActivity {

    TextView tvEstado, tvPorcentaje;
    ProgressBar progressBar;
    Button btnIniciar, btnCancelar;

    DescargaTask tarea;

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

        btnIniciar.setOnClickListener (v -> {
            if (tarea == null && tarea.getStatus() != AsyncTask.Status.RUNNING){
                tarea = new DescargaTask(this);
                tarea.execute(100);
            }
        });

        btnCancelar.setOnClickListener(v -> {
            if (tarea != null && tarea.getStatus() == AsyncTask.Status.RUNNING){
                tarea.cancel(true);
                tarea=null;
            }
        });
    }
    private static class DescargaTask extends AsyncTask<Integer, Integer, String>{
        private final WeakReference<PrincipalActivity> Ref;

        public DescargaTask(PrincipalActivity activity){
            Ref = new WeakReference<>(activity);
        }

        @Override
        protected void onCancelled() {
            PrincipalActivity activity = Ref.get();
            if (activity == null || activity.isFinishing()) return;
            activity.tvEstado.setText("Tarea cancelada");
            activity.progressBar.setProgress(0);
            activity.tvPorcentaje.setText("0%");
            activity.btnIniciar.setEnabled(true);
            activity.btnCancelar.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            PrincipalActivity activity = Ref.get();
            if (activity == null || activity.isFinishing()) return;
            activity.tvEstado.setText(s);
            activity.btnIniciar.setEnabled(true);
            activity.btnCancelar.setEnabled(false);
            Toast.makeText(activity, "s", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            PrincipalActivity activity = Ref.get();
            if (activity == null || activity.isFinishing()) return;
            activity.tvEstado.setText("Listo para iniciar");
            activity.progressBar.setProgress(0);
            activity.tvPorcentaje.setText("0%");
            activity.btnIniciar.setEnabled(false);
            activity.btnCancelar.setEnabled(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            PrincipalActivity activity = Ref.get();
            if (activity == null || activity.isFinishing()) return;
            activity.progressBar.setProgress(values[0]);
            activity.tvPorcentaje.setText(values[0] + "%");
        }

        @Override
        protected String doInBackground(Integer... params) {
            int pasos = params[0];
            for (int i = 0; i < pasos && !isCancelled(); i++){
                if (isCancelled()) return "Tarea cancelada";
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    return "Interrupcion de la tarea";
                }
                publishProgress(i*100/pasos);
            }
            return "Tarea finalizada";
        }
    }
}