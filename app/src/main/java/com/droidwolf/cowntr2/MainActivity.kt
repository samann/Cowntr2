package com.droidwolf.cowntr2

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    fun addOneCow(view: View) {
        val cowCountTextView = findViewById<TextView>(R.id.cow_count_textView)
        cowCountTextView.text = "${updateCount(cowCountTextView.text.toString(), 1)}"
    }

    fun addFieldCow(view: View) {
        val cowCountTextView = findViewById<TextView>(R.id.cow_count_textView)
        cowCountTextView.text = "${updateCount(cowCountTextView.text.toString(), 30)}"
    }

    fun graveyard(view: View) {
        val cowCountTextView = findViewById<TextView>(R.id.cow_count_textView)
        cowCountTextView.text = "0"
    }

    private fun updateCount(text: String, count: Int): Int {
        return Integer.parseInt(text) + count;
    }
}
