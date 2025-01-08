package com.example.ruletasuerte_isaacalejandro

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMain2Binding
import kotlin.random.Random

@Suppress("DEPRECATION")
class MainActivity2 : AppCompatActivity() {
    private var degree = 0
    private var degreeOld = 0
    private val factor:Float= 11.25f
    private val jugadores = Jugadores()
    lateinit var builder: Builder
    lateinit var gestorTurnos: GestorTurnos
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var mibinding: ActivityMain2Binding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mibinding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(mibinding.root)

        // Recuperar el número de jugadores de SharedPreferences
        sharedPreferences = getSharedPreferences("gameData", MODE_PRIVATE)
        val numeroJugadores = sharedPreferences.getInt("numeroJugadores", 2) // 2 es el valor por defecto
        gestorTurnos= GestorTurnos(numeroJugadores,sharedPreferences)
        builder= Builder()
        if (numeroJugadores == 2) {
            mibinding.layoutJugador3.isVisible = false
            mibinding.layoutJugador4.isVisible = false
        } else if (numeroJugadores == 3) {
            mibinding.layoutJugador3.isVisible = true
            mibinding.layoutJugador4.isVisible = false
        }

        jugadores.cargarPuntuaciones(sharedPreferences)

        // Actualizar las etiquetas de puntos
        mibinding.puntos1.text = jugadores.puntuaciones[0].toString()
        mibinding.puntos2.text = jugadores.puntuaciones[1].toString()
        mibinding.puntos3.text = jugadores.puntuaciones[2].toString()
        mibinding.puntos4.text = jugadores.puntuaciones[3].toString()

        mibinding.botonTirar.setOnClickListener {
            mibinding.botonTirar.isEnabled = false
            val siguienteJugador = gestorTurnos.contador
            editor(siguienteJugador,sharedPreferences)
            Toast.makeText(this, "Jugador $siguienteJugador", Toast.LENGTH_SHORT).show()
            startRotation(siguienteJugador)

            val handler = android.os.Handler()
            handler.postDelayed({
                mibinding.botonTirar.isEnabled = true
            }, 4000)
        }
    }
    private fun startRotation(jugadorActual: Int) {
        degreeOld = degree % 360
        degree = Random.nextInt(3600) + 720

        val rotate = RotateAnimation(
            degreeOld.toFloat(),
            degree.toFloat(),
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 3600
            fillAfter = true
            interpolator = DecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    if(currentNumber(360-(degree%360))=="Pierde Turno"){
                        val siguienteJugador = gestorTurnos.siguienteTurno()
                        editor(siguienteJugador,sharedPreferences)
                        builder.dialog("PERDIDA DE TURNO","Has perdido turno, Turno del siguiente jugador, a jugar !","Vale",this@MainActivity2)
                    }else if(currentNumber(360-(degree%360))=="Quiebra"){
                        quiebras(jugadorActual)
                        val siguienteJugador = gestorTurnos.siguienteTurno()
                        editor(siguienteJugador,sharedPreferences)
                        builder.dialog("Quebraste","Quebraste, tu puntuacion se ha reducido a 0 y pierdes turno. Turno del siguiente jugador, a jugar !","Vale",this@MainActivity2)
                    }else{
                        var miIntent = Intent(this@MainActivity2, MainActivity3::class.java)
                        miIntent.putExtra("multiplicador", currentNumber(360-(degree%360)).toInt())
                        if (miIntent.resolveActivity(packageManager) != null){
                            startActivity(miIntent)
                        }
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
        }
        mibinding.ruleta.startAnimation(rotate)
    }

    private fun currentNumber(degrees: Int): String {
        return when {
            degrees >= (factor * 1) && degrees < (factor * 3) -> "60"
            degrees >= (factor * 3) && degrees < (factor * 5) -> "Quiebra"
            degrees >= (factor * 5) && degrees < (factor * 7) -> "100"
            degrees >= (factor * 7) && degrees < (factor * 9) -> "Pierde Turno"
            degrees >= (factor * 9) && degrees < (factor * 11) -> "80"
            degrees >= (factor * 11) && degrees < (factor * 13) -> "10"
            degrees >= (factor * 13) && degrees < (factor * 15) -> "60"
            degrees >= (factor * 15) && degrees < (factor * 17) -> "20"
            degrees >= (factor * 17) && degrees < (factor * 19) -> "90"
            degrees >= (factor * 19) && degrees < (factor * 21) -> "Quiebra"
            degrees >= (factor * 21) && degrees < (factor * 23) -> "100"
            degrees >= (factor * 23) && degrees < (factor * 25) -> "Pierde Turno"
            degrees >= (factor * 25) && degrees < (factor * 27) -> "70"
            degrees >= (factor * 27) && degrees < (factor * 29) -> "20"
            degrees >= (factor * 29) && degrees < (factor * 31) -> "30"
            degrees >= (factor * 31) && degrees < 360 || degrees >= 0 && degrees < (factor * 1) -> "40"
            else -> ""
        }
    }
    @SuppressLint("SetTextI18n")
    fun quiebras(jugadorActual:Int){
        jugadores.puntuaciones[jugadorActual-1]=0
        jugadores.guardarPuntuaciones(sharedPreferences)
        when (jugadorActual) {
            1 -> mibinding.puntos1.text=jugadores.puntuaciones[0].toString()
            2 -> mibinding.puntos2.text=jugadores.puntuaciones[1].toString()
            3 -> mibinding.puntos3.text=jugadores.puntuaciones[2].toString()
            4 -> mibinding.puntos4.text=jugadores.puntuaciones[3].toString()
        }
    }
    fun editor(siguienteJugador:Int,sharedPreferences: SharedPreferences){
        val editor = sharedPreferences.edit()
        editor.putInt("jugadorActual", siguienteJugador)
        editor.apply()
    }
}