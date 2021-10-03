package com.proxymitylab.demo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.proxymitylab.demo.databinding.ActivityMainBinding
import com.proxymitylab.demo.fragment.AqiListFragment
import com.proxymitylab.demo.fragment.LiveTrackingGraphCityFragment
import com.proxymitylab.demo.interfaces.LaunchFragmentInterface

class MainActivity : AppCompatActivity(), LaunchFragmentInterface {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
        launchFragment(fragment = AqiListFragment())
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.aqi_check)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun launchFragment(bundle: Bundle?, fragment: Fragment?) {
        val fragment1 = supportFragmentManager.findFragmentByTag(fragment?.javaClass?.simpleName)
        if(fragment is LiveTrackingGraphCityFragment){
            supportActionBar?.title = getString(R.string.aqi_detail)
        }
        if (fragment1 == null) {
            fragment?.arguments = bundle
            fragment?.let { it1 ->
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, it1, it1.javaClass.simpleName)
                    .addToBackStack(it1.javaClass.simpleName)
                    .commit()
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
            supportActionBar?.title = getString(R.string.aqi_check)
        }
    }


}
