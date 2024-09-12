# Assessment

Implement **SMKitUIWorkoutListener**.
```Kotlin
val listener = object:  SMKitUIWorkoutListener {
    // Runtime error callback
    override fun handleWorkoutErrors(error: Error) {
    }

    // Workout session end callback
    override fun workoutDidFinish(summary: WorkoutSummaryData) {
    }

    // Exit workout callback
    override fun didExitWorkout(summary: WorkoutSummaryData) {
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
            assessmentType = Fitness, // Assessment Type
            listener = listener, // Assessment Listener
            userData = UserData( // User Data Optional 
                age = 28,
                gender = Gender.Female,
                email = null
            ) 
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
        smKitUI.startAssessment(
            Custom("YOUR_CUSTOM_ID"), 
            listener = this, 
            userData = null
        )
    } catch (e: Exception) {
        println("startAssessment: $e")
    }
}
```

### AssessmentTypes
| Name                | Description |More info|
|---------------------|---------------------|---------------------|
| Fitness             | For individuals of any activity level who seek to enhance their physical abilities, strength, and endurance through a tailored plan.| [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/AI-Fitness-Assessment.md) |
| 360                 | Designed for individuals of any age and activity level, this assessment determines the need for a preventative plan or medical support.| [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/360-Body-Assessment.md) |
| Strength            |For individuals of any activity level who seek to assess their strength capabilities (core and endurance) * This assessment will be available soon. Contact us for more info.| [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/Strength.md) |
| Cardio            |For individuals of any activity level who seek to assess their cardiovascular capabilities  * This assessment will be available soon. Contact us for more info.| [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/Cardio.md) |
| Custom              |If Sency created a tailored assessment for you, you probably know it, and you should use this enum.|  |
