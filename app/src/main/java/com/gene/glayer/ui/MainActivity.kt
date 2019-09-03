package com.gene.glayer.ui

import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gene.glayer.R
import com.gene.glayer.databinding.ActivityMainBinding
import com.gene.glayer.ui.all.AllMediaFragment
import com.gene.libglayer.APP

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        contentView.lifecycleOwner = this
        val viewModel: MainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(APP)
        )[MainViewModel::class.java]
        contentView.viewModel = viewModel
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
        viewModel.mediaList.observe(this, Observer {
            viewModel.presenter.setPlayList()
        })
        var onTouch = false
        viewModel.playProgress.observe(this, Observer {
            if (onTouch) {
                return@Observer
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentView.seekBar.setProgress(it, true)
            } else {
                contentView.seekBar.progress = it
            }
        })
        contentView.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                onTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                onTouch = false
                viewModel.presenter.seek(seekBar.progress)
            }
        })
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

