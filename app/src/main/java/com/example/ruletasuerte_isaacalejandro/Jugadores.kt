package com.example.ruletasuerte_isaacalejandro

import android.content.SharedPreferences

class Jugadores {
    var puntuaciones = MutableList(4) { 0 }

    // Cargar las puntuaciones desde SharedPreferences
    fun cargarPuntuaciones(sharedPreferences: SharedPreferences) {
        // Verificamos si los valores están siendo correctamente cargados desde SharedPreferences
        for (i in 0 until 4) {
            puntuaciones[i] = sharedPreferences.getInt("puntuacionJugador${i + 1}", 0)
        }
    }

    // Guardar las puntuaciones en SharedPreferences
    fun guardarPuntuaciones(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        for (i in 0 until 4) {
            editor.putInt("puntuacionJugador${i + 1}", puntuaciones[i])
        }
        editor.apply()
    }
}

