package com.example.ruletasuerte_isaacalejandro

import android.content.SharedPreferences

class GestorTurnos(private val totalJugadores: Int, private val sharedPreferences: SharedPreferences) {
    var contador: Int
        get() = sharedPreferences.getInt("turno", 1)  // Recuperar el turno guardado
        set(value) = sharedPreferences.edit().putInt("turno", value).apply()  // Guardar el nuevo turno

    /**
     * Obtiene el jugador actual y avanza al siguiente turno.
     */
    fun siguienteTurno(): Int {
        val jugadorActual = contador
        contador++
        if (contador > totalJugadores) {
            contador = 1
        }
        return jugadorActual
    }
}
