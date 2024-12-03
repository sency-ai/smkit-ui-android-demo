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
        SMExercise( //2
            prettyName = "Lunge Front Right",
            totalSeconds = 60,
            introSeconds = 5,
            videoInstruction = "LungeFrontRight",
            uiElements = setOf(UiElement.timer, UiElement.repsCounter),
            detector = "LungeSideRight",
            repBased = true,
            scoringParams = ScoringParams(
                targetReps = 54,
                scoreFactor = 0.5f,
                passCriteria = null,
                targetRom = null,
                targetTime = null,
                type = "reps"
            ),
            summaryTitleMainMetric = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summarySubTitleMainMetric = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            side = "right"
        ),
        SMExercise( //3
            prettyName = "Lunge Front Left",
            totalSeconds = 60,
            introSeconds = 5,
            videoInstruction = "LungeFrontLeft",
            uiElements = setOf(UiElement.timer, UiElement.repsCounter),
            detector = "LungeSideLeft",
            repBased = true,
            scoringParams = ScoringParams(
                targetReps = 54,
                scoreFactor = 0.5f,
                passCriteria = null,
                targetRom = null,
                targetTime = null,
                type = "reps"
            ),
            summaryTitleMainMetric = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summarySubTitleMainMetric = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            side = "left"
        ),
        SMExercise( //4
            prettyName = "Standing Knee Raise Right",
            totalSeconds = 60,
            introSeconds = 5,
            videoInstruction = "StandingKneeRaiseRight",
            uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
            detector = "StandingKneeRaiseRight",
            repBased = false,
            scoringParams = ScoringParams(
                targetReps = null,
                scoreFactor = 0.5f,
                passCriteria = null,
                targetRom = "StandingKneeRaiseElevation",
                targetTime = null,
                type = "rom"
            ),
            summaryTitleMainMetric = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summarySubTitleMainMetric = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            side = "right"
        ),
        SMExercise( //5
            prettyName = "Standing Knee Raise Left",
            totalSeconds = 60,
            introSeconds = 5,
            videoInstruction = "StandingKneeRaiseLeft",
            uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
            detector = "StandingKneeRaiseLeft",
            repBased = false,
            scoringParams = ScoringParams(
                targetReps = null,
                scoreFactor = 0.5f,
                passCriteria = null,
                targetRom = "StandingKneeRaiseElevation",
                targetTime = null,
                type = "rom"
            ),
            summaryTitleMainMetric = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summarySubTitleMainMetric = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            side = "left"
        ),
        SMExercise( //6
            prettyName = "Squat Regular Overhead Static",
            totalSeconds = 20,
            introSeconds = 5,
            videoInstruction = "SquatRegularOverheadStatic",
            uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
            detector = "SquatRegularOverheadStatic",
            repBased = true,
            scoringParams = ScoringParams(
                targetReps = 20,
                scoreFactor = 0.9f,
                passCriteria = null,
                targetRom = null,
                targetTime = 10,
                type = "time"
            ),
            summaryTitleMainMetric = "Info for the SummaryPage",
            summaryTitle = "Info for the SummaryPage",
            summarySubTitle = "Info for the SummaryPage",
            summarySubTitleMainMetric = "Info for the SummaryPage",
            exerciseIntro = "YOUR_ASSET",
            exerciseClosure = "YOUR_ASSET",
            side = null
        ),
    )

}