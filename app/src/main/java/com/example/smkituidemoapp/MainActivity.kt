package com.example.smkituidemoapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smkituidemoapp.databinding.MainActivityBinding
import com.example.smkituidemoapp.viewModels.MainViewModel
import com.example.smkituidemoapp.viewModels.SdkFeatureSettings
import com.sency.smkitui.SMKitUI
import com.sency.smkitui.listener.SMKitUIConfigurationListener
import com.sency.smkitui.listener.SMKitUIWorkoutListener
import com.sency.smkitui.model.ExerciseData
import com.sency.smkitui.model.Gender
import com.sency.smkitui.model.SMWorkout
import com.sency.smkitui.model.SMWorkoutContinuation
import com.sency.smkitui.model.UserData
import com.sency.smkitui.model.WorkoutSummaryData
import com.sency.smkitui.model.workoutConfig.CounterPreference
import com.sency.smkitui.model.workoutConfig.EndExercisePreference
import com.sency.smkitui.model.SkeletonPreset
import com.sency.smkitui.model.InstructionVideoConfig
import com.sency.smkitui.model.VideoDisplayMode
import com.sency.smkitui.presentation.fragment.PauseDialogTypes
import com.sency.smkitui.model.smkitui.Fitness

class MainActivity : AppCompatActivity(), SMKitUIWorkoutListener {

    private var _binding: MainActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()
    private var smKitUI: SMKitUI? = null
    private val selectedSdkFeatureDetectors = linkedSetOf<String>()
    private val sdkFeatureExerciseChecks = mutableMapOf<String, CheckBox>()

    private val tag = this::class.java.simpleName

    private val apiPublicKey = BuildConfig.sdk_auth_key

    private val configurationResult = object : SMKitUIConfigurationListener {
        override fun onFailure() {
            viewModel.setConfigured(false)
            Log.d("Activity", "failed to configure")
        }

        override fun onFailure(error: String) {
            viewModel.setConfigured(false)
            Log.d("Activity", error)
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
        populateSdkFeatureExerciseMenu()
        binding.progressBar.visibility = View.VISIBLE
        // To customize UI colors, use setColorTheme(UIColorTheme.BLUE)
        // Available colors: BLUE, GREEN (default), PURPLE, ORANGE, SILVER, GOLD, PINK
        smKitUI = SMKitUI.Configuration(baseContext)
            .setUIKey(apiPublicKey)
            // 1.4.9: Apply a skeleton visualisation preset at configuration time.
            // Fine-tune further with individual properties (see README for all options).
            .applySkeletonSettings {
                skeletonPreset = SkeletonPreset.DEFAULT
            }
            .applyBasicSettings(readBasicSettings())
            .configure(configurationResult)

        // 1.4.9: Enable intelligence-driven rest suggestions based on fatigue detection.
        smKitUI?.setIntelligenceRestEnabled(true)

        // 1.4.9: Choose which buttons appear on the pause overlay.
        // Buttons are activated by hovering the palm over the icon (~1.5 s hold).
        smKitUI?.setPauseTypes(
            arrayOf(
                PauseDialogTypes.Resume,
                PauseDialogTypes.Skip,
                PauseDialogTypes.StartOver,
                PauseDialogTypes.Quit,
            )
        )
    }

    private fun setClickListeners() {
        bindBasicSettings()

        binding.sdkFeaturesDemo.setOnClickListener {
            showSdkFeaturesScreen()
        }
        binding.backToHome.setOnClickListener {
            showHomeScreen()
        }
        binding.sdkFeatureSettings.setOnClickListener {
            binding.sdkFeatureSettingsPanel.visibility =
                if (binding.sdkFeatureSettingsPanel.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        binding.selectAllSdkExercises.setOnClickListener {
            sdkFeatureExerciseChecks.values.forEach { it.isChecked = true }
        }
        binding.clearSdkExerciseSelection.setOnClickListener {
            sdkFeatureExerciseChecks.values.forEach { it.isChecked = false }
        }
        binding.startSdkFeaturesWorkout.setOnClickListener {
            startSdkFeaturesWorkout()
        }
        binding.startAssessment.setOnClickListener {
            applyBasicSettingsToSdk()
            val modifications = getExampleModificationsJson()

            // Configure instruction video cycling (optional - uncomment to test)
            // smKitUI?.setInstructionVideoConfig(
            //     InstructionVideoConfig(
            //         displayMode = VideoDisplayMode.MEDIUM_CYCLE,
            //         mediumSizeCycles = 3
            //     )
            // )

            smKitUI?.startAssessment(
                assessmentType = Fitness,
                listener = this,
                userData = UserData(14, Gender.Male),
                showSummary = true,
                modifications = modifications, // Pass modifications dict here
                showPhoneCalibration = true
            )
        }
        binding.startCustomWorkout.setOnClickListener {
            if(smKitUI != null) {
                applyBasicSettingsToSdk()
                val smWorkout = SMWorkout(
                    id = "50",
                    name = "demo workout",
                    workoutIntro = "workoutIntro",
                    soundtrack = "soundtrack_7",
                    exercises = viewModel.workoutExercises(),
                    workoutClosure = "workoutClosure",
                    getInFrame = "getInFrame",
                    bodycalFinished = "bodycalFinished",
                    continuation = SMWorkoutContinuation(
                        introSoundKey = "continue_workout_intro",
                        interactionUnlockSoundKey = "smkitui_button_tutorial_start",
                        exercises = viewModel.continuationExercises(),
                    )
                )
                val modifications = getExampleModificationsJson()
                smKitUI?.startCustomizedWorkout(
                    smWorkout,
                    listener = this,
                    modifications = modifications,
                    showPhoneCalibration = true
                )
            }
        }
        binding.clearAdaptiveRomCache.setOnClickListener {
            smKitUI?.clearAdaptiveRomCache()
            Toast.makeText(baseContext, "Adaptive ROM cache cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSdkFeaturesScreen() {
        binding.homeScreen.visibility = View.GONE
        binding.sdkFeaturesScreen.visibility = View.VISIBLE
    }

    private fun showHomeScreen() {
        binding.sdkFeaturesScreen.visibility = View.GONE
        binding.homeScreen.visibility = View.VISIBLE
    }

    private fun populateSdkFeatureExerciseMenu() {
        binding.sdkExerciseListContainer.removeAllViews()
        sdkFeatureExerciseChecks.clear()
        viewModel.sdkFeatureDetectors().forEach { detector ->
            val checkBox = CheckBox(this).apply {
                text = detector
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedSdkFeatureDetectors.add(detector)
                    } else {
                        selectedSdkFeatureDetectors.remove(detector)
                    }
                    updateSdkFeatureSelectionState()
                }
            }
            sdkFeatureExerciseChecks[detector] = checkBox
            binding.sdkExerciseListContainer.addView(checkBox)
        }
        updateSdkFeatureSelectionState()
    }

    private fun updateSdkFeatureSelectionState() {
        val selected = selectedSdkFeatureDetectors.size
        binding.sdkFeatureSelectedCount.text = "Selected: $selected"
        binding.startSdkFeaturesWorkout.isEnabled = selected > 0
    }

    private fun startSdkFeaturesWorkout() {
        val detectors = selectedSdkFeatureDetectors.toList()
        if (detectors.isEmpty()) {
            Toast.makeText(baseContext, "Select at least one exercise", Toast.LENGTH_SHORT).show()
            return
        }
        val settings = readBasicSettings()
        applyBasicSettingsToSdk(settings)
        val smWorkout = SMWorkout(
            id = "sdk_features_demo",
            name = "SDK Features Demo",
            workoutIntro = "",
            soundtrack = "",
            exercises = viewModel.sdkFeatureWorkoutExercises(detectors, settings),
            workoutClosure = "",
            getInFrame = "",
            bodycalFinished = "",
        )
        smKitUI?.startCustomizedWorkout(
            smWorkout,
            listener = this,
            modifications = getExampleModificationsJson(),
            showPhoneCalibration = true,
        )
    }

    private fun bindBasicSettings() {
        val settingSwitches = listOf(
            binding.useDefaultGuidanceModeSwitch,
            binding.guidanceDebugLoggingSwitch,
            binding.variationMismatchFeedbackSwitch,
            binding.phoneMovementPreventionSwitch,
            binding.startTimerOnFirstActivitySwitch,
            binding.playPhoneCalibrationAudioSwitch,
            binding.playBodyCalibrationAudioSwitch,
            binding.allowAudioMixingSwitch,
            binding.showExternalAudioControlSwitch,
            binding.enableButtonTutorialSwitch,
            binding.shortIntroSwitch,
            binding.preExerciseCountdownSwitch,
            binding.soundOnEachRepSwitch,
            binding.repMilestoneVoiceSwitch,
            binding.adaptiveRomFeedbackSwitch,
            binding.stretchSetConfigSwitch,
        )
        settingSwitches.forEach { settingSwitch ->
            settingSwitch.setOnCheckedChangeListener { _, _ ->
                applyBasicSettingsToSdk()
            }
        }
        binding.workoutContinuationTimerInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                applyBasicSettingsToSdk()
            }
        })
    }

    private fun applyBasicSettingsToSdk() {
        applyBasicSettingsToSdk(readBasicSettings())
    }

    private fun applyBasicSettingsToSdk(settings: SdkFeatureSettings) {
        smKitUI?.setUseDefaultGuidanceMode(settings.useDefaultGuidanceMode)
        smKitUI?.setGuidanceDebugLogging(settings.guidanceDebugLogging)
        smKitUI?.setVariationMismatchFeedbackEnabled(settings.variationMismatchFeedback)
        smKitUI?.setPhoneMovementCountPreventionEnabled(settings.phoneMovementPrevention)
        smKitUI?.setStartTimerOnFirstActivity(settings.startTimerOnFirstActivity)
        smKitUI?.setWorkoutContinuationTimerDuration(settings.workoutContinuationTimerSeconds)
        smKitUI?.setPlayPhoneCalibrationAudio(settings.playPhoneCalibrationAudio)
        smKitUI?.setPlayBodyCalibrationAudio(settings.playBodyCalibrationAudio)
        smKitUI?.setAllowAudioMixing(settings.allowAudioMixing)
        smKitUI?.setShowExternalAudioControl(settings.showExternalAudioControl)
        smKitUI?.setEnableButtonTutorial(settings.enableButtonTutorial)
    }

    private fun readBasicSettings() = SdkFeatureSettings(
        useDefaultGuidanceMode = binding.useDefaultGuidanceModeSwitch.isChecked,
        guidanceDebugLogging = binding.guidanceDebugLoggingSwitch.isChecked,
        variationMismatchFeedback = binding.variationMismatchFeedbackSwitch.isChecked,
        phoneMovementPrevention = binding.phoneMovementPreventionSwitch.isChecked,
        startTimerOnFirstActivity = binding.startTimerOnFirstActivitySwitch.isChecked,
        workoutContinuationTimerSeconds = binding.workoutContinuationTimerInput.text
            ?.toString()
            ?.toIntOrNull()
            ?.coerceAtLeast(1)
            ?: 10,
        playPhoneCalibrationAudio = binding.playPhoneCalibrationAudioSwitch.isChecked,
        playBodyCalibrationAudio = binding.playBodyCalibrationAudioSwitch.isChecked,
        allowAudioMixing = binding.allowAudioMixingSwitch.isChecked,
        showExternalAudioControl = binding.showExternalAudioControlSwitch.isChecked,
        enableButtonTutorial = binding.enableButtonTutorialSwitch.isChecked,
        shortIntro = binding.shortIntroSwitch.isChecked,
        preExerciseCountdown = binding.preExerciseCountdownSwitch.isChecked,
        soundOnEachRep = binding.soundOnEachRepSwitch.isChecked,
        repMilestoneVoice = binding.repMilestoneVoiceSwitch.isChecked,
        adaptiveRomFeedback = binding.adaptiveRomFeedbackSwitch.isChecked,
        stretchSetConfig = binding.stretchSetConfigSwitch.isChecked,
    )

    private fun SMKitUI.Configuration.applyBasicSettings(settings: SdkFeatureSettings): SMKitUI.Configuration =
        setUseDefaultGuidanceMode(settings.useDefaultGuidanceMode)
            .setGuidanceDebugLogging(settings.guidanceDebugLogging)
            .setVariationMismatchFeedbackEnabled(settings.variationMismatchFeedback)
            .setPhoneMovementCountPreventionEnabled(settings.phoneMovementPrevention)
            .setStartTimerOnFirstActivity(settings.startTimerOnFirstActivity)
            .setWorkoutContinuationTimerDuration(settings.workoutContinuationTimerSeconds)
            .setPlayPhoneCalibrationAudio(settings.playPhoneCalibrationAudio)
            .setPlayBodyCalibrationAudio(settings.playBodyCalibrationAudio)
            .setAllowAudioMixing(settings.allowAudioMixing)
            .setShowExternalAudioControl(settings.showExternalAudioControl)
            .setEnableButtonTutorial(settings.enableButtonTutorial)

    private fun getExampleModificationsJson(): String? {
        // Example 1: JSON format (recommended - easier to read and maintain)
        // return """
        // {
        //     "Crunches": {
        //         "CrunchesShallowDepth": {
        //             "low": 0.25,
        //             "high": 0.75
        //         }
        //     }
        // }
        // """.trimIndent()
        
        // Example 2: Return null for no modifications (default behavior)
        return null
        
        // Example 3: Use setConfigString() directly with flat format
        // smKitUI?.setConfigString("""
        //     # Crunches
        //     CrunchesShallowDepth.low=0.25
        //     CrunchesShallowDepth.high=0.75
        // """.trimIndent())
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

    override fun workoutContinuationPromptDidAppear() {
        Log.d(tag, "workoutContinuationPromptDidAppear")
    }

    override fun workoutContinuationUserDidChoose(continueWorkout: Boolean) {
        Log.d(tag, "workoutContinuationUserDidChoose: $continueWorkout")
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
