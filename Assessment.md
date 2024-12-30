# Assessment

> Sency offers two primary types of assessments: Sency Blueprint assessments and Customized assessments.

> **Sency's Blueprint Assessments:** Developed in collaboration with Sency’s medical and fitness experts, these assessments provide a standardized, professional way to measure core aspects of movement, fitness, and a healthy lifestyle. Simply follow the "start assessment" instructions and select the [type of assessment](#assessment-types) you need.

> **Sency's Custom Assessments:** For those who prefer sency to create their Blueprint assessment, please contact our Service Center.

### Start Blueprint Assessments

**SMKitUIWorkoutListener** First Implement an Assessment Listener
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
**Start Assessment** Now you can starts one of Sency's [blueprint assessments](#assessment-types)
Passing `UserData` programmatically is optional, not passing will display the 'User Details' Fragment.
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
            listener = this, 
            userData = null,
            showSummary = true,
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
| Strength            |For individuals of any activity level who seek to assess their strength capabilities (core and endurance) * This assessment will be available soon. Contact us for more info.| [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/Strength.md) |
| Cardio            |For individuals of any activity level who seek to assess their cardiovascular capabilities  * This assessment will be available soon. Contact us for more info.| [Link](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/Cardio.md) |
| Custom              |If Sency created a tailored assessment for you, you probably know it, and you should use this enum.|  |
