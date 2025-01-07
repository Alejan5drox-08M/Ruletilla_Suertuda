package com.example.ruletasuerte_isaacalejandro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMain2Binding
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMain3Binding


class MainActivity3 : AppCompatActivity() {
    lateinit var mibinding: ActivityMain3Binding
    lateinit var mibinding2: ActivityMain2Binding
    val panel = Paneles()  // Instancia de la clase Paneles
    var frase = ""
    var pista = ""
    var estadoFrase = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mibinding = ActivityMain3Binding.inflate(layoutInflater)
        mibinding2 = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(mibinding.root)

        // Inicializar el botón deshabilitado
        mibinding.botonTirarotravez.isVisible = false


        // Recuperar el número de jugadores de SharedPreferences
        var sharedPreferences = getSharedPreferences("gameData", MODE_PRIVATE)
        val numeroJugadores = sharedPreferences.getInt("numeroJugadores", 2) // 2 es el valor por defecto

        if (numeroJugadores == 2) {
            mibinding2.layoutJugador3.isVisible = false
            mibinding2.layoutJugador4.isVisible = false
        } else if (numeroJugadores == 3) {
            mibinding2.layoutJugador3.isVisible = true
            mibinding2.layoutJugador4.isVisible = false
        }



        // Recuperar la frase y la pista desde SharedPreferences
        sharedPreferences = getSharedPreferences("panelData", Context.MODE_PRIVATE)
        frase = sharedPreferences.getString("frase", "") ?: ""
        pista = sharedPreferences.getString("pista", "") ?: ""
        estadoFrase = sharedPreferences.getString("estadoFrase", "").orEmpty()
        if (frase.isEmpty() || pista.isEmpty()) {
            obtenerNuevoPanel() // Obtiene una nueva frase y pista
        }

        // Si no se ha guardado el estado de la frase, inicializamos con guiones bajos
        if (estadoFrase.isEmpty()) {
            estadoFrase = frase.ocultar()
        }

        mibinding.textViewPanel.text = estadoFrase
        mibinding.textViewPista.text = pista

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Listener del botón Consonante
        mibinding.botonConsonante.setOnClickListener {
            comprobarLetra(mibinding.editTextTextConsonante,"aeiou", "Introduce una consonante válida")
            // Habilitar el botón ya que la consonante es válida
            mibinding.botonTirarotravez.isVisible = true
            mibinding.botonConsonante.isEnabled = false
            mibinding.editTextTextConsonante.isEnabled = false
        }

        mibinding.botonVocal.setOnClickListener {
            comprobarLetra(mibinding.editTextTextVocal,"bcdfghjklmnñpqrstvwxyz", "Introduce una vocal válida")
            /*mibinding.botonVocal.isEnabled = false
            mibinding.editTextTextVocal.isEnabled = false*/
        }

        // Cuando el jugador resuelve el panel, navega hacia MainActivity
        mibinding.botonResolver.setOnClickListener {
            val solucion = mibinding.editTextTextResolver.text.toString().trim()
            val fraseOriginal = frase.trim()

            if (solucion.equals(fraseOriginal, ignoreCase = true)){
                val builder = AlertDialog.Builder(this@MainActivity3)
                builder.setTitle("GANASTE")
                builder.setMessage("Has ganado, enhorabuena!!")
                builder.setPositiveButton("Vale") { dialog, which ->
                    val intent = Intent(this, MainActivity::class.java)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
                val dialog = builder.create()
                dialog.show()
                obtenerNuevoPanel()
            } else {
                val builder = AlertDialog.Builder(this@MainActivity3)
                builder.setTitle("FALLASTE")
                builder.setMessage("Has fallado, tu puntuacion se ha reducido a 0 y pierdes turno")
                builder.setPositiveButton("Vale") { dialog, which ->
                    mibinding.editTextTextResolver.text.clear()
                    val intent = Intent(this, MainActivity2::class.java)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
        mibinding.botonTirarotravez.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    // Método para obtener un nuevo panel
    private fun obtenerNuevoPanel() {
        // Obtener una nueva frase y pista
        frase = panel.obtener_Panel()  // Esto también guardará en SharedPreferences
        pista = panel.obtener_Pista()

        // Guardar la nueva frase y pista en SharedPreferences
        val sharedPreferences = getSharedPreferences("panelData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("frase", frase)
        editor.putString("pista", pista)
        editor.putString("estadoFrase", frase.ocultar()) // Inicializar la frase oculta
        editor.apply()
    }

    fun comprobarLetra(editText: EditText,letras: String, mensaje: String) {
        val sharedPreferences = getSharedPreferences("panelData", Context.MODE_PRIVATE)
        val letra = editText.text.toString().lowercase().firstOrNull()

        // Validar que la letra es una consonante
        if (letra == null || letra in letras || !letra.isLetter()) {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            //mibinding.botonTirarotravez.isEnabled = false // Deshabilitar si no es válida
        } else {
            // Construir la nueva frase basada en la consonante
            val nuevaFrase = frase.mapIndexed { index, char ->
                when {
                    char == ' ' -> ' ' // Mantener los espacios
                    char.equals(
                        letra,
                        ignoreCase = true
                    ) -> letra // Reemplazar la consonante si coincide
                    index < estadoFrase.length -> estadoFrase[index] // Mantener el estado actual
                    else -> "_" // Reemplazar con guion bajo sin espacio
                }
            }.joinToString("")
            // Actualizar la vista con la nueva frase
            mibinding.textViewPanel.text = nuevaFrase
            estadoFrase = nuevaFrase

            // Guardar el nuevo estado en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("estadoFrase", nuevaFrase)
            editor.apply()

            // Limpiar el EditText después de poner la consonante
            editText.text.clear()
        }
    }

    // Función de extensión para transformar una frase a su forma oculta
    fun String.ocultar(): String {
        return this.map { if (it.isLetter()) '_' else it }.joinToString("")
    }
}