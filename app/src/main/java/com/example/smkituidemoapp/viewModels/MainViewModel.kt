package com.example.smkituidemoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sency.smkitui.model.SMExercise
import com.sency.smkitui.model.ScoringParams
import com.sency.smkitui.model.UiElement
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
            prettyName = "Squat",
            exerciseIntro = "exerciseIntro_SquatRegular_60",
            totalSeconds = 60,
            introSeconds = 5,
            videoInstruction = "SquatRegularInstructionVideo",
            uiElements = setOf(UiElement.Timer, UiElement.GaugeOfMotion),
            detector = "SquatRegular",
            repBased = true,
            exerciseClosure = "exerciseClosure_0_0.mp3",
            scoringParams = ScoringParams(
                targetRom = null,
                targetTime = 0,
                scoreFactor = 0.5f,
                passCriteria = null,
                type = "time",
                targetReps = 60
            ),
        ),
        SMExercise(
            prettyName = "Push-ups",
            totalSeconds = 60,
            introSeconds = 8,
            exerciseIntro = "exerciseIntro_PushupRegular_60",
            videoInstruction = "PushupRegularInstructionVideo",
            uiElements = setOf(UiElement.RepsCounter, UiElement.Timer),
            detector = "PushupRegular",
            repBased = true,
            exerciseClosure = "",
            scoringParams = ScoringParams(
                targetRom = null,
                targetTime = 0,
                scoreFactor = 0.5f,
                passCriteria = null,
                type = "time",
                targetReps = 60
            ),
        )
    )

}