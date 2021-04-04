package com.example.whatsapp

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




         setSupportActionBar(toolbar)
         viewPager.adapter = ScreenSliderAdapter(this)
         TabLayoutMediator(tabs,viewPager,TabLayoutMediator.TabConfigurationStrategy{ tab: TabLayout.Tab, i: Int ->
                when(i){
                    0 -> tab.text = "CHATS"
                    1 -> tab.text = "PEOPLE"
                }
         }).attach()
    }
}