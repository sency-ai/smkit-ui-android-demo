package com.example.smkituidemoapp

import android.Manifest
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
import com.sency.smkit.PoseModelChoice
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
import com.sency.smkitui.model.UIColorTheme
import com.sency.smkitui.model.VideoDisplayMode
import com.sency.smkitui.presentation.fragment.PauseDialogTypes
import com.sency.smkitui.model.smkitui.Fitness
import com.sency.smkitui.model.workoutConfig.BodyZone
import com.sency.smkitui.model.workoutConfig.DifficultyLevel
import com.sency.smkitui.model.workoutConfig.SMLanguage
import com.sency.smkitui.model.workoutConfig.WorkoutConfig
import com.sency.smkitui.model.workoutConfig.WorkoutDuration

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
            showConfigurationFailure("SMKitUI configuration failed")
        }

        override fun onFailure(error: String) {
            viewModel.setConfigured(false)
            showConfigurationFailure(error)
        }

        override fun onSuccess() {
            viewModel.setConfigured(true)
            Log.d("Activity", "succeeded to configure")
        }
    }

    private fun showConfigurationFailure(message: String) {
        Log.e(tag, message)
        binding.progressBar.visibility = View.INVISIBLE
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestCameraPermission()
        observeConfiguration()
        setClickListeners()
        populateSdkFeatureExerciseMenu()
        configureSdk()
    }

    private fun configureSdk() {
        val settings = readBasicSettings()
        viewModel.setConfigured(false)
        binding.progressBar.visibility = View.VISIBLE
        smKitUI = SMKitUI.Configuration(applicationContext)
            .setUIKey(apiPublicKey)
            .setPoseModelChoice(PoseModelChoice.AdaptiveChoice)
            .setConfigureLanguage(settings.sessionLanguage())
            .setInstructionVideoConfig(settings.instructionVideoConfig())
            .setSmallBodyPartFocusEnabled(settings.smallBodyPartFocus)
            .setExerciseSummaryTimingMetricsEnabled(settings.exerciseSummaryTimingMetrics)
            .setIncludeAssessmentInsights(settings.includeAssessmentInsights)
            .setGuidanceModeSuggestionEnabled(settings.guidanceModeSuggestion)
            .setColorTheme(UIColorTheme.GREEN)
            .setButtonTutorialCompletionAudioUri(null)
            .applySkeletonSettings { skeletonPreset = SkeletonPreset.DEFAULT }
            .applyBasicSettings(settings)
            .configure(configurationResult)

        applyBasicSettingsToSdk(settings)
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
                val settings = readBasicSettings()
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
                    ),
                    exportInternalInsights = settings.exportAssessmentInsights,
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
        binding.startCustomAssessment.setOnClickListener {
            val settings = readBasicSettings()
            applyBasicSettingsToSdk(settings)
            val assessment = SMWorkout(
                id = "custom_assessment_demo",
                name = "Custom Assessment Demo",
                workoutIntro = "",
                soundtrack = "",
                exercises = viewModel.exercies(),
                workoutClosure = "",
                getInFrame = "",
                bodycalFinished = "",
                exportInternalInsights = settings.exportAssessmentInsights,
            )
            smKitUI?.startCustomizedAssessment(
                workout = assessment,
                showSummary = true,
                listener = this,
                modifications = getExampleModificationsJson(),
                showPhoneCalibration = true,
            )
        }
        binding.startWorkoutProgram.setOnClickListener {
            val programId = binding.programIdInput.text?.toString()?.trim().orEmpty()
            if (programId.isEmpty()) {
                Toast.makeText(baseContext, "Enter the program ID supplied by Sency", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val settings = readBasicSettings()
            applyBasicSettingsToSdk(settings)
            smKitUI?.startWorkoutProgram(
                workoutConfig = WorkoutConfig(
                    programId = programId,
                    week = 1,
                    bodyZone = BodyZone.FullBody,
                    difficultyLevel = DifficultyLevel.MidDifficulty,
                    workoutDuration = WorkoutDuration.Short,
                    language = settings.sessionLanguage(),
                    shortIntro = settings.shortIntro,
                ),
                listener = this,
                modifications = getExampleModificationsJson(),
                showPhoneCalibration = true,
            )
        }
        binding.applySdkConfiguration.setOnClickListener {
            configureSdk()
            Toast.makeText(baseContext, "Reconfiguring SMKitUI with current settings", Toast.LENGTH_SHORT).show()
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
            exportInternalInsights = settings.exportAssessmentInsights,
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
            binding.guidanceModeSuggestionSwitch,
            binding.guidanceDebugLoggingSwitch,
            binding.smallBodyPartFocusSwitch,
            binding.variationMismatchFeedbackSwitch,
            binding.phoneMovementPreventionSwitch,
            binding.startTimerOnFirstActivitySwitch,
            binding.exerciseSummaryTimingMetricsSwitch,
            binding.includeAssessmentInsightsSwitch,
            binding.exportAssessmentInsightsSwitch,
            binding.hebrewSessionSwitch,
            binding.perfectOnlyCounterSwitch,
            binding.targetBasedCompletionSwitch,
            binding.mediumCycleInstructionVideoSwitch,
            binding.playPhoneCalibrationAudioSwitch,
            binding.playBodyCalibrationAudioSwitch,
            binding.allowAudioMixingSwitch,
            binding.showExternalAudioControlSwitch,
            binding.enableButtonTutorialSwitch,
            binding.shortIntroSwitch,
            binding.preExerciseCountdownSwitch,
            binding.soundOnEachRepSwitch,
            binding.repMilestoneVoiceSwitch,
            binding.targetRepsCompletionVoiceSwitch,
            binding.intentVoiceFeedbackSwitch,
            binding.showTargetProgressSwitch,
            binding.adaptiveRomFeedbackSwitch,
            binding.stretchSetConfigSwitch,
            binding.positionRepsSwitch,
            binding.exerciseProgressDisplaySwitch,
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
        smKitUI?.setSessionLanguage(settings.sessionLanguage())
        smKitUI?.setPhoneCalibrationLanguage(settings.sessionLanguage())
        smKitUI?.setCounterPreferences(
            if (settings.perfectOnlyCounter) CounterPreference.PerfectOnly else CounterPreference.Default
        )
        smKitUI?.setEndExercisePreferences(
            if (settings.targetBasedCompletion) EndExercisePreference.TargetBased else EndExercisePreference.Default
        )
        smKitUI?.setIntelligenceRestEnabled(true)
        smKitUI?.setInstructionVideoConfig(settings.instructionVideoConfig())
        smKitUI?.setSmallBodyPartFocusEnabled(settings.smallBodyPartFocus)
        smKitUI?.setUseDefaultGuidanceMode(settings.useDefaultGuidanceMode)
        smKitUI?.setGuidanceModeSuggestionEnabled(settings.guidanceModeSuggestion)
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
        smKitUI?.setButtonTutorialCompletionAudioUri(null)
        smKitUI?.setColorTheme(UIColorTheme.GREEN)
        smKitUI?.applySkeletonSettings { skeletonPreset = SkeletonPreset.DEFAULT }
        smKitUI?.setFeedbacksUIToExclude(emptySet())
        smKitUI?.setConfigString(
            binding.sdkConfigStringInput.text?.toString()?.trim()?.takeIf(String::isNotEmpty)
        )
    }

    private fun readBasicSettings() = SdkFeatureSettings(
        useDefaultGuidanceMode = binding.useDefaultGuidanceModeSwitch.isChecked,
        guidanceModeSuggestion = binding.guidanceModeSuggestionSwitch.isChecked,
        guidanceDebugLogging = binding.guidanceDebugLoggingSwitch.isChecked,
        smallBodyPartFocus = binding.smallBodyPartFocusSwitch.isChecked,
        variationMismatchFeedback = binding.variationMismatchFeedbackSwitch.isChecked,
        phoneMovementPrevention = binding.phoneMovementPreventionSwitch.isChecked,
        startTimerOnFirstActivity = binding.startTimerOnFirstActivitySwitch.isChecked,
        exerciseSummaryTimingMetrics = binding.exerciseSummaryTimingMetricsSwitch.isChecked,
        includeAssessmentInsights = binding.includeAssessmentInsightsSwitch.isChecked,
        exportAssessmentInsights = binding.exportAssessmentInsightsSwitch.isChecked,
        hebrewSession = binding.hebrewSessionSwitch.isChecked,
        perfectOnlyCounter = binding.perfectOnlyCounterSwitch.isChecked,
        targetBasedCompletion = binding.targetBasedCompletionSwitch.isChecked,
        mediumCycleInstructionVideo = binding.mediumCycleInstructionVideoSwitch.isChecked,
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
        targetRepsCompletionVoice = binding.targetRepsCompletionVoiceSwitch.isChecked,
        intentVoiceFeedback = binding.intentVoiceFeedbackSwitch.isChecked,
        showTargetProgress = binding.showTargetProgressSwitch.isChecked,
        adaptiveRomFeedback = binding.adaptiveRomFeedbackSwitch.isChecked,
        stretchSetConfig = binding.stretchSetConfigSwitch.isChecked,
        positionReps = binding.positionRepsSwitch.isChecked,
        exerciseProgressDisplay = binding.exerciseProgressDisplaySwitch.isChecked,
    )

    private fun SMKitUI.Configuration.applyBasicSettings(settings: SdkFeatureSettings): SMKitUI.Configuration =
        setUseDefaultGuidanceMode(settings.useDefaultGuidanceMode)
            .setGuidanceModeSuggestionEnabled(settings.guidanceModeSuggestion)
            .setGuidanceDebugLogging(settings.guidanceDebugLogging)
            .setSmallBodyPartFocusEnabled(settings.smallBodyPartFocus)
            .setVariationMismatchFeedbackEnabled(settings.variationMismatchFeedback)
            .setPhoneMovementCountPreventionEnabled(settings.phoneMovementPrevention)
            .setStartTimerOnFirstActivity(settings.startTimerOnFirstActivity)
            .setWorkoutContinuationTimerDuration(settings.workoutContinuationTimerSeconds)
            .setPlayPhoneCalibrationAudio(settings.playPhoneCalibrationAudio)
            .setPlayBodyCalibrationAudio(settings.playBodyCalibrationAudio)
            .setAllowAudioMixing(settings.allowAudioMixing)
            .setShowExternalAudioControl(settings.showExternalAudioControl)
            .setEnableButtonTutorial(settings.enableButtonTutorial)

    private fun SdkFeatureSettings.sessionLanguage(): SMLanguage =
        if (hebrewSession) SMLanguage.Hebrew else SMLanguage.English

    private fun SdkFeatureSettings.instructionVideoConfig(): InstructionVideoConfig =
        InstructionVideoConfig(
            displayMode = if (mediumCycleInstructionVideo) {
                VideoDisplayMode.MEDIUM_CYCLE
            } else {
                VideoDisplayMode.DEFAULT
            },
            mediumSizeCycles = 3,
        )

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

    private fun requestCameraPermission() {
        if (!hasPermission(Manifest.permission.CAMERA)) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(baseContext, "Camera permission is required", Toast.LENGTH_LONG).show()
            }
        }

    private fun hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

}
