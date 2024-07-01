package com.example.smkituidemoapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smkituidemoapp.databinding.MainActivityBinding
import com.example.smkituidemoapp.viewModels.MainViewModel
import com.sency.smbase.core.listener.ConfigurationResult
import com.sency.smkitui.SMKitUI
import com.sency.smkitui.listener.SMKitUIWorkoutListener
import com.sency.smkitui.model.ExerciseData
import com.sency.smkitui.model.SMWorkout
import com.sency.smkitui.model.WorkoutSummaryData

class MainActivity : AppCompatActivity(), SMKitUIWorkoutListener {

    private var _binding: MainActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()

    private var smKitUI: SMKitUI? = null

    private val tag = this::class.java.simpleName

    private val apiPublicKey = ""

    private val configurationResult = object : ConfigurationResult {
        override fun onFailure() {
            viewModel.setConfigured(false)
            Log.d("Activity", "failed to configure")
        }

        override fun onSuccess() {
            viewModel.setConfigured(true)
            Log.d("Activity", "succeced to configure")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermmions()
        observeConfiguration()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.startAssessment.setOnClickListener {
            if (smKitUI != null) {
                smKitUI!!.startAssessment(this)
            }
        }
        binding.startCustomWorkout.setOnClickListener {
            if(smKitUI != null) {
                val smWorkout = SMWorkout(
                    id = "50",
                    name = "demo workout",
                    workoutIntro = "introduction",
                    soundtrack = "soundtrack_7",
                    exercises = viewModel.exercies(),
                    workoutClosure = "workoutClosure.mp3",
                    getInFrame = "bodycal_get_in_frame",
                    bodycalFinished = "bodycal_finished"
                )
                smKitUI!!.startWorkout(smWorkout, this)
            }
        }
        binding.configureButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            smKitUI = SMKitUI.Configuration(baseContext).setUIKey(apiPublicKey)
                .configure(configurationResult)
        }
    }

    private fun observeConfiguration() {
        viewModel.configured.observe(this) {
            if (it) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.startAssessment.visibility = View.VISIBLE
                binding.startCustomWorkout.visibility = View.VISIBLE
            }
        }
    }

    private fun requestPermmions() {
        if (!hasPermissions(baseContext)) {
            launcher.launch(PERMISSIONS_REQUIRED)
        } else {
            binding.configureButton.isEnabled = true
        }
    }

    override fun didExitWorkout(summary: WorkoutSummaryData) {
        Log.d(tag, "didExitWorkout: $summary")
    }

    override fun exerciseDidFinish(data: ExerciseData) {
        Log.d(tag, "exerciseDidFinish: $data")
    }

    override fun handleWorkoutErrors(error: Error) {
        Log.d(tag, "handleWorkoutErrors: $error")
    }

    override fun workoutDidFinish(summary: WorkoutSummaryData) {
        Log.d(tag, "workoutDidFinish: $summary")
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        // Handle Permission granted/rejected
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in PERMISSIONS_REQUIRED && !it.value) permissionGranted = false
        }
        if (permissionGranted && permissions.isNotEmpty()) {
            binding.configureButton.isEnabled = true
        }
        if (!permissionGranted) {
            Toast.makeText(baseContext, "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
