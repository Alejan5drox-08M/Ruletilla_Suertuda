package com.example.ruletasuerte_isaacalejandro

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMain2Binding
import com.example.ruletasuerte_isaacalejandro.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity2 : AppCompatActivity() {
    private var degree = 0
    private var degreeOld = 0
    private val factor:Float= 11.25f
    private lateinit var mibinding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mibinding=ActivityMain2Binding.inflate(layoutInflater)
        setContentView(mibinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mibinding.botonTirar.setOnClickListener {
            startRotation()
        }
    }
    private fun startRotation() {
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
                    var miIntent = Intent(this@MainActivity2, MainActivity3::class.java)
                    if (miIntent.resolveActivity(packageManager) != null){
                        startActivity(miIntent)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Acciones opcionales si se repite la animación
                }
            })
        }

        mibinding.ruleta.startAnimation(rotate)
    }

    private fun currentNumber(degrees: Int):String {
        var text= ""
        if(degrees>=(factor*1)&& degrees<(factor*3)){
            text="60"
        }
        if(degrees>=(factor*3)&& degrees<(factor*5)){
            text="Quiebra"
        }
        if(degrees>=(factor*5)&& degrees<(factor*7)){
            text="100"
        }
        if(degrees>=(factor*7)&& degrees<(factor*9)){
            text="Pierde Turno"
        }
        if(degrees>=(factor*9)&& degrees<(factor*11)){
            text="80"
        }
        if(degrees>=(factor*11)&& degrees<(factor*13)){
            text="10"
        }
        if(degrees>=(factor*13)&& degrees<(factor*15)){
            text="60"
        }
        if(degrees>=(factor*15)&& degrees<(factor*17)){
            text="20"
        }
        if(degrees>=(factor*17)&& degrees<(factor*19)){
            text="90"
        }
        if(degrees>=(factor*19)&& degrees<(factor*21)){
            text="Quiebra"
        }
        if(degrees>=(factor*21)&& degrees<(factor*23)){
            text="100"
        }
        if(degrees>=(factor*23)&& degrees<(factor*25)){
            text="Pierde Turno"
        }
        if(degrees>=(factor*25)&& degrees<(factor*27)){
            text="70"
        }
        if(degrees>=(factor*27)&& degrees<(factor*29)){
            text="20"
        }
        if(degrees>=(factor*29)&& degrees<(factor*31)){
            text="30"
        }
        if((degrees>=(factor*31)&& degrees<360)||(degrees>=0&&degrees<(factor*1))){
            text="40"
        }
        return text
    }
}