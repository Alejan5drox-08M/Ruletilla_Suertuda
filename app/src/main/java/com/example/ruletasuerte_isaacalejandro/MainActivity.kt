package com.example.ruletasuerte_isaacalejandro

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
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

            var miIntent = Intent(this, MainActivity2::class.java)
            miIntent.putExtra("numeroJugadores", opcionSeleccionada)
            if (miIntent.resolveActivity(packageManager) != null){
                startActivity(miIntent)
            }
        }
    }
}