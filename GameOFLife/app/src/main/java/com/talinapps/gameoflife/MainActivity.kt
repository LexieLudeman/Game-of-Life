package com.talinapps.gameoflife

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var mapper: Mapper
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        mapper = findViewById(R.id.mapper)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            when (button.text) {
                getText(R.string.start_button_label) -> {
                    mapper.start()
                    button.text = getText(R.string.pause_button_label)
                }
                getText(R.string.pause_button_label) -> {
                    mapper.stop()
                    button.text = getText(R.string.resume_button_label)
                }
                else -> {
                    mapper.start()
                    button.text = getText(R.string.pause_button_label)
                }
            }
        }

    }

}
