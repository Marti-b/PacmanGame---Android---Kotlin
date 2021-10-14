package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import org.pondar.pacmankotlin.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    //reference to the game class.
    private lateinit var game: Game
    private lateinit var binding: ActivityMainBinding

    //Timer
    private var myTimer: Timer = Timer()
    var counter: Int = 0

    private var clockTimer = Timer()
    //var timeCounter: Int = 60



    /*private var running = false
    var direction = RIGHT*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //makes sure it always runs in portrait mode - will cost a warning
        //but this is want we want!
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Log.d("onCreate", "Oncreate called")

        game = Game(this, binding.pointsView)

        //intialize the game view clas and game class
        game.setGameView(binding.gameView)
        binding.gameView.setGame(game)
        game.newGame()


        //Setting up a new Timer
        game.running = true
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }
        }, 0, 200) //0 indicates we start now, 200
        //is the number of miliseconds between each call

        clockTimer.schedule(object : TimerTask(){
            override fun run() {
                clockTimerMethod()
                if(game.running){
                    game.timeLeft--
                    game.timerCheck(game.timeLeft)
                }
            }
        }, 0, 1000)

        //Functional buttons
        binding.startButton.setOnClickListener{
            game.running = true
        }
        binding.stopButton.setOnClickListener{
            game.running = false
        }
        binding.resetButton.setOnClickListener{
            game.timeLeft = 60
            game.newGame()
            game.running = false
            binding.timerView.text = getString(R.string.timerValue,game.timeLeft)
        }

        // Changing the direction going via buttons
        // By clicking the direction buttons the game continues
        binding.moveRight.setOnClickListener {
            game.running = true
            game.direction= game.RIGHT
        }
        binding.moveLeft.setOnClickListener {
            game.running = true
            game.direction = game.LEFT
        }
        binding.moveUp.setOnClickListener {
            game.running = true
            game.direction = game.UP
        }
        binding.moveDown.setOnClickListener {
            game.running = true
            game.direction = game.DOWN
        }
    }

    override fun onStop() {
        super.onStop()
        //just to make sure if the app is killed, that we stop the timer.
        //myTimer.cancel()
        //clockTimer.cancel()
    }

    private fun timerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer - i.e the background

        //We call the method that will work with the UI
        //through the runOnUiThread method.

        this.runOnUiThread(timerTick)
    }
    private fun clockTimerMethod() {
        this.runOnUiThread(timerTick)
    }

    private val timerTick = Runnable {
        if (game.running) {
            counter++

            binding.timerView.text = getString(R.string.timerValue, game.timeLeft)

            game.moveEnemyUpAndDown(game.enemySpeeed)

            if (game.direction == game.RIGHT) {
                game.movePacmanRight(60)
            }
            if (game.direction == game.LEFT) {
                game.movePacmanLeft(60)
            }
            if (game.direction == game.UP) {
                game.movePacmanUp(60)
            }
            if (game.direction == game.DOWN) {
                game.movePacmanDown(60)
            }
        }
    }

    /*override fun onClick(v: View) {
        if (v.id == R.id.startButton) {
            game.running = true
        } else if (v.id == R.id.stopButton) {
            game.running = false
        } else if (v.id == R.id.resetButton) {
            counter = 0
            game.newGame() //you should call the newGame method instead of this
            game.running = false
            binding.timerView.text = getString(R.string.timerValue,counter)
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            counter = 0
            game.timeLeft = 60
            game.newGame()

            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
