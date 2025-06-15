
# SessionOptions.md

This guide explains how to configure session-related options using the `smKitUI` SDK. These settings allow you to customize the workout experience and user interface behavior.

---

## 🔘 Pause Dialog Options

Control which actions are available when a user pauses a session.

### ✅ Example

```kotlin
import com.sency.smkitui.presentation.fragment.PauseDialogTypes

configureViewModel.smKitUI?.setPauseTypes(
    arrayOf(
        PauseDialogTypes.Quit,
        PauseDialogTypes.Resume
    )
)
```

### 🧩 Available Options

```kotlin
enum class PauseDialogTypes {
    Resume,
    Skip,
    StartOver,
    Quit
}
```

> You can pass any combination of these enum values to display different buttons in the pause dialog.

---

## 🔢 Counter Preferences

Customize how repetition counting works during a workout.

### ✅ Example

```kotlin
import com.sency.smkitui.model.workoutConfig.CounterPreference

configureViewModel.smKitUI?.setCounterPreferences(
    if (isCounterPrefs) {
        CounterPreference.PerfectOnly
    } else {
        CounterPreference.Default
    }
)
```

### 🧩 Available Options

```kotlin
enum class CounterPreference {
    Default,       // Counts all valid reps
    PerfectOnly    // Counts only reps classified as "perfect"
}
```

> Use this setting to emphasize rep quality or allow all valid reps.

---

## 🏁 End Exercise Preferences

Define how exercises should conclude — either automatically or based on a specific target.

### ✅ Example

```kotlin
import com.sency.smkitui.model.workoutConfig.EndExercisePreference

configureViewModel.smKitUI?.setEndExercisePreferences(
    if (endExercisePrefs) {
        EndExercisePreference.TargetBased
    } else {
        EndExercisePreference.Default
    }
)
```

### 🧩 Available Options

```kotlin
enum class EndExercisePreference {
    Default,        // Ends based on session duration or default logic
    TargetBased     // Ends when the set rep/target is achieved
}
```

> This lets you tailor when a set or exercise is considered complete.

---

## 📌 Summary Table

| Feature              | Enum                        | Description                                       |
|----------------------|-----------------------------|---------------------------------------------------|
| Pause UI Options     | `PauseDialogTypes`          | Controls what buttons appear when session pauses  |
| Counter Preferences  | `CounterPreference`         | Defines which reps are counted                    |
| End Exercise Logic   | `EndExercisePreference`     | Determines when an exercise finishes              |

> 🧠 Combine these configurations to create a personalized and guided workout experience.
