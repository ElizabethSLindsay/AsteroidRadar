package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.udacity.asteroidradar.BaseApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidEntities
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.AsteroidViewModelFactory
import com.udacity.asteroidradar.main.MainViewModel

class DetailFragment : Fragment() {

    private val navigationArgs: DetailFragmentArgs by navArgs()

    private val viewModel: MainViewModel by activityViewModels {
        AsteroidViewModelFactory(
            (activity?.application as BaseApplication).database.asteroidDao()
        )
    }

    private lateinit var asteroidEntities: AsteroidEntities

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id

        viewModel.asteroidDetails(id).observe(viewLifecycleOwner) { value ->
            asteroidEntities = value
            bindAsteroidDetails(asteroidEntities)

        }
    }

    private fun bindAsteroidDetails(asteroidEntities: AsteroidEntities) {
        binding.apply {
            closeApproachDate.text = asteroidEntities.closeApproachDate
            absoluteMagnitude.text = String.format(getString(R.string.km_unit_format), asteroidEntities.absoluteMagnitude)
            estimatedDiameter.text = String.format(getString(R.string.km_unit_format), asteroidEntities.estimatedDiameter)
            relativeVelocity.text = String.format(getString(R.string.km_s_unit_format), asteroidEntities.relativeVelocity)
            distanceFromEarth.text = String.format(getString(R.string.km_unit_format), asteroidEntities.distanceFromEarth)
            if (asteroidEntities.isPotentiallyHazardous) {
                activityMainImageOfTheDay.setImageResource(R.drawable.asteroid_hazardous)
            } else {
                activityMainImageOfTheDay.setImageResource(R.drawable.asteroid_safe)

            }
        }
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
