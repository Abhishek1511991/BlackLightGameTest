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

//https://stackoverflow.com/questions/34360883/change-the-colors-of-different-buttons-randomly-at-a-specified-time
class MainActivity : AppCompatActivity() {

     var scoreValue:Int =0
     lateinit var changingButtons: Array<TextView?>
     var timer:Timer?=null
     var isClicked:Boolean=false
     var previousPosition=0;


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
                checkViewBackgroudColor(split1)
        }

        split2?.setOnClickListener {
                checkViewBackgroudColor(split2)
        }

        split3?.setOnClickListener {
                checkViewBackgroudColor(split3)
        }

        split4?.setOnClickListener {
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

            retryColorChange(randomViewPos,color)

        }


        fun retryColorChange(randomViewPos:Int,color:Int)
        {
            if(randomViewPos==activity.previousPosition)
            {
                val randomViewPos1 = Random().nextInt(4)
                var color1 = Color.TRANSPARENT
                val background = changingButtons[randomViewPos1]?.background
                if (background is ColorDrawable)
                    color1 = background.color

                retryColorChange(randomViewPos1,color1)
            }
            else
            {
                activity.previousPosition=randomViewPos
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
        else if(isClicked && color==Color.parseColor("#C0C0C0") ){

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


    fun exitAlert()
    {
        timer?.purge()
        timer?.cancel()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Currently game is running, Are you want to exit game or play continue?")
        builder.setMessage("Your score is: $scoreValue")

        builder.setPositiveButton("Resume") { dialog, which ->
            setScoreText(scoreValue)
            startGame()
            Toast.makeText(this,"Game Resume",Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Exit") { dialog, which ->
            Toast.makeText(this.applicationContext,"Thanks for playing game",Toast.LENGTH_SHORT).show()
            finish()
        }
        val dialog=builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitAlert()
    }


}