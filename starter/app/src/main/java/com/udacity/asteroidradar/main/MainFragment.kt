package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid

class MainFragment : Fragment() {

    private var mainAsteroidsAdapter: MainAsteroidsAdapter? = null
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        mainAsteroidsAdapter = MainAsteroidsAdapter(AsteroidClickListener {
            this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        binding.asteroidRecycler.adapter = mainAsteroidsAdapter

        // Removed this options menu because it was not part of the screens in the task description
        //setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.asteroidsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNullOrEmpty()) {
                    viewModel.performNetworkRequest()
                } else {
                    mainAsteroidsAdapter?.submitList(it)
                }
            }
        })

        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            Picasso.get().load(it.url).into(binding.activityMainImageOfTheDay)
            binding.activityMainImageOfTheDay.contentDescription = it.title
        })

        viewModel.loadImageOfTheDay()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
