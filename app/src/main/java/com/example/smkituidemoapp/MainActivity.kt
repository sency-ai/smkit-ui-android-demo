package com.example.smkituidemoapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smkituidemoapp.databinding.MainActivityBinding
import com.example.smkituidemoapp.viewModels.MainViewModel
import com.example.smkituidemoapp.viewModels.SdkFeatureSettings
import com.sency.smkit.PoseModelChoice
import com.sency.smbase.nativeclient.model.FormFeedbackType
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
import com.sency.smkitui.model.SkeletonColorOption
import com.sency.smkitui.model.SkeletonConnectionStyle
import com.sency.smkitui.model.SkeletonJointShape
import com.sency.smkitui.model.InstructionVideoConfig
import com.sency.smkitui.model.UIColorTheme
import com.sency.smkitui.model.VideoDisplayMode
import com.sency.smkitui.presentation.fragment.PauseDialogTypes
import com.sency.smkitui.model.smkitui.AssessmentType
import com.sency.smkitui.model.smkitui.Body360
import com.sency.smkitui.model.smkitui.Cardio
import com.sency.smkitui.model.smkitui.Fitness
import com.sency.smkitui.model.smkitui.Strength
import com.sency.smkitui.model.workoutConfig.BodyZone
import com.sency.smkitui.model.workoutConfig.DifficultyLevel
import com.sency.smkitui.model.workoutConfig.SMLanguage
import com.sency.smkitui.model.workoutConfig.WorkoutConfig
import com.sency.smkitui.model.workoutConfig.WorkoutDuration
import java.util.Locale

class MainActivity : AppCompatActivity(), SMKitUIWorkoutListener {

    private var _binding: MainActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()
    private var smKitUI: SMKitUI? = null
    private val selectedSdkFeatureDetectors = linkedSetOf<String>()
    private val sdkFeatureExerciseChecks = mutableMapOf<String, CheckBox>()
    private val settingsPreferences by lazy {
        getSharedPreferences("smkitui_demo_settings", MODE_PRIVATE)
    }
    private var restoringSettings = false

    private val builtInAssessmentOptions: List<Pair<String, AssessmentType>> = listOf(
        "Fitness" to Fitness,
        "Body 360" to Body360,
        "Cardio" to Cardio,
        "Strength" to Strength,
    )

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
        setupSettingsControls()
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
            .setPoseModelChoice(settings.poseModelChoice)
            .setConfigureLanguage(settings.sessionLanguage)
            .setInstructionVideoConfig(settings.instructionVideoConfig())
            .setSmallBodyPartFocusEnabled(settings.smallBodyPartFocus)
            .setExerciseSummaryTimingMetricsEnabled(settings.exerciseSummaryTimingMetrics)
            .setIncludeAssessmentInsights(settings.includeAssessmentInsights)
            .setGuidanceModeSuggestionEnabled(settings.guidanceModeSuggestion)
            .setColorTheme(settings.colorTheme)
            .setButtonTutorialCompletionAudioUri(null)
            .applySkeletonSettings { applyDemoSettings(settings) }
            .applyBasicSettings(settings)
            .configure(configurationResult)

        applyBasicSettingsToSdk(settings)
        smKitUI?.setPauseTypes(settings.pauseTypes.toTypedArray())
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
            val settings = readBasicSettings()
            applyBasicSettingsToSdk(settings)
            val modifications = getExampleModificationsJson()

            // Configure instruction video cycling (optional - uncomment to test)
            // smKitUI?.setInstructionVideoConfig(
            //     InstructionVideoConfig(
            //         displayMode = VideoDisplayMode.MEDIUM_CYCLE,
            //         mediumSizeCycles = 3
            //     )
            // )

            smKitUI?.startAssessment(
                assessmentType = settings.assessmentType,
                listener = this,
                userData = UserData(14, Gender.Male),
                showSummary = true,
                modifications = modifications, // Pass modifications dict here
                showPhoneCalibration = settings.showPhoneCalibration
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
                    showPhoneCalibration = settings.showPhoneCalibration
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
                showPhoneCalibration = settings.showPhoneCalibration,
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
                    language = settings.sessionLanguage,
                    shortIntro = settings.shortIntro,
                ),
                listener = this,
                modifications = getExampleModificationsJson(),
                showPhoneCalibration = settings.showPhoneCalibration,
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
            showPhoneCalibration = settings.showPhoneCalibration,
        )
    }

    private fun setupSettingsControls() {
        restoringSettings = true

        bindSpinner(
            binding.builtInAssessmentTypeSpinner,
            builtInAssessmentOptions.map { it.first },
        )
        bindSpinner(binding.poseModelChoiceSpinner, PoseModelChoice.values().map { it.name })
        bindSpinner(binding.colorThemeSpinner, UIColorTheme.values().map { it.name.toDisplayName() })
        bindSpinner(binding.sessionLanguageSpinner, SMLanguage.values().map { it.name })
        bindSpinner(binding.phoneCalibrationLanguageSpinner, SMLanguage.values().map { it.name })
        bindSpinner(binding.skeletonPresetSpinner, SkeletonPreset.values().map { it.name.toDisplayName() })
        bindSpinner(
            binding.skeletonConnectionStyleSpinner,
            SkeletonConnectionStyle.values().map { it.name.toDisplayName() },
        )
        bindSpinner(binding.skeletonJointShapeSpinner, SkeletonJointShape.values().map { it.name.toDisplayName() })
        val skeletonColors = listOf("Preset") + SkeletonColorOption.values().map { it.name.toDisplayName() }
        bindSpinner(binding.skeletonDotsInnerColorSpinner, skeletonColors)
        bindSpinner(binding.skeletonDotsOuterColorSpinner, skeletonColors)
        bindSpinner(binding.skeletonConnectionsInnerColorSpinner, skeletonColors)
        bindSpinner(binding.skeletonConnectionsOuterColorSpinner, skeletonColors)

        settingsSwitches().forEach { settingSwitch ->
            val key = settingSwitch.preferenceKey()
            settingSwitch.isChecked = settingsPreferences.getBoolean(key, settingSwitch.isChecked)
        }
        settingsSeekBars().forEach { seekBar ->
            val key = seekBar.preferenceKey()
            seekBar.progress = settingsPreferences.getInt(key, seekBar.progress)
        }
        binding.workoutContinuationTimerInput.setText(
            settingsPreferences.getString("workoutContinuationTimerInput", binding.workoutContinuationTimerInput.text?.toString())
        )
        binding.instructionVideoCyclesInput.setText(
            settingsPreferences.getString("instructionVideoCyclesInput", binding.instructionVideoCyclesInput.text?.toString())
        )
        binding.sdkConfigStringInput.setText(
            settingsPreferences.getString("sdkConfigStringInput", "")
        )

        restoringSettings = false
    }

    private fun bindSpinner(spinner: Spinner, labels: List<String>) {
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            labels,
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        val key = spinner.preferenceKey()
        spinner.setSelection(settingsPreferences.getInt(key, spinner.selectedItemPosition).coerceIn(labels.indices))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (restoringSettings) return
                settingsPreferences.edit().putInt(key, position).apply()
                applyBasicSettingsToSdk()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun settingsSwitches(): List<CompoundButton> = listOf(
        binding.showPhoneCalibrationSwitch,
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
        binding.perfectOnlyCounterSwitch,
        binding.targetBasedCompletionSwitch,
        binding.mediumCycleInstructionVideoSwitch,
        binding.playPhoneCalibrationAudioSwitch,
        binding.playBodyCalibrationAudioSwitch,
        binding.allowAudioMixingSwitch,
        binding.showExternalAudioControlSwitch,
        binding.enableButtonTutorialSwitch,
        binding.intelligenceRestSwitch,
        binding.excludePushupKneesFeedbackSwitch,
        binding.skeletonHiddenSwitch,
        binding.pauseResumeSwitch,
        binding.pauseSkipSwitch,
        binding.pauseStartOverSwitch,
        binding.pauseQuitSwitch,
        binding.pauseSwitchSwitch,
        binding.useWideAngleCameraSwitch,
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

    private fun settingsSeekBars(): List<SeekBar> = listOf(
        binding.skeletonDotsOpacitySeekBar,
        binding.skeletonConnectionsOpacitySeekBar,
        binding.skeletonDotsGlowSeekBar,
        binding.skeletonConnectionsGlowSeekBar,
        binding.skeletonLineWidthSeekBar,
        binding.skeletonOutlineSeekBar,
        binding.skeletonSoftnessSeekBar,
        binding.skeletonAnimationSeekBar,
    )

    private fun bindBasicSettings() {
        settingsSwitches().forEach { settingSwitch ->
            settingSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (restoringSettings) return@setOnCheckedChangeListener
                settingsPreferences.edit().putBoolean(settingSwitch.preferenceKey(), isChecked).apply()
                applyBasicSettingsToSdk()
            }
        }
        settingsSeekBars().forEach { seekBar ->
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (!fromUser || restoringSettings) return
                    settingsPreferences.edit().putInt(seekBar.preferenceKey(), progress).apply()
                    applyBasicSettingsToSdk()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })
        }
        binding.workoutContinuationTimerInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                if (restoringSettings) return
                settingsPreferences.edit().putString("workoutContinuationTimerInput", s?.toString()).apply()
                applyBasicSettingsToSdk()
            }
        })
        binding.instructionVideoCyclesInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                if (restoringSettings) return
                settingsPreferences.edit().putString("instructionVideoCyclesInput", s?.toString()).apply()
                applyBasicSettingsToSdk()
            }
        })
        binding.sdkConfigStringInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                if (restoringSettings) return
                settingsPreferences.edit().putString("sdkConfigStringInput", s?.toString()).apply()
            }
        })
    }

    private fun applyBasicSettingsToSdk() {
        applyBasicSettingsToSdk(readBasicSettings())
    }

    private fun applyBasicSettingsToSdk(settings: SdkFeatureSettings) {
        smKitUI?.setSessionLanguage(settings.sessionLanguage)
        smKitUI?.setPhoneCalibrationLanguage(settings.phoneCalibrationLanguage)
        smKitUI?.setCounterPreferences(
            if (settings.perfectOnlyCounter) CounterPreference.PerfectOnly else CounterPreference.Default
        )
        smKitUI?.setEndExercisePreferences(
            if (settings.targetBasedCompletion) EndExercisePreference.TargetBased else EndExercisePreference.Default
        )
        smKitUI?.setIntelligenceRestEnabled(settings.intelligenceRest)
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
        smKitUI?.setColorTheme(settings.colorTheme)
        smKitUI?.applySkeletonSettings { applyDemoSettings(settings) }
        smKitUI?.setPauseTypes(settings.pauseTypes.toTypedArray())
        smKitUI?.setFeedbacksUIToExclude(
            if (settings.excludePushupKneesFeedback) {
                setOf(FormFeedbackType.PushupKneesOnFloor)
            } else {
                emptySet()
            }
        )
        smKitUI?.setConfigString(
            binding.sdkConfigStringInput.text?.toString()?.trim()?.takeIf(String::isNotEmpty)
        )
    }

    private fun readBasicSettings() = SdkFeatureSettings(
        assessmentType = builtInAssessmentOptions[binding.builtInAssessmentTypeSpinner.selectedItemPosition].second,
        showPhoneCalibration = binding.showPhoneCalibrationSwitch.isChecked,
        poseModelChoice = PoseModelChoice.values()[binding.poseModelChoiceSpinner.selectedItemPosition],
        colorTheme = UIColorTheme.values()[binding.colorThemeSpinner.selectedItemPosition],
        sessionLanguage = SMLanguage.values()[binding.sessionLanguageSpinner.selectedItemPosition],
        phoneCalibrationLanguage = SMLanguage.values()[binding.phoneCalibrationLanguageSpinner.selectedItemPosition],
        skeletonHidden = binding.skeletonHiddenSwitch.isChecked,
        skeletonPreset = SkeletonPreset.values()[binding.skeletonPresetSpinner.selectedItemPosition],
        skeletonConnectionStyle = SkeletonConnectionStyle.values()[binding.skeletonConnectionStyleSpinner.selectedItemPosition],
        skeletonJointShape = SkeletonJointShape.values()[binding.skeletonJointShapeSpinner.selectedItemPosition],
        skeletonDotsInnerColor = binding.skeletonDotsInnerColorSpinner.selectedSkeletonColor(),
        skeletonDotsOuterColor = binding.skeletonDotsOuterColorSpinner.selectedSkeletonColor(),
        skeletonConnectionsInnerColor = binding.skeletonConnectionsInnerColorSpinner.selectedSkeletonColor(),
        skeletonConnectionsOuterColor = binding.skeletonConnectionsOuterColorSpinner.selectedSkeletonColor(),
        skeletonDotsOpacity = binding.skeletonDotsOpacitySeekBar.progress / 100f,
        skeletonConnectionsOpacity = binding.skeletonConnectionsOpacitySeekBar.progress / 100f,
        skeletonDotsGlow = binding.skeletonDotsGlowSeekBar.progress / 100f,
        skeletonConnectionsGlow = binding.skeletonConnectionsGlowSeekBar.progress / 100f,
        skeletonLineWidthScale = (binding.skeletonLineWidthSeekBar.progress + 50) / 100f,
        skeletonOutlineScale = (binding.skeletonOutlineSeekBar.progress + 50) / 100f,
        skeletonSoftness = binding.skeletonSoftnessSeekBar.progress / 100f,
        skeletonAnimationDurationSeconds = binding.skeletonAnimationSeekBar.progress / 1000f,
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
        perfectOnlyCounter = binding.perfectOnlyCounterSwitch.isChecked,
        targetBasedCompletion = binding.targetBasedCompletionSwitch.isChecked,
        mediumCycleInstructionVideo = binding.mediumCycleInstructionVideoSwitch.isChecked,
        instructionVideoCycles = binding.instructionVideoCyclesInput.text
            ?.toString()?.toIntOrNull()?.coerceIn(1, 5) ?: 3,
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
        intelligenceRest = binding.intelligenceRestSwitch.isChecked,
        excludePushupKneesFeedback = binding.excludePushupKneesFeedbackSwitch.isChecked,
        pauseTypes = selectedPauseTypes(),
        useWideAngleCamera = binding.useWideAngleCameraSwitch.isChecked,
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

    private fun SdkFeatureSettings.instructionVideoConfig(): InstructionVideoConfig =
        InstructionVideoConfig(
            displayMode = if (mediumCycleInstructionVideo) {
                VideoDisplayMode.MEDIUM_CYCLE
            } else {
                VideoDisplayMode.DEFAULT
            },
            mediumSizeCycles = instructionVideoCycles,
        )

    private fun selectedPauseTypes(): Set<PauseDialogTypes> = buildSet {
        if (binding.pauseResumeSwitch.isChecked) add(PauseDialogTypes.Resume)
        if (binding.pauseSkipSwitch.isChecked) add(PauseDialogTypes.Skip)
        if (binding.pauseStartOverSwitch.isChecked) add(PauseDialogTypes.StartOver)
        if (binding.pauseQuitSwitch.isChecked) add(PauseDialogTypes.Quit)
        if (binding.pauseSwitchSwitch.isChecked) add(PauseDialogTypes.Switch)
    }.ifEmpty { setOf(PauseDialogTypes.Resume) }

    private fun com.sency.smkitui.model.SkeletonSettings.applyDemoSettings(settings: SdkFeatureSettings) {
        skeletonHidden = settings.skeletonHidden
        skeletonPreset = settings.skeletonPreset
        skeletonConnectionStyle = settings.skeletonConnectionStyle
        skeletonJointShape = settings.skeletonJointShape
        skeletonDotsOpacity = settings.skeletonDotsOpacity
        skeletonConnectionsOpacity = settings.skeletonConnectionsOpacity
        skeletonDotsInnerColorOption = settings.skeletonDotsInnerColor
        skeletonDotsOuterColorOption = settings.skeletonDotsOuterColor
        skeletonConnectionsInnerColorOption = settings.skeletonConnectionsInnerColor
        skeletonConnectionsOuterColorOption = settings.skeletonConnectionsOuterColor
        skeletonDotsGlow = settings.skeletonDotsGlow
        skeletonConnectionsGlow = settings.skeletonConnectionsGlow
        skeletonLineWidthScale = settings.skeletonLineWidthScale
        skeletonOutlineScale = settings.skeletonOutlineScale
        skeletonSoftness = settings.skeletonSoftness
        skeletonAnimationDurationSeconds = settings.skeletonAnimationDurationSeconds
    }

    private fun Spinner.selectedSkeletonColor(): SkeletonColorOption? =
        selectedItemPosition.takeIf { it > 0 }?.let { SkeletonColorOption.values()[it - 1] }

    private fun View.preferenceKey(): String = resources.getResourceEntryName(id)

    private fun String.toDisplayName(): String =
        lowercase(Locale.ROOT).replace('_', ' ').replaceFirstChar(Char::uppercase)

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
