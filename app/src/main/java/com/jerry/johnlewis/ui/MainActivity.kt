package com.jerry.johnlewis.ui

import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.jerry.johnlewis.R
import com.jerry.johnlewis.databinding.ActivityMainBinding
import com.jerry.johnlewis.databinding.FragmentProductListBinding

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customerToolbar.toolbarSupport)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    //click "<" on app bar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, AppBarConfiguration(navController.graph))
    }

    open fun showTitlabarBackIcon(show: Boolean){
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    open fun setTitleBarTile(str: String){
        binding?.customerToolbar.tvTitleBarTitle.text = str
    }

    open fun showLoading(show: Boolean){
        binding?.let{
            if (show)
                it.loadingCircularProgressIndicator.visibility = View.VISIBLE
            else
                it.loadingCircularProgressIndicator.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}