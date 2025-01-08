package com.example.ruletasuerte_isaacalejandro



class Paneles {
    data class Panel(val frase: String, val pista: String)
    val listaPaneles: ArrayList<Panel> = arrayListOf(
        Panel("NO POR MUCHO MADRUGAR AMANECE MAS TEMPRANO", "No te apresures, todo llega a su tiempo"),
        Panel("A CABALLO REGALADO NO SE LE MIRA EL DIENTE", "Agradece lo que recibes sin criticarlo"),
        Panel("CRIA CUERVOS Y TE SACARAN LOS OJOS", "Ten cuidado con en quién confías"),
        Panel("Y AL OTRO NO PUEDES VIVIR SIN TUS GAFAS DE PRESBICIA", "Un día eres joven..."),
        Panel("ricky rubio pau ribas y quino colom", "Tres de baloncesto"),
        Panel("LOS PANDA GIGANTES SE ALIMENTAN PRINCIPALMENTE DE BAMBU", "Viven en China"),
        Panel("la cancion imagine de john lennon es un himno a la paz", "Un clásico de los Beatles"),
    )
    var indice=-1
    fun obtener_Panel():String{
        indice=listaPaneles.indices.random()
        return listaPaneles[indice].frase
    }
    fun obtener_Pista():String{
        return listaPaneles[indice].pista
    }


}