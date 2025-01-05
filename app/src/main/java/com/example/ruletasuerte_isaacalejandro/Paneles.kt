package com.example.ruletasuerte_isaacalejandro



class Paneles {
    data class Panel(val frase: String, val pista: String)
    val listaPaneles: ArrayList<Panel> = arrayListOf(
        Panel("no por mucho madrugar amanece mas temprano", "No te apresures, todo llega a su tiempo"),
        Panel("a caballo regalado no se le mira el diente", "Agradece lo que recibes sin criticarlo"),
        Panel("cria cuervos y te sacarán los ojos", "Ten cuidado con en quién confías")
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