package com.example.smkituidemoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sency.smkit.PoseModelChoice
import com.sency.smkitui.data.entity.ScoringParams
import com.sency.smkitui.data.entity.UiElement
import com.sency.smkitui.model.SMExercise
import com.sency.smkitui.model.SMExerciseDisplayContext
import com.sency.smkitui.model.SMExerciseDisplayGroup
import com.sency.smkitui.model.SMExerciseDisplayGroupKind
import com.sency.smkitui.model.SMPositionRepConfig
import com.sency.smkitui.model.SMStretchSetConfig
import com.sency.smkitui.model.SkeletonColorOption
import com.sency.smkitui.model.SkeletonConnectionStyle
import com.sency.smkitui.model.SkeletonJointShape
import com.sency.smkitui.model.SkeletonPreset
import com.sency.smkitui.model.UIColorTheme
import com.sency.smkitui.model.smkitui.AssessmentType
import com.sency.smkitui.model.workoutConfig.SMLanguage
import com.sency.smkitui.presentation.fragment.PauseDialogTypes
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _configured = MutableLiveData(false)
    val configured: LiveData<Boolean>
        get() = _configured

    fun setConfigured(configured: Boolean) {
        viewModelScope.launch {
            _configured.postValue(configured)
        }
    }

    fun exercies() = listOf(
        SMExercise(
            prettyName = "Air Squat",
            totalSeconds = 60,
            videoInstruction = "SquatRegular",
            uiElements = setOf(UiElement.timer, UiElement.repsCounter),
            detector = "SquatRegular",
            scoringParams = ScoringParams(
                targetReps = 10,
                scoreFactor = 0.5f,
                passCriteria = null,
                targetRom = null,
                targetTime = null,
                type = "reps"
            ),
            summaryMainMetricTitle = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summaryMainMetricSubTitle = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            closureFailedSound = null,
            side = "right"
        ),
        SMExercise(
            prettyName = "Standing Knee Raise Right",
            totalSeconds = 60,
            videoInstruction = "StandingKneeRaiseRight",
            uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
            detector = "StandingKneeRaiseRight",
            scoringParams = ScoringParams(
                targetReps = null,
                scoreFactor = 0.5f,
                passCriteria = null,
                targetRom = "StandingKneeRaiseElevation",
                targetTime = null,
                type = "rom"
            ),

            summaryMainMetricTitle = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summaryMainMetricSubTitle = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            closureFailedSound = null,
            side = "right"
        ),
        SMExercise(
            prettyName = "Standing Knee Raise Left",
            totalSeconds = 60,
            videoInstruction = "StandingKneeRaiseLeft",
            uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
            detector = "StandingKneeRaiseLeft",
            scoringParams = ScoringParams(
                targetReps = null,
                scoreFactor = 0.5f,
                passCriteria = null,
                targetRom = "StandingKneeRaiseElevation",
                targetTime = null,
                type = "rom"
            ),

            summaryMainMetricTitle = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summaryMainMetricSubTitle = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            closureFailedSound = null,
            side = "left"
        ),
        SMExercise(
            prettyName = "Squat Regular Overhead Static",
            totalSeconds = 20,
            videoInstruction = "SquatRegularOverheadStatic",
            uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
            detector = "SquatRegularOverheadStatic",
            scoringParams = ScoringParams(
                targetReps = 20,
                scoreFactor = 0.9f,
                passCriteria = null,
                targetRom = null,
                targetTime = 10,
                type = "time"
            ),

            summaryMainMetricTitle = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summaryMainMetricSubTitle = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            closureFailedSound = null,
            side = null
        ),
    )

    fun workoutExercises(): List<SMExercise> =
        exercies().map { exercise ->
            exercise.copy(scoringParams = null)
        }

    fun continuationExercises(): List<SMExercise> =
        workoutExercises().take(2).map { exercise ->
            exercise.copy(
                shortIntro = true,
                playPreExerciseCountdown = true,
                guidanceMode = false,
            )
        }

    fun sdkFeatureDetectors(): List<String> =
        sdkDetectorCatalog

    fun sdkFeatureWorkoutExercises(
        detectors: List<String>,
        settings: SdkFeatureSettings,
    ): List<SMExercise> =
        detectors.mapIndexed { index, detector ->
            val uiElements = sdkUiElements(detector)
            val supportsTargetReps =
                UiElement.repsCounter in uiElements && UiElement.gaugeOfMotion !in uiElements
            SMExercise(
                prettyName = detector.toPrettyName(),
                totalSeconds = if (UiElement.repsCounter in uiElements) 45 else 30,
                videoInstruction = detector,
                uiElements = uiElements,
                detector = detector,
                scoringParams = if (settings.showTargetProgress && supportsTargetReps) {
                    ScoringParams(
                        targetReps = 10,
                        scoreFactor = 1f,
                        passCriteria = null,
                        targetRom = null,
                        targetTime = null,
                        type = "reps",
                    )
                } else {
                    null
                },
                summaryMainMetricTitle = "Result",
                summaryTitle = detector.toPrettyName(),
                summarySubTitle = "SDK feature demo",
                summaryMainMetricSubTitle = "Completed",
                exerciseIntro = "",
                exerciseClosure = null,
                closureFailedSound = null,
                side = detector.side(),
                shortIntro = settings.shortIntro,
                playPreExerciseCountdown = settings.preExerciseCountdown,
                playSoundOnEachRep = settings.soundOnEachRep,
                playRepMilestoneVoice = settings.repMilestoneVoice,
                repMilestoneInterval = 10,
                playTargetRepsCompletionVoice = settings.targetRepsCompletionVoice,
                intentVoiceFeedbackEnabled = settings.intentVoiceFeedback,
                guidanceMode = null,
                enableGuidanceModeSuggestion = settings.guidanceModeSuggestion,
                enableSmallBodyPartFocus = settings.smallBodyPartFocus,
                adaptiveRomFeedbackEnabled = settings.adaptiveRomFeedback,
                adaptiveRomWarmupReps = 2,
                stretchSetConfig = if (settings.stretchSetConfig && UiElement.holdingPosition in uiElements) {
                    SMStretchSetConfig(
                        repetitions = 3,
                        secondsPerStretch = 6,
                        restSecondsBetweenStretches = 2,
                        introSoundKey = "StretchSetStartStretch",
                    )
                } else {
                    null
                },
                showTargetProgress = settings.showTargetProgress && supportsTargetReps,
                internalInsightsKey = detector,
                positionRepConfig = if (
                    settings.positionReps && UiElement.holdingPosition in uiElements
                ) {
                    SMPositionRepConfig(targetReps = 3, secondsPerRep = 5)
                } else {
                    null
                },
            ).also { exercise ->
                exercise.useWideAngleCamera = settings.useWideAngleCamera && detector != "Rest"
                if (settings.exerciseProgressDisplay && detector != "Rest") {
                    exercise.displayContext = sdkDisplayContext(index)
                }
            }
        }

    private fun sdkDisplayContext(index: Int): SMExerciseDisplayContext =
        if (index < 2) {
            SMExerciseDisplayContext(sectionTitle = "WARM-UP", playsTitleSound = true)
        } else {
            val circuitNumber = ((index - 2) / 3) + 1
            SMExerciseDisplayContext(
                sectionTitle = "MAIN SET",
                playsTitleSound = true,
                group = SMExerciseDisplayGroup(
                    id = "demo-circuit-$circuitNumber",
                    kind = SMExerciseDisplayGroupKind.CIRCUIT,
                    number = circuitNumber,
                ),
            )
        }

    private fun String.toPrettyName(): String =
        replace(Regex("(?<=[a-z])(?=[A-Z])"), " ")

    private fun String.side(): String? = when {
        endsWith("Right") -> "right"
        endsWith("Left") -> "left"
        else -> null
    }

    private fun sdkUiElements(detector: String): Set<UiElement> {
        if (detector == "Rest") return setOf(UiElement.timer)
        if (detector in quickMotionDetectors) return setOf(UiElement.timer, UiElement.quickMotion)
        if (
            detector.contains("Mobility") ||
            detector.contains("KneeRaise") ||
            detector.contains("LegRaise") ||
            detector.contains("Curl") ||
            detector.contains("SideBend") ||
            detector.contains("SideLungeHold") ||
            detector.contains("TableTop") ||
            detector == "Squat" ||
            detector == "WallBall" ||
            detector.startsWith("Squat") ||
            detector.startsWith("LungeFront") ||
            detector.startsWith("Lateral")
        ) {
            return setOf(UiElement.timer, UiElement.repsCounter, UiElement.gaugeOfMotion)
        }
        if (
            detector.contains("Stretch") ||
            detector.contains("Hold") ||
            detector.contains("Static") ||
            detector.contains("Stance") ||
            detector == "HappyBaby" ||
            detector == "GroinAndAdductor" ||
            detector == "StandingForwardFold" ||
            detector == "WideInnerThighStretch"
        ) {
            return setOf(UiElement.timer, UiElement.holdingPosition)
        }
        return setOf(UiElement.timer, UiElement.repsCounter)
    }

    companion object {
        private val quickMotionDetectors = setOf(
            "ShoulderCircles",
            "SquatPulsing",
            "QuickFeet",
            "PogoJumps",
            "FastMarchRun",
            "PowerWalkInPlace",
        )

        private val sdkDetectorCatalog = listOf(
            "AirJumpRope",
            "AlternateWindmillToeTouch",
            "AnkleMobilityLeft",
            "AnkleMobilityRight",
            "BackSuperman",
            "BackSupermanHold",
            "BirdDog",
            "Burpees",
            "ButtKicks",
            "CalfStretchLungePositionRight",
            "CalfStretchLungePositionLeft",
            "ClamshellsRight",
            "ClamshellsLeft",
            "Crunches",
            "Dips",
            "DownwardDogStretch",
            "DownwardDogPrayerStretch",
            "Froggers",
            "GlutesBridge",
            "GlutesBridgeHold",
            "GunDraw",
            "GroinAndAdductor",
            "GlutesStretchOnTheFloorRight",
            "GlutesStretchOnTheFloorLeft",
            "HamstringMobility",
            "HandGrip",
            "HappyBaby",
            "HangingLegRaise",
            "HangingLegRaiseHold",
            "HangingKneeRaise",
            "HangingKneeRaiseHold",
            "HighKnees",
            "HollowHold",
            "HipExternalRotationRight",
            "HipExternalRotationLeft",
            "HipExternalRotationFigureFourStretchRight",
            "HipExternalRotationFigureFourStretchLeft",
            "HipFlexionRight",
            "HipFlexionLeft",
            "HipFlexorStretchRight",
            "HipFlexorStretchLeft",
            "HipFlexorLungeStretchRight",
            "HipFlexorLungeStretchLeft",
            "HipInternalRotationRight",
            "HipInternalRotationLeft",
            "InnerThighMobility",
            "InternalRotationSideStretchRight",
            "InternalRotationSideStretchLeft",
            "JeffersonCurl",
            "JumpingJacks",
            "Jumps",
            "KravMagaJabCross",
            "KravMagaStanceStatic",
            "KneelingQuadStretchRight",
            "KneelingQuadStretchLeft",
            "LateralHandRaise",
            "LateralHandRaiseRight",
            "LateralHandRaiseLeft",
            "LateralRaises",
            "LungeJumps",
            "LungePlyo",
            "LungeFront",
            "LungeFrontLeft",
            "LungeFrontRight",
            "LungeFrontAlternate",
            "LungeRegularStatic",
            "LungeRegularStaticLeft",
            "LungeRegularStaticRight",
            "LungeSide",
            "LungeSideLeft",
            "LungeSideRight",
            "LungeSideStaticLeft",
            "LungeSideStaticRight",
            "LumbarCatSeated",
            "LumbarCamelSeated",
            "LumbarCamelCatSeated",
            "OverheadMobility",
            "LatStretchRight",
            "LatStretchLeft",
            "LumbarRotationsSeatedRight",
            "LumbarRotationsSeatedLeft",
            "RhomboidStretch",
            "ShoulderCircles",
            "SingleLegStanceRight",
            "SingleLegStanceLeft",
            "SingleLegHamstringStretchRight",
            "SingleLegHamstringStretchLeft",
            "SeatedBowArrowThoracicMobilityRight",
            "SeatedBowArrowThoracicMobilityLeft",
            "SeatedThoracicSideBendingRight",
            "SeatedThoracicSideBendingLeft",
            "SeatedHipRotationsRight",
            "SeatedHipRotationsLeft",
            "PlankCommando",
            "PlankHighShoulderTaps",
            "PlankSideLowStatic",
            "PlankSideLowStaticRight",
            "PlankSideLowStaticLeft",
            "PlankSideHighStatic",
            "PlankSideHighStaticRight",
            "PlankSideHighStaticLeft",
            "PlankHighToeTaps",
            "PlankJacksHigh",
            "PlankWalkouts",
            "PlankHighStatic",
            "PlankLowStatic",
            "PlankLowHipTwist",
            "PrayerStretch",
            "PullupsPronated",
            "PullupsSupinated",
            "PushupKnees",
            "PushupKneesRegular",
            "PushupKneesWide",
            "PushupKneesNarrow",
            "PushupNarrow",
            "PushupRegular",
            "PushupWide",
            "Pushups",
            "Rowing",
            "ReverseSitToTableTop",
            "ReverseTableTopHold",
            "ShouldersPress",
            "SingleHandOverheadHealDigs",
            "SideLunge",
            "SideLungeHoldRight",
            "SideLungeHoldLeft",
            "SideStepJacks",
            "SkaterHops",
            "SkiJumps",
            "Skydivers",
            "SkydiversHold",
            "Squat",
            "WallBall",
            "SquatPulsing",
            "SitToStand",
            "SitupPenguin",
            "SitupRussianTwist",
            "SitupRussianTwistStatic",
            "SquatAndKick",
            "SquatAndRotationJab",
            "SquatRegular",
            "SquatRegularOverhead",
            "SquatRegularOverheadStatic",
            "SquatRegularStatic",
            "SquatAndStep",
            "SquatNarrow",
            "SquatSumo",
            "SquatSumoStatic",
            "StandingAlternateToeTouch",
            "StandingBicycleCrunches",
            "StandingBowArrowThoracicMobilityRight",
            "StandingBowArrowThoracicMobilityLeft",
            "StandingHamstringMobility",
            "StandingKneeRaiseLeft",
            "StandingKneeRaiseRight",
            "StandingObliqueCrunches",
            "StandingForwardFold",
            "StandingSideBendLeft",
            "StandingSideBendRight",
            "StandingStepReverseAirFly",
            "StandingThoracicSideBendingRight",
            "StandingThoracicSideBendingLeft",
            "TableTopHold",
            "ToesToBar",
            "TuckHold",
            "QuadThoraticRotation",
            "QuadThoraticRotationRight",
            "QuadThoraticRotationLeft",
            "QuickFeet",
            "PogoJumps",
            "WideInnerThighStretch",
            "FastMarchRun",
            "PowerWalkInPlace",
            "ToesRaises",
            "CalfRaises",
            "ShadowBoxingBounce",
            "SeatedShadowBoxing",
            "DeadHangPronated",
            "DeadHangSupinated",
            "Rest",
        )
    }
}

data class SdkFeatureSettings(
    val assessmentType: AssessmentType,
    val showPhoneCalibration: Boolean,
    val poseModelChoice: PoseModelChoice,
    val colorTheme: UIColorTheme,
    val sessionLanguage: SMLanguage,
    val phoneCalibrationLanguage: SMLanguage,
    val skeletonHidden: Boolean,
    val skeletonPreset: SkeletonPreset,
    val skeletonConnectionStyle: SkeletonConnectionStyle,
    val skeletonJointShape: SkeletonJointShape,
    val skeletonDotsInnerColor: SkeletonColorOption?,
    val skeletonDotsOuterColor: SkeletonColorOption?,
    val skeletonConnectionsInnerColor: SkeletonColorOption?,
    val skeletonConnectionsOuterColor: SkeletonColorOption?,
    val skeletonDotsOpacity: Float,
    val skeletonConnectionsOpacity: Float,
    val skeletonDotsGlow: Float,
    val skeletonConnectionsGlow: Float,
    val skeletonLineWidthScale: Float,
    val skeletonOutlineScale: Float,
    val skeletonSoftness: Float,
    val skeletonAnimationDurationSeconds: Float,
    val useDefaultGuidanceMode: Boolean,
    val guidanceModeSuggestion: Boolean,
    val guidanceDebugLogging: Boolean,
    val smallBodyPartFocus: Boolean,
    val variationMismatchFeedback: Boolean,
    val phoneMovementPrevention: Boolean,
    val startTimerOnFirstActivity: Boolean,
    val exerciseSummaryTimingMetrics: Boolean,
    val includeAssessmentInsights: Boolean,
    val exportAssessmentInsights: Boolean,
    val perfectOnlyCounter: Boolean,
    val targetBasedCompletion: Boolean,
    val mediumCycleInstructionVideo: Boolean,
    val instructionVideoCycles: Int,
    val workoutContinuationTimerSeconds: Int,
    val playPhoneCalibrationAudio: Boolean,
    val playBodyCalibrationAudio: Boolean,
    val allowAudioMixing: Boolean,
    val showExternalAudioControl: Boolean,
    val enableButtonTutorial: Boolean,
    val intelligenceRest: Boolean,
    val excludePushupKneesFeedback: Boolean,
    val pauseTypes: Set<PauseDialogTypes>,
    val useWideAngleCamera: Boolean,
    val shortIntro: Boolean,
    val preExerciseCountdown: Boolean,
    val soundOnEachRep: Boolean,
    val repMilestoneVoice: Boolean,
    val targetRepsCompletionVoice: Boolean,
    val intentVoiceFeedback: Boolean,
    val showTargetProgress: Boolean,
    val adaptiveRomFeedback: Boolean,
    val stretchSetConfig: Boolean,
    val positionReps: Boolean,
    val exerciseProgressDisplay: Boolean,
)
