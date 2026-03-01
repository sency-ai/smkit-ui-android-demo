# SMKitUI Android Demo

Welcome to the Sency SMKitUI Android SDK demo project! This guide will help you get started quickly and provide links to detailed documentation for each SDK feature.

## 🚀 Quick Start
1. **Install the SDK**
2. **Configure your app**
3. **Start using SMKitUI features**

## 📦 Installation
Add Sency's Maven repo to your project-level `build.gradle`:
```groovy
allprojects {
    maven {
        url "https://artifacts.sency.ai/artifactory/release/"
    }
}
```
Add the dependency to your app-level `build.gradle`:
```groovy
dependencies {
    implementation 'com.sency.smkitui:smkitui:1.5.0'
}
```

## ⚙️ Setup & Prerequisites

### Basic Setup
- Add CAMERA permission to your `AndroidManifest.xml`
- Lock screen orientation to portrait for activities using SMKitUI
- Set `minSdk` to 26 in your app-level `build.gradle`

### 📱 Android 15 (16KB Page Size) Compatibility

SMKitUI v1.3.9+ is fully compatible with **Android 15's 16KB page size requirements**.

To ensure your project works with Android 15 devices, you must meet specific build configuration requirements including:
- Gradle 8.4+
- Android Gradle Plugin 8.0+
- Kotlin 2.0+
- Java 17 compatibility
- Target SDK 36

**📋 See [PREREQUISITES.md](./PREREQUISITES.md) for detailed setup instructions and requirements.**

The SDK automatically handles 16KB page size optimizations internally - no additional runtime configuration is needed beyond meeting the build prerequisites.

## 🔑 Configuration
Call `configure` on app launch:
```kotlin
val smKitUI: SMKitUI = SMKitUI.Configuration(context)
    .setUIKey("YOUR_KEY")
    .configure(object : SMKitUIConfigurationListener {
        override fun onSuccess() { /* Success */ }
        override fun onFailure() { /* Failure */ }
    })
```
> **SMKitUI will not work if you don't first call configure.**

## 🏁 Main Features & Guides
- [Start Assessment](./StartAssessment.md)
- [Start Customized Assessment](./StartCustomizedAssessment.md)
- [Start Customized Workout](./StartCustomizedWorkout.md)
- [Workout From Program](./StartWorkoutFromProgram.md)
- [Session Options](./SessionOptionsFull.md)
- [Data Types & Results](./DataTypes.md)

## 🧩 Advanced Topics
- [Session Options](./SessionOptionsFull.md)
- [Data Types](./DataTypes.md)

## 🎨 Customizing UI Colors

To change the UI color theme, use `UIColorTheme.current = UIColorTheme.BLUE` (available colors: BLUE, GREEN, PURPLE, ORANGE, SILVER, GOLD, PINK).

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
    modifications = modifications,
    showPhoneCalibration = true
)
```

**Note:** We will release our feedbacks catalog soon. Feel free to reach us for assistant in applying modifications.

## 🤖 MCP Server Access
- Cursor: add the server definition below to `~/.cursor/mcp.json` and reload Cursor.
  [Contact us](mailto:support@sency.ai) to receive your API key.

```json
{
  "mcpServers": {
    "smkitui": {
      "type": "streamable-http",
      "url": "https://sency-mcp-production.up.railway.app/mcp",
      "headers": {
        "X-API-Key": "Your-API-Key"
      }
    }
  }
}
```

- CLI: run
  ```npx @modelcontextprotocol/cli client http --url https://sency-mcp-production.up.railway.app/mcp --header "X-API-Key: Your-API-Key"```.

## 📝 Changelog

### Version 1.4.9
- ✅ Multiple new exercises — check our movement catalog
- 🖐️ Pause by hovering palm over pause-menu icons (gesture-driven selection, no tap required)
- 🧠 Intelligence rest and exercise modification suggestions — automatically detects fatigue and recommends in-session rest
- 🦴 Full skeleton visualisation customisation system (presets, connection styles, joint shapes, colors, glow, opacity, and more)

### Version 1.4.8
- The SDK automatically selects the best pose estimation model based on device capabilities - Pro for maximum accuracy, Lite and UltraLite for smooth real-time performance on lower-end devices.
- UI updates

### Version 1.4.3
- ✅ Added `showPhoneCalibration` parameter to all workout/assessment methods (default: true)
- 📱 Phone calibration screen can now be controlled via API

### Version 1.4.2
- ✅ Multiple new exercises - check our movement catalog
- 🚀 Customization in exercise feedbacks made possible

### Version 1.3.9
- ✅ **Android 15 (16KB Page Size) Compatibility**: Full support for Android 15's 16KB page size requirements
- 🔧 Updated native libraries optimized for 16KB page alignment
- 🚀 Enhanced stability and performance across all Android versions
- ⚙️ Requires Gradle 8.4+, AGP 8.0+, and Kotlin 2.0+ for full Android 15 support

## ⚙️ Advanced Configuration (1.4.9) <a name="advanced"></a>

These properties must be set **before** starting a session.

### Intelligence / Fatigue Detection
```kotlin
smKitUI.setIntelligenceRestEnabled(true)  // Enable in-session rest suggestions based on fatigue
```

### Configurable Pause Menu
Choose which buttons appear on the pause overlay:
```kotlin
smKitUI.setPauseTypes(arrayOf(
    PauseDialogTypes.Resume,
    PauseDialogTypes.Skip,
    PauseDialogTypes.Quit
))
```

### `PauseDialogTypes`
| Type       | Description                          |
|------------|--------------------------------------|
| Resume     | Resume the workout                   |
| StartOver  | Restart the current exercise         |
| Skip       | Skip to the next exercise            |
| Quit       | Quit the session                     |
| Rest       | Take a rest                          |
| Switch     | Switch exercise variant              |

> **Note:** Pause buttons are activated by hovering your palm over the icon (gesture-based, ~1.5 s hold).

### Skeleton Visualisation
Use a preset for quick theming:
```kotlin
smKitUI.applySkeletonSettings {
    skeletonPreset = SkeletonPreset.NEON_GLOW
}
```

Or fine-tune individual properties:
```kotlin
smKitUI.applySkeletonSettings {
    skeletonHidden = false
    skeletonConnectionStyle = SkeletonConnectionStyle.SOLID   // NONE, DOTTED, DASHED, SOLID, LONG_DASHED, THIN_DOTS, DOT_DASHED, ROUNDED
    skeletonJointShape = SkeletonJointShape.CIRCLE            // CIRCLE, SQUARE, TRIANGLE, DIAMOND, STAR, HEXAGON
    skeletonDotsOpacity = 1.0f
    skeletonConnectionsOpacity = 0.8f
    skeletonDotsGlow = 0.5f
    skeletonConnectionsGlow = 0.3f
    skeletonLineWidthScale = 1.0f
    skeletonOutlineScale = 1.0f
    skeletonSoftness = 0.0f
    skeletonAnimationDurationSeconds = 0.05f
    skeletonDotsInnerColorOption = SkeletonColorOption.WHITE
    skeletonDotsOuterColorOption = SkeletonColorOption.CYAN
    skeletonConnectionsInnerColorOption = SkeletonColorOption.WHITE
    skeletonConnectionsOuterColorOption = SkeletonColorOption.CYAN
}
```

You can also apply skeleton settings at configuration time:
```kotlin
val smKitUI = SMKitUI.Configuration(context)
    .setUIKey("YOUR_KEY")
    .applySkeletonSettings {
        skeletonPreset = SkeletonPreset.ATHLETIC
    }
    .configure(listener)
```

Available `SkeletonPreset` values: `DEFAULT`, `MINIMAL_DOTS`, `THIN_OUTLINE`, `MONOCHROME_CLEAN`, `NEON_GLOW`, `BOLD_HIGHLIGHT`, `SOFT_FILL`, `WIREFRAME`, `HIGH_CONTRAST`, `PASTEL`, `DARK_OUTLINE`, `MINIMAL_LINE`, `DOUBLE_STROKE`, `GRADIENT_READY`, `SUBTLE_SHADOW`, `CLASSIC`, `ATHLETIC`, `PREMIUM`, `HOLOGRAM`, `MATTE`, `NEON_PULSE`, `OUTLINE_ONLY`, `SLIM`, `THICK`, `STUDIO`, `ACCESSIBILITY`.

## 🆘 Troubleshooting & Support
- Always call `configure` before starting any session
- If you have issues, [contact us](mailto:support@sency.ai)

---

Enjoy building with Sency SMKitUI!
