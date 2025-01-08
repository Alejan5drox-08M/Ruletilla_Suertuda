package com.example.ruletasuerte_isaacalejandro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMain2Binding
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMain3Binding


class MainActivity3 : AppCompatActivity() {
    lateinit var mibinding: ActivityMain3Binding
    lateinit var mibinding2: ActivityMain2Binding
    val panel = Paneles()
    var frase = ""
    var pista = ""
    var estadoFrase = ""
    lateinit var gestorTurnos: GestorTurnos
    lateinit var builder: Builder
    var jugadores=Jugadores()
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mibinding = ActivityMain3Binding.inflate(layoutInflater)
        mibinding2 = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(mibinding.root)

        // Inicializar el botón deshabilitado
        mibinding.botonTirarotravez.isVisible = false

        // Recuperar el número de jugadores de SharedPreferences
        sharedPreferences = getSharedPreferences("gameData", MODE_PRIVATE)
        val numeroJugadores = sharedPreferences.getInt("numeroJugadores", 2) // 2 es el valor por defecto
        val multiplicador= intent.getIntExtra("multiplicador",0)
        gestorTurnos=GestorTurnos(numeroJugadores,sharedPreferences)
        builder=Builder()
        jugadores.cargarPuntuaciones(sharedPreferences)
        val jugadorActual = sharedPreferences.getInt("jugadorActual", 1)


        if (numeroJugadores == 2) {
            mibinding2.layoutJugador3.isVisible = false
            mibinding2.layoutJugador4.isVisible = false
        } else if (numeroJugadores == 3) {
            mibinding2.layoutJugador3.isVisible = true
            mibinding2.layoutJugador4.isVisible = false
        }



        val sharedPreferences = getSharedPreferences("panelData", Context.MODE_PRIVATE)
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
            comprobarLetra(mibinding.editTextTextConsonante,"aeiou", "Introduce una consonante válida",multiplicador,jugadorActual)
        }

        mibinding.botonVocal.setOnClickListener {
            comprobarLetra(mibinding.editTextTextVocal,"bcdfghjklmnñpqrstvwxyz", "Introduce una vocal válida",multiplicador,jugadorActual)
            /*mibinding.botonVocal.isEnabled = false
            mibinding.editTextTextVocal.isEnabled = false*/
        }

        // Cuando el jugador resuelve el panel, navega hacia MainActivity2
        mibinding.botonResolver.setOnClickListener {
            resolverPanel(jugadorActual)
        }
        mibinding.botonTirarotravez.setOnClickListener {
            Intent()
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
    private fun resolverPanel(jugadorActual:Int){
        val solucion = mibinding.editTextTextResolver.text.toString().trim()
        val fraseOriginal = frase.trim()

        if (solucion.equals(fraseOriginal, ignoreCase = true)){
            builder.dialogIntent("GANASTE","Has ganado, enhorabuena!!","Vale",this,MainActivity::class.java)
            obtenerNuevoPanel()
        } else {
            jugadores.puntuaciones[jugadorActual-1]=0
            jugadores.guardarPuntuaciones(sharedPreferences)
            mibinding.editTextTextResolver.text.clear()
            val editor = sharedPreferences.edit()
            val siguienteJugador = gestorTurnos.siguienteTurno() // Actualizar turno
            editor.putInt("jugadorActual", siguienteJugador) // Guardamos el siguiente turno
            editor.apply()
            builder.dialogIntent("FALLASTE","Has fallado, tu puntuacion se ha reducido a 0 y pierdes turno!!","Vale",this,MainActivity2::class.java)

        }
    }



    fun comprobarLetra(editText: EditText, letras: String, mensaje: String, multiplicador: Int,jugador:Int) {
        var sharedPreferences = getSharedPreferences("panelData", Context.MODE_PRIVATE)
        val letra = editText.text.toString().uppercase().firstOrNull()

        // Validar que la letra es una consonante
        if (letra == null || letra in letras || !letra.isLetter()) {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        } else {
            // Comprobar si la letra está en la frase
            if (!frase.contains(letra, ignoreCase = true)) {
                // Si la letra no está en la frase, mostrar un Toast y regresar
                Toast.makeText(this, "La letra no está en la frase. Pierdes el turno", Toast.LENGTH_SHORT).show()
                val editor = sharedPreferences.edit()
                val siguienteJugador = gestorTurnos.siguienteTurno() // Actualizar turno
                editor.putInt("jugadorActual", siguienteJugador) // Guardamos el siguiente turno
                editor.apply()
                Intent()
            } else {
                if(letras=="aeiou"){
                    val repeticiones = frase.count { it.equals(letra, ignoreCase = true) }
                    var puntos=0
                    // Calcular los puntos
                    puntos += repeticiones * multiplicador  // Multiplicamos las repeticiones por el multiplicador

                    // Mostrar los puntos acumulados
                    Toast.makeText(this, "Puntos: $puntos", Toast.LENGTH_SHORT).show()
                    puntuaciones(jugador,puntos)
                }

                // Construir la nueva frase basada en la consonante
                val nuevaFrase = frase.mapIndexed { index, char ->
                    when {
                        char == ' ' -> ' ' // Mantener los espacios
                        char.equals(letra, ignoreCase = true) -> letra // Reemplazar la consonante si coincide
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
                if(letras=="aeiou"){
                    mibinding.botonTirarotravez.isVisible = true
                    mibinding.botonConsonante.isEnabled = false
                    mibinding.editTextTextConsonante.isEnabled = false
                }
            }
        }
    }
    fun puntuaciones(jugadorActual:Int,puntos:Int){
        jugadores.puntuaciones[jugadorActual - 1] +=puntos

        // Guardar las puntuaciones actualizadas en SharedPreferences
        val sharedPreferences = getSharedPreferences("gameData", Context.MODE_PRIVATE)
        jugadores.guardarPuntuaciones(sharedPreferences)
    }
    // Función de extensión para transformar una frase a su forma oculta
    fun String.ocultar(): String {
        return this.map { if (it.isLetter()) '_' else it }.joinToString("")
    }
    fun Intent(){
        val intent = Intent(this, MainActivity2::class.java)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}