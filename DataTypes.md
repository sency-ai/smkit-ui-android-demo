# Data

## Assessment Data

### AssessmentData
| Name                | Type              | Description                                                                                                  |
|---------------------|-------------------|--------------------------------------------------------------------------------------------------------------|
| session_id          | `String`          | Random 24 character string.                                                                                  |
| type                | `String`          | Either “FitnessAssessment” or “CustomAssessment”.                                                            |
| user_data           | `UserData`        | User details.                                                                                                |
| sdk_data            | `SdkData`         | SDK details.                                                                                                 |
| exercises           | `[ExerciseData]`  | List of exercises data.                                                                                      |
| start_time          | `Date`            | UTC format: "YYYY-MM-dd HH:mm:ss.SSSZ".                                                                      |
| end_time            | `Date`            | UTC format: "YYYY-MM-dd HH:mm:ss.SSSZ".                                                                      |
| total_time          | `Double`          | Time in seconds from session start to session end.                                                           |
| total_score         | `Int`             | Number in range [0-100].                                                                                     |
| total_score_segmented| `Int`             | Number in range [0-100].                                                                                     |

## Workout Data

### WorkoutData
| Name                | Type              | Description                                                                                                  |
|---------------------|-------------------|--------------------------------------------------------------------------------------------------------------|
| session_id          | `String`          | Random 24 character string.                                                                                  |
| type                | `String`          | Either “CustomWorkout” or “ProgramWorkout”.                                                                  |
| workout_config      | `WorkoutDetailsData?` | Program details; set for program workouts only.                                                          |
| user_data           | `UserData`        | User details.                                                                                                |
| language            | `String`          | Session language code (“en”, “he”, or “zh”).                                                                 |
| exercises           | `[ExerciseData]`  | List of exercises data.                                                                                      |
| start_time          | `Date`            | UTC format: "YYYY-MM-dd HH:mm:ss.SSSZ".                                                                      |
| end_time            | `Date`            | UTC format: "YYYY-MM-dd HH:mm:ss.SSSZ".                                                                      |
| total_time          | `Double`          | Time in seconds from session start to session end.                                                           |
| total_score         | `Float`           | Number in range [0-100].                                                                                     |
| modifications       | `[WorkoutModificationEvent]?` | Exercise switches, added rests, and paired switches/skips made during the session.                |

In Kotlin these are `WorkoutSummaryData` properties (`userData`, `sessionId`, `language`, `activityType`, `startTime`, `endTime`, `totalTime`, `totalScore`, `totalScoreSegmented`, `exercises`, `workoutConfig`, `modifications`).

### WorkoutModificationEvent
| Name                | Type              | Description                                                                                                  |
|---------------------|-------------------|--------------------------------------------------------------------------------------------------------------|
| type                | `String`          | “exercise_switched”, “rest_added”, “paired_exercise_switched”, or “paired_exercise_skipped”.                 |
| timestamp           | `String`          | Time of the change.                                                                                          |
| source_detector     | `String?`         | Detector that was replaced.                                                                                  |
| applied_detector    | `String?`         | Detector that replaced it.                                                                                   |
| paired_detector     | `String?`         | Paired (left/right) detector, when one was affected.                                                         |
| rest_seconds        | `Int?`            | Length of an added rest.                                                                                     |
| source              | `String`          | “host”, “pause_suggestion”, or “pause_manual”.                             |

## Exercise Data

### ExerciseData
| Name                | Type              | Description                                                                                                  |
|---------------------|-------------------|--------------------------------------------------------------------------------------------------------------|
| session_id          | `String`          | Random 24 character string.                                                                                  |
| type                | `String`          | Either “CustomWorkout” or “ProgramWorkout”.                                                                  |
| exercise_info       | `ExerciseInfoData`| Details about the exercise.                                                                                  |
| start_time          | `Date`            | UTC format: "YYYY-MM-dd HH:mm:ss.SSSZ".                                                                      |
| end_time            | `Date`            | UTC format: "YYYY-MM-dd HH:mm:ss.SSSZ".                                                                      |
| total_time          | `Double`          | Time in seconds from session start to session end.                                                           |
| total_score         | `Float?`          | Optional, number in range [0-100].                                                                           |
| total_score_segmented| `Float?`          | Optional, number in range [0-100].                                                                           |
| performance_score   | `Float?`          | Optional, number in range [0-100].                                                                           |
| performance_score_segmented| `Float?`    | Optional, number in range [0-100].                                                                           |
| technique_score     | `Float?`          | Optional, number in range [0-100].                                                                           |
| technique_score_segmented | `Float?`    | Optional, number in range [0-100].                                                                           |
| reps_performed      | `Int?`            | Optional, positive number.                                                                                   |
| reps_performed_perfect | `Int?`          | Optional, positive number (<= reps_performed).                                                               |
| time_in_position    | `Float?`          | Optional, positive number in seconds. Total time in position.                                                |
| time_in_position_perfect | `Float?`     | Optional, positive number in seconds. Total time in position.                                                |
| peak_range_of_motion_score | `Float?`   | Optional, number in range [0-100].                                                                           |
| feedback            | `[FormFeedbackType:Float]?` | Optional, pairs feedback occurrence ratio by feedback type.                                                  |
| peak_range_of_motion_degrees | `Float?` | Optional, peak range of motion in degrees.                                                                   |
| stretch_set_config  | `Map?`            | Optional, the `SMStretchSetConfig` the exercise ran with.                                                     |
| position_rep_config | `Map?`            | Optional, the `SMPositionRepConfig` the exercise ran with.                                                    |
| mobility_rep_config | `Map?`            | Optional, the `SMMobilityRepConfig` the exercise ran with.                                                    |
| adaptive_rom        | `Map?`            | Optional, adaptive-ROM state for account history.                                                            |
| configured_score    | `Object?`         | Optional, evaluated score components.                                                                        |
| internal_insights   | `Object?`         | Optional, assessment insights. Requires `setIncludeAssessmentInsights(true)` and `exportInternalInsights`.    |
| avg_rep_time_seconds | `Double?`        | Optional. Requires `setExerciseSummaryTimingMetricsEnabled(true)`.                                           |
| max_consecutive_hold_seconds | `Double?` | Optional. Requires `setExerciseSummaryTimingMetricsEnabled(true)`.                                         |
| max_consecutive_perfect_hold_seconds | `Double?` | Optional. Requires `setExerciseSummaryTimingMetricsEnabled(true)`.                                 |


## Minor Types

### UserData
| Name                | Type              | Description                                                                                                  |
|---------------------|-------------------|--------------------------------------------------------------------------------------------------------------|
| age                 | `Int?`            | Optional, user's age in years (rounded).                                                                     |
| gender              | `String?`         | Optional, either “male” or “female”.                                                                         |

### WorkoutDetailsData
| Name                | Type              | Description                                                                                                  |
|---------------------|-------------------|--------------------------------------------------------------------------------------------------------------|
| program_id          | `String`          | Unique program id.                                                                                           |
| week                | `Int`             | Number greater than 0.                                                                                       |
| body_zone           | `String`          | Either “upper”, “lower”, or “full”.                                                                          |
| difficulty_level    | `String`          | Either “high”, “med”, or “low”.                                                                              |
| duration            | `String`          | Either “long” or “short”.                                                                                    |

### ExerciseInfoData
| Name                | Type              | Description                                                                                                  |
|---------------------|-------------------|--------------------------------------------------------------------------------------------------------------|
| exercise_id         | `String`          | Exercise id.                                                                                                 |
| total_time          | `Int?`            | Optional, exercise total time in seconds (to be used in exercise timer).                                      |
| scoring_params      | `ScoringParamsData` | Scoring params information.                                                                                  |
| ui_elements         | `[String]`        | “timer”, “repsCounter”, “gaugeOfMotion”, “countdownTimer”, “holdingPosition”, “quickMotion”, “skeleton”.     |
| instruction_video   | `String?`         | Optional, file name.                                                                                         |
| voice_intro         | `String?`         | Optional, file name.                                                                                         |
| voice_outro         | `String?`         | Optional, file name.                                                                                         |
| pretty_name         | `String`          | Exercise display name.                                                                                       |


### ScoringParamsData
| Name                | Type              | Description                                                                                                  |
|---------------------|-------------------|--------------------------------------------------------------------------------------------------------------|
| target_reps         | `Int?`            | Optional, positive number.                                                                                   |
| target_time         | `Int?`            | Optional, positive number.                                                                                   |
| target_rom          | `FormFeedbackType?` | Optional, feedback type.                                                                                     |
| pass_criteria       | `[FormFeedbackType]?` | Optional, list of form checks to be used as pass criteria for scoring.                                        |
