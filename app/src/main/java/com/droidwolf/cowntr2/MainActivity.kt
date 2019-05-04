package com.droidwolf.cowntr2

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : BlankFragment.OnFragmentInteractionListener, AppCompatActivity() {
    override fun onFragmentInteraction(uri: Uri) {}

    private val db = FirebaseFirestore.getInstance()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_view, BlankFragment.newInstance(), "cowCount").commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.findFragmentByTag("cowCount")?.let {
                    supportFragmentManager.beginTransaction().remove(it).commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.findFragmentByTag("cowCount")?.let {
                    supportFragmentManager.beginTransaction().remove(it).commit()
                }
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
        navView.selectedItemId = R.id.navigation_home
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
        val newCount = Integer.parseInt(text) + count
        saveCurrentCount(newCount)
        return newCount
    }

    private fun saveCurrentCount(newCount: Int) {
        val cowData = hashMapOf("count" to newCount)
        db.collection(USERS_KEY).document(USER_KEY).set(cowData).addOnFailureListener { Log.e("APP", "....failed....") }
    }

    companion object {
        const val USER_KEY = "KING"
        const val USERS_KEY = "users"
    }
}
