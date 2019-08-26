package com.gene.glayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gene.glayer.R
import com.gene.glayer.databinding.ActivityMainBinding
import com.gene.glayer.ui.all.AllMediaFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        contentView.lifecycleOwner = this
        GlayerClient.connect()
        contentView.viewPager.adapter = object :
            FragmentStateAdapter(this) {
            val fragment by lazy { AllMediaFragment() }
            override fun getItemCount(): Int {
                return 1
            }

            override fun createFragment(position: Int): Fragment {
                return fragment
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

