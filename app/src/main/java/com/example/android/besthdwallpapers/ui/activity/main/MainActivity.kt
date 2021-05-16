package com.example.android.besthdwallpapers.ui.activity.main

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.android.besthdwallpapers.R
import com.example.android.besthdwallpapers.databinding.ActivityMainBinding
import com.example.android.besthdwallpapers.ui.activity.GalleryActivity
import com.example.android.besthdwallpapers.ui.activity.SearchActivity
import com.example.android.besthdwallpapers.ui.activity.categories.CategoryActivity
import com.example.android.besthdwallpapers.ui.activity.favourites.FavouriteActivity
import com.example.android.besthdwallpapers.util.Constants.PIC_EXTRA
import com.example.android.besthdwallpapers.util.NetworkUtils.getNetworkLiveData
import com.example.android.besthdwallpapers.util.setupWithNavController
import com.example.android.besthdwallpapers.util.shortToast
import com.example.android.besthdwallpapers.util.startActivity
import com.example.android.besthdwallpapers.util.viewBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModel()
    private var currentNavController: LiveData<NavController>? = null
    private var backButtonPressedTwice = false
    private lateinit var reviewManager: ReviewManager
    private lateinit var reviewInfo: ReviewInfo

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) setupBottomNavigationBar()

        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.itemIconTintList = null

        reviewManager = ReviewManagerFactory.create(this)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reviewInfo = task.result
                viewModel.isRateDialogShow.observe(this) { show ->
                    if (show) showInAppReview()
                }
            }
        }

        getNetworkLiveData(applicationContext).observe(this) {
            binding.noInternetWarning.isVisible = !it
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> startActivity<SearchActivity>()
            R.id.action_gallery -> startActivity<CategoryActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.bmw, R.navigation.audi, R.navigation.mercedes)

        val controller = binding.bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        controller.observe(this, { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_aston -> startActivity<GalleryActivity> {
                putExtra(
                    PIC_EXTRA,
                    getString(R.string.aston)
                )
            }
            R.id.nav_bentley -> startActivity<GalleryActivity> {
                putExtra(
                    PIC_EXTRA,
                    getString(R.string.bentley)
                )
            }
            R.id.nav_bugatti -> startActivity<GalleryActivity> {
                putExtra(
                    PIC_EXTRA,
                    getString(R.string.bugatti)
                )
            }
            R.id.nav_ferrari -> startActivity<GalleryActivity> {
                putExtra(
                    PIC_EXTRA,
                    getString(R.string.ferrari)
                )
            }
            R.id.nav_lambo -> startActivity<GalleryActivity> {
                putExtra(
                    PIC_EXTRA,
                    getString(R.string.lambo_walp)
                )
            }
            R.id.nav_mclaren -> startActivity<GalleryActivity> {
                putExtra(
                    PIC_EXTRA,
                    getString(R.string.mclaren)
                )
            }
            R.id.nav_porsche -> startActivity<GalleryActivity> {
                putExtra(
                    PIC_EXTRA,
                    getString(R.string.porsche)
                )
            }
            R.id.nav_rolls -> startActivity<GalleryActivity> {
                putExtra(
                    PIC_EXTRA,
                    getString(R.string.rolls)
                )
            }
            R.id.nav_favorites -> startActivity<FavouriteActivity>()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> binding.drawerLayout.closeDrawer(
                GravityCompat.START
            )
            else -> {
                when (currentNavController?.value?.currentDestination?.label) {
                    getString(R.string.bmw) -> {
                        if (backButtonPressedTwice) {
                            super.onBackPressed()
                        } else {
                            backButtonPressedTwice = true
                            shortToast(getString(R.string.press_back))
                            lifecycleScope.launch {
                                delay(2000)
                                backButtonPressedTwice = false
                            }
                        }
                    }
                    else -> super.onBackPressed()
                }
            }
        }
    }

    private fun showInAppReview() {
        if (::reviewInfo.isInitialized) reviewManager.launchReviewFlow(this, reviewInfo)
    }
}