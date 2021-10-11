package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import java.lang.Math.pow
import java.util.*
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextInt


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView) {

        private var pointsView: TextView = view
        private var points : Int = 0
        //bitmap of the pacman
        var pacBitmap: Bitmap
        var pacx: Int = 0
        var pacy: Int = 0

        //Creating the coins
        val random = Random()
        var coinBitmap : Bitmap
        var coinx = 0
        var coiny = 0

        /*val UP: Int = 1;
        val DOWN: Int = -1;
        val LEFT: Int = 1;
        val RIGHT: Int = 1;*/

        var running = false
        var direction = 4
        val UP: Int = 1
        val DOWN: Int = 2
        val LEFT: Int = 3
        val RIGHT: Int = 4

        var level = 1;

        //did we initialize the coins?
        var coinsInitialized = false

        //the list of goldcoins - initially empty
        var coins = ArrayList<GoldCoin>(5)




        //a reference to the gameview
        private lateinit var gameView: GameView
        private var h: Int = 0
        private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.coin)

    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins()
    {
        //Emptying the array by overwriting with a new empty array
        var coinsNew = ArrayList<GoldCoin>()
        coins = coinsNew

        val coin1 = GoldCoin(kotlin.random.Random.nextInt(1000),kotlin.random.Random.nextInt(1000),false)
        val coin2 = GoldCoin(kotlin.random.Random.nextInt(1000),kotlin.random.Random.nextInt(1000), false)
        val coin3 = GoldCoin(kotlin.random.Random.nextInt(1000),kotlin.random.Random.nextInt(1000), false)
        val coin4 = GoldCoin(kotlin.random.Random.nextInt(1000),kotlin.random.Random.nextInt(1000), false)
        val coin5 = GoldCoin(kotlin.random.Random.nextInt(1000),kotlin.random.Random.nextInt(1000), false)

        //DO Stuff to initialize the array list with some coins.
        coins.add(coin1)
        coins.add(coin2)
        coins.add(coin3)
        coins.add(coin4)
        coins.add(coin5)

        coinsInitialized = true
    }


    fun newGame() {
        pacx = 50
        pacy = 400 //just some starting coordinates - you can change this.
        //reset the points

        coinsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"

        coinx = kotlin.random.Random.nextInt(1000)
        coiny = kotlin.random.Random.nextInt(1000)

        initializeGoldcoins()



        gameView.invalidate() //redraw screen
    }
    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            doCollisionCheck()
            gameView.invalidate()
        }
    }
    fun movePacmanLeft(pixels: Int){
        if(pacx - pixels > 0){
            pacx = pacx - pixels
            doCollisionCheck()
            gameView.invalidate()
        }
    }
    fun movePacmanUp(pixels: Int){
        if (pacy - pixels > 0){
            pacy = pacy - pixels
            doCollisionCheck()
            gameView.invalidate()
        }
    }
    fun movePacmanDown(pixels: Int){
        if (pacy + pixels + pacBitmap.height <h){
            pacy = pacy + pixels
            doCollisionCheck()
            gameView.invalidate()
        }
    }
    fun distance(x1:Int,y1:Int,x2:Int,y2:Int) : Float {
        // calculate distance and return it
        var xDistance = (x2-x1).toDouble()
        var yDistance = (y2-y1).toDouble()

        var distance = sqrt((xDistance * xDistance) + (yDistance * yDistance))

        return distance.toFloat();
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {

        for (coin in coins){

            if(!coin.taken){
                if (distance(pacx,pacy,coin.coinx,coin.coiny)< 150) {
                    Log.d("Mission complished", "Mission complished")
                    coin.taken = true
                    points++
                    Log.d("Points: ",points.toString())
                    pointsView.text = "${context.resources.getString(R.string.points)} $points"
                }
            }
        }
        if (coins.size == points){
            running = false
            Toast.makeText(context, "You won", Toast.LENGTH_LONG).show()
        }
    }
    fun timerCheck(counter : Int){
        if (counter == 0 && coins.size != points){
            running = false
            Toast.makeText(context, "Time is up. You lost. Start a new game", Toast.LENGTH_LONG).show()
        } else if(counter >= 0 && coins.size == points){
            running = false
            Toast.makeText(context, "You won", Toast.LENGTH_LONG).show()
        }
    }


}