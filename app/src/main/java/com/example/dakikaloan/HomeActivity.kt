package com.example.dakikaloan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.toolbar)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar)

        // Set up the navigation drawer
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation menu item clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_item1 -> {
                    val intent = Intent(this, ApplyNowActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_item2 -> {
                    // Handle item 2 click
                }
                R.id.nav_item3 -> {
                    // Handle item 3 click
                }
                R.id.nav_item4 -> {
                    // Handle item 4 click
                }
                R.id.nav_item5 -> {
                    // Handle item 5 click
                }
                R.id.nav_item6 -> {
                    // Handle item 6 click
                }
                R.id.nav_item7 -> {
                    // Handle item 7 (logout) click
                    val logoutIntent = Intent(this, LoginActivity::class.java)
                    startActivity(logoutIntent)
                    finish()
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime > 2000) {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
            backPressedTime = currentTime
        } else {
            super.onBackPressed()
        }
    }
}
