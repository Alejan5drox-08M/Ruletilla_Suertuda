package com.example.ruletasuerte_isaacalejandro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mibinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Restablecer el valor predeterminado al iniciar la aplicación
        val sharedPreferences = getSharedPreferences("panelData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Borra todos los valores guardados
        editor.apply()


        mibinding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(mibinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mibinding.botonComenzar.setOnClickListener{
            val opcionSeleccionada = when (mibinding.radioGroupjugadores.checkedRadioButtonId) {
                mibinding.radioButtonJugadores2.id -> 2
                mibinding.radioButtonJugadores3.id -> 3
                mibinding.radioButtonJugadores4.id -> 4
                else -> 0
            }
            if(opcionSeleccionada==0){
                Toast.makeText(this,"Selecciona un número de jugadores", Toast.LENGTH_SHORT).show()
            }else{
                // Guardar el número de jugadores en SharedPreferences
                val sharedPreferences = getSharedPreferences("gameData", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("numeroJugadores", opcionSeleccionada)
                editor.apply()

                val miIntent = Intent(this, MainActivity2::class.java)
                startActivity(miIntent)
            }
        }
    }
}