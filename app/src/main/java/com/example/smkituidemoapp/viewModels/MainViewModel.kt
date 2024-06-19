package com.example.smkituidemoapp.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sency.smkitui.model.SMExercise
import com.sency.smkitui.model.SMWorkout
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
            exerciseClosure = "exerciseClosure_0_2.mp3",
            targetReps = 60,
            targetTime = 0,
            scoreFactor = 0.5,
            passCriteria = null
        ),
        SMExercise(
            prettyName = "Plank",
            totalSeconds = 60,
            introSeconds = 8,
            exerciseIntro = "exerciseIntro_PlankHighStatic_60",
            videoInstruction = "PlankHighStaticInstructionVideo",
            uiElements = setOf(UiElement.GaugeOfMotion, UiElement.Timer),
            detector = "PlankHighStatic",
            repBased = false,
            exerciseClosure = "",
            targetReps = 60,
            targetTime = 0,
            scoreFactor = 0.5,
            passCriteria = null,
        )
    )

}