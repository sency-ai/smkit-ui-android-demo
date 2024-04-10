package com.sency.smkitui.demo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.sency.smkitui.demo.ui.theme.SMKitUIAndroidDemoTheme

class MainActivity : ComponentActivity(), SMKitUIWorkoutListener {

    private val smKitUI = (application as SMKitUIDemoApplication).smKitUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMKitUIAndroidDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Button(onClick = { start() }) {
                        Text(text = "Start")
                    }
                    Button(onClick = { startAssessment() }) {
                        Text(text = "Start Assessment")
                    }
                }
            }
        }
    }

    /**
     *  If there is an error during runtime, it will be received here.
     */
    override fun handleWorkoutErrors(error: Error) = Unit

    /**
     *  When the user finishes the workout, this function will be called with a summary.
     */
    override fun workoutDidFinish(summary: WorkoutSummaryData) {

    }

    /**
     *  When the user exits the workout before finishing, this function will be called with a summary.
     */
    override fun didExitWorkout(summary: WorkoutSummaryData) {

    }

    private fun start() {
        val intro = applicationContext.getRawResourceUri(R.raw.customWorkoutIntro)
        val soundtrack = applicationContext.getRawResourceUri(R.raw.fullBodyLong)
        val exercises: List<SMExercise> = listOf(
            SMExercise(
                name = "High Knees",
                exerciseIntro = null, // Custom sound,
                totalSeconds = 30,
                introSeconds = 5,
                videoInstruction = applicationContext.getRawResourceUri(R.raw.HighKnees),
                uiElements = setOf(UiElement.RepsCounter, UiElement.Timer),
                detector = "HighKnees",
                repBased = true,
                exerciseClosure = null // Custom sound
            ),
            SMExercise(
                name = "Squat Regular Static",
                exerciseIntro = null, // Custom sound,
                totalSeconds = 30,
                introSeconds = 5,
                videoInstruction = applicationContext.getRawResourceUri(R.raw.SquatRegularStatic),
                uiElements = setOf(UiElement.GaugeOfMotion, UiElement.Timer),
                detector = "SquatRegularStatic",
                repBased = false,
                exerciseClosure = null // Custom sound
            ),
            SMExercise(
                name = "Plank High Static",
                exerciseIntro = null, // Custom sound,
                totalSeconds = 30,
                introSeconds = 5,
                videoInstruction = applicationContext.getRawResourceUri(R.raw.PlankHighStatic),
                uiElements = setOf(UiElement.GaugeOfMotion, UiElement.Timer),
                detector = "PlankHighStatic",
                repBased = false,
                exerciseClosure = null // Custom sound
            ),
        )
        val smWorkout = SMWorkout(
            id = "",
            name = "TEST",
            workoutIntro = intro,
            soundtrack = soundtrack,
            exercises = exercises,
            workoutClosure = null // Custom sound
        )

        try {
            smKitUI.startWorkout(smWorkout, this)
        } catch (e: Exception) {
            Log.e("MainActivity", "startAssessment: ")
        }
    }

    private fun startAssessment() {
        try {
            smKitUI.startAssessment(this)
        } catch (e: Exception) {
            Log.e("MainActivity", "startAssessment: ")
        }
    }

    private fun Context.getRawResourceUri(@RawRes rawResourceId: Int): Uri {
        return Uri.parse("android.resource://${packageName}/$rawResourceId")
    }
}