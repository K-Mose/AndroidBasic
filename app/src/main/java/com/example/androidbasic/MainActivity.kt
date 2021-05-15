package com.example.androidbasic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidbasic.canvas.CanvasActivity
import com.example.androidbasic.coroutines.CoroutineNetworkRequest
import com.example.androidbasic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnCanvas.setOnClickListener {
                startActivity(Intent(this@MainActivity, CanvasActivity::class.java))
            }
            btnCoroutine.apply{
                startActivity(Intent(this@MainActivity, CoroutineNetworkRequest::class.java))
            }
        }
    }
}