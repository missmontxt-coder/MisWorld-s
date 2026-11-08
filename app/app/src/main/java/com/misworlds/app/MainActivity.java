
package com.misworlds.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;

import java.util.Calendar;

public class MainActivity extends Activity {

    EditText nombre;
    TextView fecha;
    CheckBox tutor;
    Calendar nacimiento = Calendar.getInstance();
    boolean fechaElegida = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mostrarRegistro();
    }

    private void mostrarRegistro() {
        int blanco = Color.WHITE;
        int cyan = Color.rgb(80, 220, 255);

        LinearLayout fondo = new LinearLayout(this);
        fondo.setOrientation(LinearLayout.VERTICAL);
        fondo.setGravity(Gravity.CENTER);
        fondo.setPadding(28, 35, 28, 35);
        fondo.setBackground(new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{0xFF100B30, 0xFF25105B, 0xFF080D25}
        ));

        ScrollView scroll = new ScrollView(this);
        scroll.addView(fondo);

        TextView titulo = new TextView(this);
        titulo.setText("MISWORLD'S");
        titulo.setTextColor(cyan);
        titulo.setTextSize(34);
        titulo.setTypeface(null, Typeface.BOLD);
        titulo.setGravity(Gravity.CENTER);
        fondo.addView(titulo);

        TextView bienvenida = new TextView(this);
        bienvenida.setText("\nBienvenido al multiverso\n\nCrea tu identidad de explorador");
        bienvenida.setTextColor(blanco);
        bienvenida.setTextSize(19);
        bienvenida.setGravity(Gravity.CENTER);
        fondo.addView(bienvenida);

        nombre = new EditText(this);
        nombre.setSingleLine(true);
        nombre.setHint("¿Cómo te llamas?");
        nombre.setTextColor(blanco);
        nombre.setHintTextColor(0xFFBDB5D5);
        fondo.addView(nombre, parametros());

        Button elegirFecha = new Button(this);
        elegirFecha.setText("Seleccionar fecha de nacimiento");
        fondo.addView(elegirFecha, parametros());

        fecha = new TextView(this);
        fecha.setText("Fecha de nacimiento no seleccionada");
        fecha.setTextColor(blanco);
        fecha.setGravity(Gravity.CENTER);
        fondo.addView(fecha, parametros());

        tutor = new CheckBox(this);
        tutor.setText("Soy madre, padre o tutor y autorizo el registro del menor.");
        tutor.setTextColor(blanco);
        fondo.addView(tutor, parametros());

        Button continuar = new Button(this);
        continuar.setText("CREAR MI IDENTIDAD");
        fondo.addView(continuar, parametros());

        elegirFecha.setOnClickListener(v -> seleccionarFecha());

        continuar.setOnClickListener(v -> registrar());

        setContentView(scroll);
    }

    private LinearLayout.LayoutParams parametros() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
            -1, -2
        );
        p.setMargins(0, 18, 0, 8);
        return p;
    }

    private void seleccionarFecha() {
        Calendar hoy = Calendar.getInstance();

        DatePickerDialog dialogo = new DatePickerDialog(
            this,
            (vista, year, month, day) -> {
                nacimiento.set(year, month, day);
                fechaElegida = true;
                fecha.setText(day + "/" + (month + 1) + "/" + year);
            },
            hoy.get(Calendar.YEAR) - 18,
            hoy.get(Calendar.MONTH),
            hoy.get(Calendar.DAY_OF_MONTH)
        );

        dialogo.getDatePicker().setMaxDate(hoy.getTimeInMillis());
        dialogo.show();
    }

    private void registrar() {
        String nombreUsuario = nombre.getText().toString().trim();

        if (nombreUsuario.isEmpty()) {
            nombre.setError("Escribe tu nombre");
            return;
        }

        if (!fechaElegida) {
            Toast.makeText(this, "Selecciona tu fecha de nacimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar hoy = Calendar.getInstance();
        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);

        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }

        if (edad < 18 && !tutor.isChecked()) {
            Toast.makeText(this, "Se necesita autorización de madre, padre o tutor.", Toast.LENGTH_LONG).show();
            return;
        }

        String grupo = edad < 13 ? "infantil" : edad < 18 ? "adolescente" : "adulto";

        getSharedPreferences("MisWorlds", MODE_PRIVATE)
            .edit()
            .putString("nombre", nombreUsuario)
            .putString("grupo_edad", grupo)
            .apply();

        new AlertDialog.Builder(this)
            .setTitle("¡Bienvenido, " + nombreUsuario + "!")
            .setMessage("Tu identidad de explorador está preparada.")
            .setPositiveButton("Continuar", (d, w) -> {
                Toast.makeText(this, "Próximamente: tu universo.", Toast.LENGTH_SHORT).show();
            })
            .show();
    }
  }
      
