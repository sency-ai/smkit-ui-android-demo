package com.example.smkituidemoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sency.smkitui.data.entity.ScoringParams
import com.sency.smkitui.data.entity.UiElement
import com.sency.smkitui.model.SMExercise
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
            prettyName = "Lunge Front Right",
            totalSeconds = 60,
            videoInstruction = "LungeFrontRight",
            uiElements = setOf(UiElement.timer, UiElement.repsCounter),
            detector = "LungeSideRight",
            scoringParams = ScoringParams(
                targetReps = 54,
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
            side = "right"
        ),
        SMExercise(
            prettyName = "Lunge Front Left",
            totalSeconds = 60,
            videoInstruction = "LungeFrontLeft",
            uiElements = setOf(UiElement.timer, UiElement.repsCounter),
            detector = "LungeSideLeft",
            scoringParams = ScoringParams(
                targetReps = 54,
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
            side = "left"
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
            side = null
        ),
    )

}