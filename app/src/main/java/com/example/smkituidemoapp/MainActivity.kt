package com.example.smkituidemoapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import com.sency.smkitui.SMKitUI
import com.sency.smkitui.listener.SMKitUIConfigurationListener
import com.sency.smkitui.listener.SMKitUIWorkoutListener
import com.sency.smkitui.model.ExerciseData
import com.sency.smkitui.model.Gender
import com.sency.smkitui.model.SMWorkout
import com.sency.smkitui.model.UserData
import com.sency.smkitui.model.WorkoutSummaryData
import com.sency.smkitui.model.workoutConfig.CounterPreference
import com.sency.smkitui.model.workoutConfig.EndExercisePreference
import com.sency.smkitui.presentation.fragment.PauseDialogTypes
import com.sency.smkitui.model.smkitui.Fitness

class MainActivity : AppCompatActivity(), SMKitUIWorkoutListener {

    private var _binding: MainActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()
    private var smKitUI: SMKitUI? = null

    private val tag = this::class.java.simpleName

    private val apiPublicKey = BuildConfig.sdk_auth_key

    private val configurationResult = object : SMKitUIConfigurationListener {
        override fun onFailure() {
            viewModel.setConfigured(false)
            Log.d("Activity", "failed to configure")
        }

        override fun onSuccess() {
            viewModel.setConfigured(true)
            Log.d("Activity", "succeeded to configure")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermmions()
        observeConfiguration()
        setClickListeners()
        binding.progressBar.visibility = View.VISIBLE
        smKitUI = SMKitUI.Configuration(baseContext)
            .setUIKey(apiPublicKey)
            .configure(configurationResult)
    }

    private fun setClickListeners() {
        binding.startAssessment.setOnClickListener {
            smKitUI?.startAssessment(
                assessmentType = Fitness,
                listener = this,
                userData = UserData(14, Gender.Male),
                showSummary = true,
            )
        }
        binding.startCustomWorkout.setOnClickListener {
            if(smKitUI != null) {
                val smWorkout = SMWorkout(
                    id = "50",
                    name = "demo workout",
                    workoutIntro = "workoutIntro",
                    soundtrack = "soundtrack_7",
                    exercises = viewModel.exercies(),
                    workoutClosure = "workoutClosure",
                    getInFrame = "getInFrame",
                    bodycalFinished = "bodycalFinished"
                )
                smKitUI?.startCustomizedAssessment(smWorkout, true, this)
            }
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
        }
    }

    override fun didExitWorkout(summary: WorkoutSummaryData) {
        Log.d(tag, "didExitWorkout: $summary")
        smKitUI?.quitWorkout()
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
