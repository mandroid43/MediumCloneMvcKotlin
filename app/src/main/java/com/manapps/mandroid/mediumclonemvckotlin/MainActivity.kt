package com.manapps.mandroid.mediumclonemvckotlin


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.manapps.mandroid.mediumclonemvckotlin.Utils.SharedPref
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Utils
import com.manapps.mandroid.mediumclonemvckotlin.databinding.ActivityMainBinding
import com.manapps.mandroid.mediumclonemvckotlin.home.GlobalPostsFragment
import com.manapps.mandroid.mediumclonemvckotlin.home.MyPostsFragment
import com.manapps.mandroid.mediumclonemvckotlin.profile.ProfileActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        setUpToolBar()
        setUpViewPager()
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.toolBar)
    }

    private fun setUpViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(GlobalPostsFragment(), "Global Posts")
        adapter.addFragment(MyPostsFragment(), "My Posts")
        binding.viewpager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    private fun initBindings() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    class ViewPagerAdapter
    constructor(supportFragmentManager: FragmentManager) :
        FragmentPagerAdapter(supportFragmentManager) {

        private var fragmentList1: ArrayList<Fragment> = ArrayList()
        private var fragmentTitleList1: ArrayList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList1[position]
        }

        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList1[position]
        }

        override fun getCount(): Int {
            return fragmentList1.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList1.add(fragment)
            fragmentTitleList1.add(title)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profileMenuItem -> Utils.moveTo(this, ProfileActivity::class.java)
            R.id.logoutMenuItem -> SharedPref.logoutSessionAndGoToLogin(this)
        }
        return super.onOptionsItemSelected(item)
    }

}