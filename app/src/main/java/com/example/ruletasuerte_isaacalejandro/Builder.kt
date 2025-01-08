package com.example.ruletasuerte_isaacalejandro

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog


class Builder{

    fun dialog(titulo:String,mensaje:String,mensajeboton:String,context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton(mensajeboton) { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
    }
    fun dialogIntent(titulo:String,mensaje:String,mensajeboton:String,context: Context,targetActivity: Class<out Activity>){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton(mensajeboton) { dialog, which ->
            val intent = Intent(context, targetActivity)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}