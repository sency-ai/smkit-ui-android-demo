# Assessment

> Sency offers two primary types of assessments: Sency Blueprint assessments and Customized assessments.

> **Sency Blueprint Assessments:** Developed in collaboration with Sency’s medical and fitness experts, these assessments provide a standardized, professional way to measure core aspects of movement, fitness, and a healthy lifestyle. Simply follow the "start assessment" instructions and select the [type of assessment](#assessment-types) you need.

> **Sency Customized Assessments:** For those who prefer to build their own assessments, you can create a customized evaluation using the exercises and movements from our movement catalog, according to your specific requirements (check the CustomizedAssessment.md file for more info).

**Start Assessment**
Passing `UserData` programmatically is optional, not passing will display the 'User Details' Fragment.
```Kotlin
fun startAssessment() {
    try {
        // In order to start Sency's blueprint assessment
        smKitUI.startAssessment(
            assessmentType = Fitness, // Assessment Type
            listener = object: SMKitUIWorkoutListener {
                // Runtime error callback
                override fun handleWorkoutErrors(error: Error) {}
    
                // Workout session ended successfully callback
                override fun workoutDidFinish(summary: WorkoutSummaryData) {}
    
                // When user manually exit the workout callback
                override fun didExitWorkout(summary: WorkoutSummaryData) {}
    
                // When exercise is finished
                override fun exerciseDidFinish(data: ExerciseData) {}
            }, // Assessment Listener
            userData = UserData( // User Data Optional 
                age = 28,
                gender = Gender.Female,
                email = null
            ) ,
            showSummary = true // show summary screen for the assessment
        )
    } catch (e: Exception) {
        println("startAssessment: $e")
    }
}
```

### Start Custom Assessment
If Sency provided you with a special key, not auth-key, please use it here. 

```Kotlin
fun startAssessment() {
    try {
        // In order to start your own custom assessment
        smKitUI.startAssessment(
            assessmentType = Custom("YOUR_CUSTOM_ID"), 
            listener = myListener, // SMKitUIWorkoutListener 
            userData = null,
            showSummary = false,
        )
    } catch (e: Exception) {
        println("startAssessment: $e")
    }
}
```

### AssessmentTypes <a name="assessment-types"></a>
| Name                | Description |More info|
|---------------------|---------------------|---------------------|
| Fitness             | For individuals of any activity level who seek to enhance their physical abilities, strength, and endurance through a tailored plan.| [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/AI-Fitness-Assessment.md) |
| 360                 | Designed for individuals of any age and activity level, this assessment determines the need for a preventative plan or medical support.| [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/360-Body-Assessment.md) |
| Strength            |For individuals of any activity level who seek to assess their strength capabilities (core and endurance) | [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/Strength.md) |
| Cardio            |For individuals of any activity level who seek to assess their cardiovascular capabilities | [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/Cardio.md) |
| Custom              |If Sency created a tailored assessment for you, you probably know it, and you should use this enum.|  |
