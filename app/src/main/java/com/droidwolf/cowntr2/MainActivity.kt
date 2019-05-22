package com.droidwolf.cowntr2

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.droidwolf.cowntr2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : CowCountFragment.OnFragmentInteractionListener, AppCompatActivity() {
    override fun onFragmentInteraction(uri: Uri) {}

    private lateinit var binding: ActivityMainBinding

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_count -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_view, CowCountFragment.newInstance(), "cowCount").commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            navView.selectedItemId = R.id.navigation_count
        }
    }

}
