package com.mainp.musicapp.presentation.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mainp.musicapp.R
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.repository.SongRepositoryApiImpl
import com.mainp.musicapp.presentation.ui.fragment.ExploreFragment
import com.mainp.musicapp.presentation.ui.fragment.FavoriteSongFragment
import com.mainp.musicapp.presentation.ui.fragment.LanguageFragment
import com.mainp.musicapp.presentation.ui.fragment.MyMusicFragment
import com.mainp.musicapp.databinding.ActivityHomeBinding
import com.mainp.musicapp.presentation.viewmodel.SongViewModel
import com.mainp.musicapp.presentation.viewmodel.SongViewModelFactory

@androidx.media3.common.util.UnstableApi


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val headerView = binding.navView.getHeaderView(0)
        val ivClose = headerView.findViewById<ImageView>(R.id.ivClose)

        ivClose.setOnClickListener {
            binding.main.closeDrawer(GravityCompat.START)
        }

        drawerLayout = binding.main
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ExploreFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_explore)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_explore -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ExploreFragment()).commit()
            R.id.nav_my_music -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MyMusicFragment()).commit()
            R.id.nav_favorite_song -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FavoriteSongFragment()).commit()
            R.id.nav_language -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LanguageFragment()).commit()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}