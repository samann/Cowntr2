package com.droidwolf.cowntr2

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var loaded = false

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
        val cowCountTextView = findViewById<TextView>(R.id.cow_count_textView)
        cowCountTextView.setText(R.string.loading_count)
        db.collection(USERS_KEY).document(USER_KEY).get().addOnCompleteListener { it ->
            it.result?.let {
                cowCountTextView.text = "${it["count"]}"
                loaded = true
            }
        }
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
        saveCurrentCount(0)
        cowCountTextView.text = "0"
    }

    private fun updateCount(text: String, count: Int): Int {
        if (!loaded) {
            return 0
        }
        val newCount = Integer.parseInt(text) + count
        saveCurrentCount(newCount)
        return newCount
    }

    private fun saveCurrentCount(newCount: Int) {
        val cowData = hashMapOf("count" to newCount)
        db.collection(USERS_KEY).document(USER_KEY).set(cowData)
    }

    companion object {
        const val USER_KEY = "KING"
        const val USERS_KEY = "users"
    }
}
