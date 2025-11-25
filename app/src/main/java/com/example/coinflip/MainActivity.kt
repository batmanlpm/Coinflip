package com.example.coinflip

import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var coinImage: ImageView
    private lateinit var resultText: TextView
    private lateinit var flipButton: Button
    private lateinit var nsfwSwitch: SwitchCompat
    
    private var coinFlipSound: MediaPlayer? = null
    private var isAnimating = false
    private var isNsfwMode = false

    // Arrays of drawable resource IDs for NSFW mode
    private val nsfwHeadsImages = intArrayOf(
        R.drawable.nsfw_heads_1,
        R.drawable.nsfw_heads_2,
        R.drawable.nsfw_heads_3,
        R.drawable.nsfw_heads_4,
        R.drawable.nsfw_heads_5,
        R.drawable.nsfw_heads_6,
        R.drawable.nsfw_heads_7,
        R.drawable.nsfw_heads_8,
        R.drawable.nsfw_heads_9,
        R.drawable.nsfw_heads_10
    )

    private val nsfwTailsImages = intArrayOf(
        R.drawable.nsfw_tails_1,
        R.drawable.nsfw_tails_2,
        R.drawable.nsfw_tails_3,
        R.drawable.nsfw_tails_4,
        R.drawable.nsfw_tails_5,
        R.drawable.nsfw_tails_6,
        R.drawable.nsfw_tails_7,
        R.drawable.nsfw_tails_8,
        R.drawable.nsfw_tails_9,
        R.drawable.nsfw_tails_10
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coinImage = findViewById(R.id.coinImage)
        resultText = findViewById(R.id.resultText)
        flipButton = findViewById(R.id.flipButton)
        nsfwSwitch = findViewById(R.id.nsfwSwitch)

        // Load saved NSFW mode state
        val sharedPreferences = getSharedPreferences("coinflip_prefs", MODE_PRIVATE)
        isNsfwMode = sharedPreferences.getBoolean("nsfw_mode", false)
        nsfwSwitch.isChecked = isNsfwMode
        updateModeUI()

        flipButton.setOnClickListener {
            if (!isAnimating) {
                flipCoin()
            }
        }

        nsfwSwitch.setOnCheckedChangeListener { _, isChecked ->
            isNsfwMode = isChecked
            sharedPreferences.edit().putBoolean("nsfw_mode", isChecked).apply()
            updateModeUI()
            // Reset to default coin image when switching modes
            if (isNsfwMode) {
                coinImage.setImageResource(nsfwHeadsImages[Random.nextInt(nsfwHeadsImages.size)])
            } else {
                coinImage.setImageResource(R.drawable.coin_heads)
            }
        }
    }

    private fun updateModeUI() {
        if (isNsfwMode) {
            resultText.text = getString(R.string.nsfw_mode_active)
        } else {
            resultText.text = getString(R.string.tap_to_flip)
        }
    }

    private fun flipCoin() {
        isAnimating = true
        flipButton.isEnabled = false

        // Play coin flip sound
        playCoinFlipSound()

        // Determine the result
        val isHeads = Random.nextBoolean()

        // Load and start the flip animation
        val flipAnimation = AnimationUtils.loadAnimation(this, R.anim.coin_flip)
        
        flipAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                resultText.text = getString(R.string.flipping)
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Set the final result
                if (isNsfwMode) {
                    if (isHeads) {
                        val randomIndex = Random.nextInt(nsfwHeadsImages.size)
                        coinImage.setImageResource(nsfwHeadsImages[randomIndex])
                    } else {
                        val randomIndex = Random.nextInt(nsfwTailsImages.size)
                        coinImage.setImageResource(nsfwTailsImages[randomIndex])
                    }
                } else {
                    if (isHeads) {
                        coinImage.setImageResource(R.drawable.coin_heads)
                    } else {
                        coinImage.setImageResource(R.drawable.coin_tails)
                    }
                }
                resultText.text = if (isHeads) getString(R.string.heads) else getString(R.string.tails)
                isAnimating = false
                flipButton.isEnabled = true
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        coinImage.startAnimation(flipAnimation)
    }

    private fun playCoinFlipSound() {
        try {
            coinFlipSound?.release()
            coinFlipSound = MediaPlayer.create(this, R.raw.coin_flip_sound)
            coinFlipSound?.start()
            coinFlipSound?.setOnCompletionListener { mp ->
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coinFlipSound?.release()
        coinFlipSound = null
    }
}
