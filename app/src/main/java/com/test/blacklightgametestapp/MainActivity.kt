package com.test.blacklightgametestapp



import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

     var scoreValue:Int =0
     lateinit var changingButtons: Array<TextView?>
     var timer:Timer?=null
     var isClicked:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setScoreText(scoreValue)
        changingButtons = arrayOfNulls(4)
        changingButtons[0] = split1
        changingButtons[1] = split2
        changingButtons[2] = split3
        changingButtons[3] = split4

        split1?.setOnClickListener {
            if(!isClicked)
                checkViewBackgroudColor(split1)
        }

        split2?.setOnClickListener {
            if(!isClicked)
                checkViewBackgroudColor(split2)
        }

        split3?.setOnClickListener {
            if(!isClicked)
                checkViewBackgroudColor(split3)
        }

        split4?.setOnClickListener {
            if(!isClicked)
                checkViewBackgroudColor(split4)
        }

        startGame();
    }


    fun setScoreText(score: Int?)
    {
        scopecard?.text= "Score $score"
    }

    fun startGame(){
        timer = Timer()
        timer?.scheduleAtFixedRate(MyTask(changingButtons, this), 1000,2000)
    }

    class MyTask(
        private val changingButtons: Array<TextView?>,
        private val activity: MainActivity
    ) : TimerTask() {
        override fun run() {
            val randomViewPos = Random().nextInt(4)
            var color = Color.TRANSPARENT
            val background = changingButtons[randomViewPos]?.background
            if (background is ColorDrawable)
                color = background.color


            changingButtons[randomViewPos]?.setBackgroundColor(Color.parseColor("#C0C0C0"))
            changingButtons[randomViewPos]?.postDelayed(object:Runnable
            {
                override fun run() {
                    changingButtons[randomViewPos]?.setBackgroundColor(color)
                    if(!activity.isClicked)
                        activity.gameOverAlert()
                    else
                        activity.isClicked=false
                }

            },1000)
        }

    }


    fun checkViewBackgroudColor(view: View)
    {
        var color = Color.TRANSPARENT
        val background = view.background
        if (background is ColorDrawable)
            color = background.color

        if(!isClicked && color==Color.parseColor("#C0C0C0"))
        {
            isClicked=true
            setScoreText(++scoreValue)
        }
        else if(isClicked){

        }
        else {
            gameOverAlert()
        }
    }

    fun gameOverAlert()
    {
        timer?.purge()
        timer?.cancel()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over")
        builder.setMessage("Your score is: $scoreValue")

        builder.setPositiveButton("Restart") { dialog, which ->
            scoreValue=0
            setScoreText(scoreValue)
            startGame()
            Toast.makeText(this,"Game Start",Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Exit") { dialog, which ->
            Toast.makeText(this.applicationContext,"Thanks to play game",Toast.LENGTH_SHORT).show()
            finish()
        }
        val dialog=builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

    }


}