package com.example.ruletasuerte_isaacalejandro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    lateinit var mibinding: ActivityMain3Binding
    val panel = Paneles()  // Instancia de la clase Paneles
    var frase = ""
    var pista = ""
    var estadoFrase = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mibinding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(mibinding.root)

        // Recuperar la frase y la pista desde SharedPreferences
        val sharedPreferences = getSharedPreferences("panelData", Context.MODE_PRIVATE)
        frase = sharedPreferences.getString("frase", "Frase no disponible") ?: "Frase no disponible"
        pista = sharedPreferences.getString("pista", "Pista no disponible") ?: "Pista no disponible"
        estadoFrase = sharedPreferences.getString("estadoFrase", "").orEmpty()

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
            // Llamamos al nuevo método comprobarConsonante
            comprobarLetra(mibinding.editTextTextConsonante,"aeiou", "Introduce una consonante válida")
        }

        mibinding.botonVocal.setOnClickListener {
            comprobarLetra(mibinding.editTextTextVocal,"bcdfghjklmnñpqrstvwxyz", "Introduce una vocal válida")

        }

        // Cuando el jugador resuelve el panel, navega hacia MainActivity2
        mibinding.botonResolver.setOnClickListener {
            // Guardar un nuevo panel y su pista en SharedPreferences
            obtenerNuevoPanel()

            // Navegar a MainActivity2
            val intent = Intent(this, MainActivity2::class.java)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
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
            // Validar que se introduzca una consonante válida
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
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

            // Limpiar el EditText después de procesar la consonante
            editText.text.clear()
        }
    }

    // Función de extensión para transformar una frase a su forma oculta
    fun String.ocultar(): String {
        return this.map { if (it.isLetter()) '_' else it }.joinToString("")
    }
}