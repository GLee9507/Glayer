package com.gene.glayer.ui.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.*
import com.gene.glayer.ui.MainViewModel
import com.gene.libglayer.APP

class AllMediaFragment : Fragment() {
    private lateinit var adapter: AllMediaAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecyclerView(APP).apply {
        layoutParams = ViewGroup.LayoutParams(-1, -1)
        layoutManager = LinearLayoutManager(context)
        this@AllMediaFragment.adapter = AllMediaAdapter()
        adapter = this@AllMediaFragment.adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider(
            activity as ViewModelStoreOwner, ViewModelProvider.AndroidViewModelFactory.getInstance(
                APP
            )
        ).get(MainViewModel::class.java).mediaList
            .observe(this, Observer {
                adapter.submitList(it.list)
            })
    }
}

