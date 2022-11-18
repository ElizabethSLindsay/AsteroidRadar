package com.udacity.asteroidradar.main


import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.BaseApplication
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels {
        AsteroidViewModelFactory(
            (activity?.application as BaseApplication).database.asteroidDao()
        )
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

//        binding.lifecycleOwner = this
//
//        binding.viewModel = viewModel



        Picasso.with(activity_main_image_of_the_day.context)
            .load("https://api.nasa.gov/planetary/apod?api_key=SNrG4C86m2Zxhx9b7HAAnOGdJQqB6BzYRLlTi0fp")
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(activity_main_image_of_the_day, object : Callback {
                override fun onSuccess() {
                    // No op
                }
                override fun onError() {
                    Picasso.with(activity_main_image_of_the_day.context)
                        .load("https://api.nasa.gov/planetary/apod?api_key=SNrG4C86m2Zxhx9b7HAAnOGdJQqB6BzYRLlTi0fp")
                        .placeholder(R.drawable.placeholder_picture_of_day)
                        .error(R.drawable.ic_connection_error)
                        .into(activity_main_image_of_the_day)
                }
            })



        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AsteroidAdapter {  asteroidEntities ->
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
                findNavController().navigate(action)

        }
        viewModel.allAsteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
