package com.droidwolf.cowntr2

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : CowCountFragment.OnFragmentInteractionListener, ChatFragment.OnFragmentInteractionListener,
    AppCompatActivity() {
    override fun onFragmentInteraction(uri: Uri) {}

    private val db = FirebaseFirestore.getInstance()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_count -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_view, CowCountFragment.newInstance(), "cowCount").commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_view, ChatFragment.newInstance(), "chat")
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.findFragmentByTag("cowCount")?.let {
                    supportFragmentManager.beginTransaction().remove(it).commit()
                }
                supportFragmentManager.findFragmentByTag("chat")?.let {
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
        navView.selectedItemId = R.id.navigation_count
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

    override fun sendChat(view: View) {
        val chatTextView = findViewById<TextView>(R.id.chatTextView)
        val chatEditText = findViewById<EditText>(R.id.editText)
        val text = chatTextView.text.toString()
        val currentChat = "$text\n$USER_KEY:\t${chatEditText.text}"
        chatTextView.text = currentChat
        val chatData = hashMapOf("chat" to currentChat)
        db.collection(USERS_KEY).document(USER_KEY).collection(USER_DATA).document(CHAT_DOC).set(chatData)
            .addOnFailureListener { Log.e("APP", "....failed....") }
        chatEditText.text.clear()
    }

    private fun updateCount(text: String, count: Int): Int {
        val newCount = Integer.parseInt(text) + count
        saveCurrentCount(newCount)
        return newCount
    }

    private fun saveCurrentCount(newCount: Int) {
        val cowData = hashMapOf("count" to newCount)
        db.collection(USERS_KEY).document(USER_KEY).collection(COUNT_DOC).document(COUNT_DATA).set(cowData)
            .addOnFailureListener { Log.e("APP", "....failed....") }
    }

    companion object {
        const val USER_KEY = "KING"
        const val USERS_KEY = "users"
        const val USER_DATA = "USER_DATA"
        const val CHAT_DOC = "CHAT"
        const val CHAT_DATA = "chatData"
        const val COUNT_DOC = "COUNT"
        const val COUNT_DATA = "countData"
    }
}
