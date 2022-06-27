package com.normurodov_nazar.noteprayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    val textView: TextView = TODO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.t)
        val button = findViewById<Button>(R.id.b)
        button.setOnClickListener{
            textView.text = "asdxzc"
            Toast.makeText(this,"aaa",Toast.LENGTH_LONG).show()
        }
    }
}