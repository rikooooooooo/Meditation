package com.example.meditation

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val storedUsername = getStoredUsername()

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home -> {
                    loadFragment(home())
                    true
                }
                R.id.learn -> {
                    loadFragment(learn())
                    true
                }
                R.id.music -> {
                    loadFragment(music())
                    true
                }
                R.id.user -> {
                    loadFragment(user())
                    true
                }
                else -> false
            }
        }
        loadFragment(home())
    }

    private  fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }

    fun setSelectedBottomNavItem(itemId: Int) {
        bottomNavigationView.selectedItemId = itemId
    }

    private fun getStoredUsername(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }

}