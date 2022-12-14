@file:Suppress("OverrideDeprecatedMigration", "OverrideDeprecatedMigration")

package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

@RequiresApi(Build.VERSION_CODES.N)
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)
        val adapter = AsteroidAdapter(AsteroidClickListener { asteroid ->
            viewModel.navigateToDetailFragment.value=asteroid
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, Observer { asteroid->
            if(asteroid!=null){
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.navigateToDetailFragment.value=null
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onChangeFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> AsteroidTime.TODAY
                R.id.show_all_menu -> AsteroidTime.WEEK
                else -> AsteroidTime.ALL
            }
        )
        return true
    }
}
