# Assessment

Implement **SMKitUIWorkoutListener**.
```Kotlin
class MainActivity : ComponentActivity(), SMKitUIWorkoutListener {
    // Runtime error callback
    override fun handleWorkoutErrors(error: Error) {
    }

    // Workout session end callback
    override fun workoutDidFinish(summary: WorkoutSummaryData) {
    }

    // Exit workout callback
    overrice fun didExitWorkout(summary: WorkoutSummaryData) {
    }
    
    // When exercise is finished
    override fun exerciseDidFinish(data: ExerciseData) {
    }
}
```

### Start Fitness Assessment
**startAssessment** starts one of Sency's blueprint assessments
```Kotlin
fun startAssessment() {
    try {
        // In order to start Sency's blueprint assessment
        smKitUI.startAssessment(
            Fitness, // Assessment Type
            listener = this, // Assessment Listener
            userData = UserData(age = 28, gender = Gender.Female) // User Data 
        )
    } catch (e: Exception) {
        println("startAssessment: $e")
    }
}
```
Passing User Data programmtically is optional, not passing will display User Details Page 

### Start Custom Assessment
**startAssessment** and **Custom** starts one of your own Custom Assessments.
```Kotlin
fun startAssessment() {
    try {
        // In order to start your own custom assessment
        smKitUI.startAssessment(Custom("YOUR_CUSTOM_ID"), listener = this, userData = null)
    } catch (e: Exception) {
        println("startAssessment: $e")
    }
}
```
