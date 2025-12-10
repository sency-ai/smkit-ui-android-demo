# SMKitUI Android SDK: Assessment

This guide covers the full flow for starting a Sency Blueprint Assessment and handling results.

## Overview
Sency Blueprint Assessments are standardized tests developed by Sency’s medical and fitness experts. You can also run custom assessments (see CustomizedAssessment.md).

## Prerequisites
- SDK configured (see README.md)
- CAMERA permission in AndroidManifest.xml

## Starting an Assessment
```kotlin
smKitUI.startAssessment(
    assessmentType = Fitness, // or 360, Strength, Cardio, Custom
    listener = object: SMKitUIWorkoutListener {
        override fun handleWorkoutErrors(error: Error) { /* handle error */ }
        override fun workoutDidFinish(summary: WorkoutSummaryData) { /* handle summary */ }
        override fun didExitWorkout(summary: WorkoutSummaryData) { /* handle exit */ }
        override fun exerciseDidFinish(data: ExerciseData) { /* handle exercise end */ }
    },
    userData = UserData(age = 28, gender = Gender.Female), // optional
    showSummary = true // show summary screen
)
```

## 🔧 Modifying Feedback Parameters <a name="modify"></a>

You have the ability to modify specific feedback parameters for exercises.
This allows you to customize the thresholds and ranges for feedback detection.

To modify feedback parameters, use the following example:

```kotlin
val modifications = """
{
    "Crunches": {
        "CrunchesShallowDepth": {
            "low": 0.25,
            "high": 0.75
        }
    }
}
""".trimIndent()

smKitUI.startAssessment(
    assessmentType = Fitness,
    listener = myListener,
    userData = null,
    showSummary = true,
    modifications = modifications
)
```

## Assessment Types
| Name      | Description | More info |
|-----------|-------------|-----------|
| Fitness   | Physical abilities, strength, endurance | [Fitness](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/AI-Fitness-Assessment.md) |
| 360       | Preventative/medical support | [360](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/360-Body-Assessment.md) |
| Strength  | Core/endurance strength | [Strength](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/Strength.md) |
| Cardio    | Cardiovascular | [Cardio](https://github.com/sency-ai/smkit-sdk/blob/main/Assessments/Cardio.md) |
| Custom    | Tailored by Sency | |

## Result Handling
- `workoutDidFinish(summary: WorkoutSummaryData)` — called when assessment ends
- `didExitWorkout(summary: WorkoutSummaryData)` — called if user exits early
- See DataTypes.md for result structure

## Custom Assessment
If Sency provided a custom key:
```kotlin
smKitUI.startAssessment(
    assessmentType = Custom("YOUR_CUSTOM_ID"),
    listener = myListener,
    userData = null,
    showSummary = false
)
```

## Troubleshooting
- Always call `configure` before starting an assessment
- Contact support@sency.ai for help