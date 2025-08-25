# SMKitUI Android SDK: Session Options

This guide explains all session-related configuration options, with full flow and result handling.

## Overview
Session options let you customize the workout experience and UI behavior.

## Pause Dialog Options
Control which actions are available when a user pauses a session.
```kotlin
configureViewModel.smKitUI?.setPauseTypes(
    arrayOf(
        PauseDialogTypes.Quit,
        PauseDialogTypes.Resume
    )
)
```
Available options:
- Resume
- Skip
- StartOver
- Quit

## Counter Preferences
Customize how repetition counting works during a workout.
```kotlin
configureViewModel.smKitUI?.setCounterPreferences(
    CounterPreference.PerfectOnly // or Default
)
```
Available options:
- Default: Counts all valid reps
- PerfectOnly: Counts only "perfect" reps

## End Exercise Preferences
Define how exercises should conclude.
```kotlin
configureViewModel.smKitUI?.setEndExercisePreferences(
    EndExercisePreference.TargetBased // or Default
)
```
Available options:
- Default: Ends based on session duration or default logic
- TargetBased: Ends when the set rep/target is achieved

## Result Handling
All session options affect callbacks in your SMKitUIWorkoutListener:
- `workoutDidFinish(summary: WorkoutSummaryData)`
- `didExitWorkout(summary: WorkoutSummaryData)`
- `exerciseDidFinish(data: ExerciseData)`

See DataTypes.md for result structure.

## Troubleshooting
- Always call `configure` before starting a session
- Contact support@sency.ai for help
