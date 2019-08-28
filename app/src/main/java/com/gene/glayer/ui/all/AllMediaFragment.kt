package com.gene.glayer.ui.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.gene.libglayer.APP
import com.gene.glayer.ui.GlayerClient

class AllMediaFragment : Fragment() {
    private lateinit var adapter: AllMediaAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecyclerView(com.gene.libglayer.APP).apply {
        layoutParams = ViewGroup.LayoutParams(-1, -1)
        layoutManager = LinearLayoutManager(context)
        this@AllMediaFragment.adapter = AllMediaAdapter()
        adapter = this@AllMediaFragment.adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlayerClient.mediaList.observe(this, Observer {
            adapter.submitList(it.list)
        })
    }
}

