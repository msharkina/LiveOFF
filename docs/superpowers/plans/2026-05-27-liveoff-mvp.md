# LiveOFF MVP Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build an offline-first Android survival tips app with SOS features, checklists, instruction cards, search, favorites, and Morse code trainer.

**Architecture:** Clean Architecture (Data/Domain/UI) with Kotlin + Jetpack Compose. Room DB with FTS4 for offline content. All content bundled in APK as JSON, imported to Room on first launch.

**Tech Stack:** Kotlin 2.1+, Jetpack Compose BOM, Room + FTS4, Hilt, Compose Navigation, DataStore, Kotlinx Serialization, CameraX (torch), MediaPlayer

**Spec:** `docs/superpowers/specs/2026-05-26-liveoff-design.md`

---

## Task 1: Project Configuration — Gradle & Version Catalog

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `build.gradle.kts`
- Modify: `app/build.gradle.kts`
- Modify: `settings.gradle.kts`

- [ ] **Step 1: Update version catalog**

Replace entire `gradle/libs.versions.toml`:

```toml
[versions]
agp = "9.2.1"
kotlin = "2.1.0"
ksp = "2.1.0-1.0.29"
composeBom = "2025.05.00"
room = "2.7.1"
hilt = "2.54"
hiltNavigationCompose = "1.2.0"
navigationCompose = "2.8.9"
datastore = "1.1.4"
kotlinxSerialization = "1.7.3"
coreKtx = "1.16.0"
lifecycleRuntimeKtx = "2.9.0"
activityCompose = "1.10.1"
camerax = "1.4.2"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"
coroutines = "1.10.1"

[libraries]
# Compose
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }

# Core
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }

# Navigation
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }

# Room
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-android-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
androidx-hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }

# DataStore
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }

# Serialization
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }

# CameraX
androidx-camera-core = { group = "androidx.camera", name = "camera-core", version.ref = "camerax" }
androidx-camera-camera2 = { group = "androidx.camera", name = "camera-camera2", version.ref = "camerax" }
androidx-camera-lifecycle = { group = "androidx.camera", name = "camera-lifecycle", version.ref = "camerax" }

# Coroutines
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "coroutines" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutines" }

# Test
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
```

- [ ] **Step 2: Update root build.gradle.kts**

Replace entire `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
}
```

- [ ] **Step 3: Update app/build.gradle.kts**

Replace entire `app/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.lefesafety.liveoff"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lefesafety.liveoff"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
```

- [ ] **Step 4: Verify project syncs**

Run: `cd /Users/mariasharkina/AndroidStudioProjects/LiveOFF && ./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL (may take time on first run to download dependencies)

- [ ] **Step 5: Commit**

```bash
git add gradle/libs.versions.toml build.gradle.kts app/build.gradle.kts
git commit -m "chore: configure project for Kotlin, Compose, Hilt, Room, CameraX"
```

---

## Task 2: Domain Models

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/model/Category.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/model/Card.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/model/Checklist.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/model/Difficulty.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/model/ContentType.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/model/Favorite.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/model/UserNote.kt`

- [ ] **Step 1: Create Difficulty enum and ContentType enum**

`app/src/main/java/com/lefesafety/liveoff/domain/model/Difficulty.kt`:
```kotlin
package com.lefesafety.liveoff.domain.model

enum class Difficulty { EASY, MEDIUM, HARD }
```

`app/src/main/java/com/lefesafety/liveoff/domain/model/ContentType.kt`:
```kotlin
package com.lefesafety.liveoff.domain.model

enum class ContentType { CARD, CHECKLIST }
```

- [ ] **Step 2: Create Category model**

`app/src/main/java/com/lefesafety/liveoff/domain/model/Category.kt`:
```kotlin
package com.lefesafety.liveoff.domain.model

data class Category(
    val id: String,
    val name: String,
    val icon: String,
    val accentColor: String,
    val sortOrder: Int
)
```

- [ ] **Step 3: Create Card model**

`app/src/main/java/com/lefesafety/liveoff/domain/model/Card.kt`:
```kotlin
package com.lefesafety.liveoff.domain.model

data class Card(
    val id: String,
    val categoryId: String,
    val title: String,
    val briefSteps: List<String>,
    val detailedContent: String,
    val warnings: List<String>,
    val difficulty: Difficulty,
    val estimatedTime: String,
    val tools: List<String>,
    val tags: List<String>,
    val version: Int
)
```

- [ ] **Step 4: Create Checklist model**

`app/src/main/java/com/lefesafety/liveoff/domain/model/Checklist.kt`:
```kotlin
package com.lefesafety.liveoff.domain.model

data class ChecklistItem(
    val text: String,
    val sortOrder: Int
)

data class Checklist(
    val id: String,
    val categoryId: String,
    val title: String,
    val items: List<ChecklistItem>
)
```

- [ ] **Step 5: Create Favorite and UserNote models**

`app/src/main/java/com/lefesafety/liveoff/domain/model/Favorite.kt`:
```kotlin
package com.lefesafety.liveoff.domain.model

data class Favorite(
    val id: Long = 0,
    val contentId: String,
    val contentType: ContentType,
    val addedAt: Long
)
```

`app/src/main/java/com/lefesafety/liveoff/domain/model/UserNote.kt`:
```kotlin
package com.lefesafety.liveoff.domain.model

data class UserNote(
    val id: Long = 0,
    val contentId: String,
    val text: String,
    val updatedAt: Long
)
```

- [ ] **Step 6: Verify compilation**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/domain/
git commit -m "feat: add domain models"
```

---

## Task 3: Room Entities & Type Converters

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/entity/CategoryEntity.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/entity/CardEntity.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/entity/CardFtsEntity.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/entity/ChecklistEntity.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/entity/FavoriteEntity.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/entity/UserNoteEntity.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/entity/ChecklistProgressEntity.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/entity/ReadStatusEntity.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/db/Converters.kt`

- [ ] **Step 1: Create CategoryEntity**

`app/src/main/java/com/lefesafety/liveoff/data/local/entity/CategoryEntity.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val icon: String,
    val accentColor: String,
    val sortOrder: Int
)
```

- [ ] **Step 2: Create CardEntity and CardFtsEntity**

`app/src/main/java/com/lefesafety/liveoff/data/local/entity/CardEntity.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class CardEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val title: String,
    val briefSteps: String,       // JSON array serialized
    val detailedContent: String,
    val warnings: String,          // JSON array serialized
    val difficulty: String,        // "EASY", "MEDIUM", "HARD"
    val estimatedTime: String,
    val tools: String,             // JSON array serialized
    val tags: String,              // JSON array serialized
    val version: Int
)
```

`app/src/main/java/com/lefesafety/liveoff/data/local/entity/CardFtsEntity.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = CardEntity::class)
@Entity(tableName = "cards_fts")
data class CardFtsEntity(
    val title: String,
    val briefSteps: String,
    val detailedContent: String
)
```

- [ ] **Step 3: Create ChecklistEntity**

`app/src/main/java/com/lefesafety/liveoff/data/local/entity/ChecklistEntity.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checklists",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class ChecklistEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val title: String,
    val items: String  // JSON array serialized: [{"text":"...","sortOrder":1}, ...]
)
```

- [ ] **Step 4: Create user data entities**

`app/src/main/java/com/lefesafety/liveoff/data/local/entity/FavoriteEntity.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contentId: String,
    val contentType: String,  // "CARD" or "CHECKLIST"
    val addedAt: Long
)
```

`app/src/main/java/com/lefesafety/liveoff/data/local/entity/UserNoteEntity.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_notes")
data class UserNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contentId: String,
    val text: String,
    val updatedAt: Long
)
```

`app/src/main/java/com/lefesafety/liveoff/data/local/entity/ChecklistProgressEntity.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity

@Entity(tableName = "checklist_progress", primaryKeys = ["checklistId", "itemIndex"])
data class ChecklistProgressEntity(
    val checklistId: String,
    val itemIndex: Int,
    val isChecked: Boolean
)
```

`app/src/main/java/com/lefesafety/liveoff/data/local/entity/ReadStatusEntity.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_status")
data class ReadStatusEntity(
    @PrimaryKey val cardId: String,
    val readAt: Long
)
```

- [ ] **Step 5: Create type converters**

`app/src/main/java/com/lefesafety/liveoff/data/local/db/Converters.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.db

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = json.decodeFromString(value)
}
```

- [ ] **Step 6: Verify compilation**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/data/
git commit -m "feat: add Room entities and type converters"
```

---

## Task 4: DAOs

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/dao/CategoryDao.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/dao/CardDao.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/dao/ChecklistDao.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/dao/UserDataDao.kt`

- [ ] **Step 1: Create CategoryDao**

`app/src/main/java/com/lefesafety/liveoff/data/local/dao/CategoryDao.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lefesafety.liveoff.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun count(): Int
}
```

- [ ] **Step 2: Create CardDao**

`app/src/main/java/com/lefesafety/liveoff/data/local/dao/CardDao.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lefesafety.liveoff.data.local.entity.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards WHERE categoryId = :categoryId")
    fun getCardsByCategory(categoryId: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCardById(id: String): CardEntity?

    @Query("SELECT * FROM cards WHERE id = :id")
    fun observeCard(id: String): Flow<CardEntity?>

    @Query("""
        SELECT cards.* FROM cards
        JOIN cards_fts ON cards.rowid = cards_fts.rowid
        WHERE cards_fts MATCH :query
    """)
    fun search(query: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<CardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CardEntity>)

    @Query("SELECT COUNT(*) FROM cards")
    suspend fun count(): Int
}
```

- [ ] **Step 3: Create ChecklistDao**

`app/src/main/java/com/lefesafety/liveoff/data/local/dao/ChecklistDao.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lefesafety.liveoff.data.local.entity.ChecklistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklists WHERE categoryId = :categoryId")
    fun getChecklistsByCategory(categoryId: String): Flow<List<ChecklistEntity>>

    @Query("SELECT * FROM checklists WHERE id = :id")
    suspend fun getChecklistById(id: String): ChecklistEntity?

    @Query("SELECT * FROM checklists WHERE id = :id")
    fun observeChecklist(id: String): Flow<ChecklistEntity?>

    @Query("SELECT * FROM checklists")
    fun getAllChecklists(): Flow<List<ChecklistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checklists: List<ChecklistEntity>)

    @Query("SELECT COUNT(*) FROM checklists")
    suspend fun count(): Int
}
```

- [ ] **Step 4: Create UserDataDao**

`app/src/main/java/com/lefesafety/liveoff/data/local/dao/UserDataDao.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lefesafety.liveoff.data.local.entity.ChecklistProgressEntity
import com.lefesafety.liveoff.data.local.entity.FavoriteEntity
import com.lefesafety.liveoff.data.local.entity.ReadStatusEntity
import com.lefesafety.liveoff.data.local.entity.UserNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {
    // Favorites
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE contentId = :contentId AND contentType = :contentType LIMIT 1")
    suspend fun getFavorite(contentId: String, contentType: String): FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE contentId = :contentId AND contentType = :contentType")
    suspend fun deleteFavorite(contentId: String, contentType: String)

    // Notes
    @Query("SELECT * FROM user_notes WHERE contentId = :contentId LIMIT 1")
    fun observeNote(contentId: String): Flow<UserNoteEntity?>

    @Query("SELECT * FROM user_notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<UserNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: UserNoteEntity)

    @Query("DELETE FROM user_notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    // Checklist progress
    @Query("SELECT * FROM checklist_progress WHERE checklistId = :checklistId")
    fun getChecklistProgress(checklistId: String): Flow<List<ChecklistProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChecklistProgress(progress: ChecklistProgressEntity)

    @Query("DELETE FROM checklist_progress WHERE checklistId = :checklistId")
    suspend fun resetChecklistProgress(checklistId: String)

    // Read status
    @Query("SELECT * FROM read_status WHERE cardId = :cardId LIMIT 1")
    suspend fun getReadStatus(cardId: String): ReadStatusEntity?

    @Query("SELECT * FROM read_status")
    fun getAllReadStatuses(): Flow<List<ReadStatusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markAsRead(readStatus: ReadStatusEntity)
}
```

- [ ] **Step 5: Verify compilation**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/data/local/dao/
git commit -m "feat: add Room DAOs"
```

---

## Task 5: Room Database & ContentImporter

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/db/LiveOffDatabase.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/ContentImporter.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/dto/ContentPackageDto.kt`

- [ ] **Step 1: Create JSON DTOs for content parsing**

`app/src/main/java/com/lefesafety/liveoff/data/local/dto/ContentPackageDto.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class ContentPackageDto(
    val packageId: String,
    val version: Int,
    val categories: List<CategoryDto>,
    val cards: List<CardDto>,
    val checklists: List<ChecklistDto>
)

@Serializable
data class CategoryDto(
    val id: String,
    val name: String,
    val icon: String,
    val accentColor: String,
    val sortOrder: Int
)

@Serializable
data class CardDto(
    val id: String,
    val categoryId: String,
    val title: String,
    val briefSteps: List<String>,
    val detailedContent: String,
    val warnings: List<String>,
    val difficulty: String,
    val estimatedTime: String,
    val tools: List<String>,
    val tags: List<String>,
    val version: Int
)

@Serializable
data class ChecklistItemDto(
    val text: String,
    val sortOrder: Int
)

@Serializable
data class ChecklistDto(
    val id: String,
    val categoryId: String,
    val title: String,
    val items: List<ChecklistItemDto>
)
```

- [ ] **Step 2: Create LiveOffDatabase**

`app/src/main/java/com/lefesafety/liveoff/data/local/db/LiveOffDatabase.kt`:
```kotlin
package com.lefesafety.liveoff.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lefesafety.liveoff.data.local.dao.CardDao
import com.lefesafety.liveoff.data.local.dao.CategoryDao
import com.lefesafety.liveoff.data.local.dao.ChecklistDao
import com.lefesafety.liveoff.data.local.dao.UserDataDao
import com.lefesafety.liveoff.data.local.entity.CardEntity
import com.lefesafety.liveoff.data.local.entity.CardFtsEntity
import com.lefesafety.liveoff.data.local.entity.CategoryEntity
import com.lefesafety.liveoff.data.local.entity.ChecklistEntity
import com.lefesafety.liveoff.data.local.entity.ChecklistProgressEntity
import com.lefesafety.liveoff.data.local.entity.FavoriteEntity
import com.lefesafety.liveoff.data.local.entity.ReadStatusEntity
import com.lefesafety.liveoff.data.local.entity.UserNoteEntity

@Database(
    entities = [
        CategoryEntity::class,
        CardEntity::class,
        CardFtsEntity::class,
        ChecklistEntity::class,
        FavoriteEntity::class,
        UserNoteEntity::class,
        ChecklistProgressEntity::class,
        ReadStatusEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LiveOffDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun cardDao(): CardDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun userDataDao(): UserDataDao
}
```

- [ ] **Step 3: Create ContentImporter**

`app/src/main/java/com/lefesafety/liveoff/data/local/ContentImporter.kt`:
```kotlin
package com.lefesafety.liveoff.data.local

import android.content.Context
import com.lefesafety.liveoff.data.local.dao.CardDao
import com.lefesafety.liveoff.data.local.dao.CategoryDao
import com.lefesafety.liveoff.data.local.dao.ChecklistDao
import com.lefesafety.liveoff.data.local.dto.ContentPackageDto
import com.lefesafety.liveoff.data.local.entity.CardEntity
import com.lefesafety.liveoff.data.local.entity.CategoryEntity
import com.lefesafety.liveoff.data.local.entity.ChecklistEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentImporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryDao: CategoryDao,
    private val cardDao: CardDao,
    private val checklistDao: ChecklistDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun importIfNeeded() {
        if (categoryDao.count() > 0) return

        val jsonString = context.assets.open("content/base.json")
            .bufferedReader()
            .use { it.readText() }

        val pkg = json.decodeFromString<ContentPackageDto>(jsonString)

        categoryDao.insertAll(pkg.categories.map { cat ->
            CategoryEntity(
                id = cat.id,
                name = cat.name,
                icon = cat.icon,
                accentColor = cat.accentColor,
                sortOrder = cat.sortOrder
            )
        })

        cardDao.insertAll(pkg.cards.map { card ->
            CardEntity(
                id = card.id,
                categoryId = card.categoryId,
                title = card.title,
                briefSteps = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(kotlinx.serialization.builtins.serializer<String>()), card.briefSteps),
                detailedContent = card.detailedContent,
                warnings = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(kotlinx.serialization.builtins.serializer<String>()), card.warnings),
                difficulty = card.difficulty,
                estimatedTime = card.estimatedTime,
                tools = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(kotlinx.serialization.builtins.serializer<String>()), card.tools),
                tags = json.encodeToString(kotlinx.serialization.builtins.ListSerializer(kotlinx.serialization.builtins.serializer<String>()), card.tags),
                version = card.version
            )
        })

        checklistDao.insertAll(pkg.checklists.map { cl ->
            ChecklistEntity(
                id = cl.id,
                categoryId = cl.categoryId,
                title = cl.title,
                items = json.encodeToString(
                    kotlinx.serialization.builtins.ListSerializer(com.lefesafety.liveoff.data.local.dto.ChecklistItemDto.serializer()),
                    cl.items
                )
            )
        })
    }
}
```

- [ ] **Step 4: Verify compilation**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/data/
git commit -m "feat: add Room database, DTOs, and ContentImporter"
```

---

## Task 6: Domain Repositories & Use Cases

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/repository/ContentRepository.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/repository/UserDataRepository.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/usecase/GetCardsByCategory.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/usecase/SearchContent.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/usecase/ToggleFavorite.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/usecase/UpdateChecklistProgress.kt`

- [ ] **Step 1: Create repository interfaces**

`app/src/main/java/com/lefesafety/liveoff/domain/repository/ContentRepository.kt`:
```kotlin
package com.lefesafety.liveoff.domain.repository

import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.model.Checklist
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
    fun getCardsByCategory(categoryId: String): Flow<List<Card>>
    fun getCardById(id: String): Flow<Card?>
    fun getAllCards(): Flow<List<Card>>
    fun searchCards(query: String): Flow<List<Card>>
    fun getChecklistsByCategory(categoryId: String): Flow<List<Checklist>>
    fun getChecklistById(id: String): Flow<Checklist?>
    fun getAllChecklists(): Flow<List<Checklist>>
    suspend fun importContent()
}
```

`app/src/main/java/com/lefesafety/liveoff/domain/repository/UserDataRepository.kt`:
```kotlin
package com.lefesafety.liveoff.domain.repository

import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.model.Favorite
import com.lefesafety.liveoff.domain.model.UserNote
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    fun getAllFavorites(): Flow<List<Favorite>>
    suspend fun isFavorite(contentId: String, contentType: ContentType): Boolean
    suspend fun toggleFavorite(contentId: String, contentType: ContentType)
    fun observeNote(contentId: String): Flow<UserNote?>
    fun getAllNotes(): Flow<List<UserNote>>
    suspend fun saveNote(contentId: String, text: String)
    suspend fun deleteNote(id: Long)
    fun getChecklistProgress(checklistId: String): Flow<Map<Int, Boolean>>
    suspend fun setChecklistItemChecked(checklistId: String, itemIndex: Int, isChecked: Boolean)
    suspend fun resetChecklistProgress(checklistId: String)
    suspend fun markCardAsRead(cardId: String)
    suspend fun isCardRead(cardId: String): Boolean
    fun getAllReadCardIds(): Flow<Set<String>>
}
```

- [ ] **Step 2: Create use cases**

`app/src/main/java/com/lefesafety/liveoff/domain/usecase/GetCardsByCategory.kt`:
```kotlin
package com.lefesafety.liveoff.domain.usecase

import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCardsByCategory @Inject constructor(
    private val contentRepository: ContentRepository
) {
    operator fun invoke(categoryId: String): Flow<List<Card>> =
        contentRepository.getCardsByCategory(categoryId)
}
```

`app/src/main/java/com/lefesafety/liveoff/domain/usecase/SearchContent.kt`:
```kotlin
package com.lefesafety.liveoff.domain.usecase

import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchContent @Inject constructor(
    private val contentRepository: ContentRepository
) {
    operator fun invoke(query: String): Flow<List<Card>> =
        contentRepository.searchCards(query)
}
```

`app/src/main/java/com/lefesafety/liveoff/domain/usecase/ToggleFavorite.kt`:
```kotlin
package com.lefesafety.liveoff.domain.usecase

import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import javax.inject.Inject

class ToggleFavorite @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(contentId: String, contentType: ContentType) {
        userDataRepository.toggleFavorite(contentId, contentType)
    }
}
```

`app/src/main/java/com/lefesafety/liveoff/domain/usecase/UpdateChecklistProgress.kt`:
```kotlin
package com.lefesafety.liveoff.domain.usecase

import com.lefesafety.liveoff.domain.repository.UserDataRepository
import javax.inject.Inject

class UpdateChecklistProgress @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(checklistId: String, itemIndex: Int, isChecked: Boolean) {
        userDataRepository.setChecklistItemChecked(checklistId, itemIndex, isChecked)
    }
}
```

- [ ] **Step 3: Verify compilation**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/domain/
git commit -m "feat: add domain repository interfaces and use cases"
```

---

## Task 7: Repository Implementations

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/data/repository/ContentRepositoryImpl.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/repository/UserDataRepositoryImpl.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/EntityMappers.kt`

- [ ] **Step 1: Create entity-to-domain mappers**

`app/src/main/java/com/lefesafety/liveoff/data/local/EntityMappers.kt`:
```kotlin
package com.lefesafety.liveoff.data.local

import com.lefesafety.liveoff.data.local.entity.CardEntity
import com.lefesafety.liveoff.data.local.entity.CategoryEntity
import com.lefesafety.liveoff.data.local.entity.ChecklistEntity
import com.lefesafety.liveoff.data.local.entity.FavoriteEntity
import com.lefesafety.liveoff.data.local.entity.UserNoteEntity
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.model.Checklist
import com.lefesafety.liveoff.domain.model.ChecklistItem
import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.model.Difficulty
import com.lefesafety.liveoff.domain.model.Favorite
import com.lefesafety.liveoff.domain.model.UserNote
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    icon = icon,
    accentColor = accentColor,
    sortOrder = sortOrder
)

fun CardEntity.toDomain() = Card(
    id = id,
    categoryId = categoryId,
    title = title,
    briefSteps = json.decodeFromString<List<String>>(briefSteps),
    detailedContent = detailedContent,
    warnings = json.decodeFromString<List<String>>(warnings),
    difficulty = Difficulty.valueOf(difficulty),
    estimatedTime = estimatedTime,
    tools = json.decodeFromString<List<String>>(tools),
    tags = json.decodeFromString<List<String>>(tags),
    version = version
)

fun ChecklistEntity.toDomain(): Checklist {
    @kotlinx.serialization.Serializable
    data class ItemDto(val text: String, val sortOrder: Int)

    val itemDtos = json.decodeFromString<List<ItemDto>>(items)
    return Checklist(
        id = id,
        categoryId = categoryId,
        title = title,
        items = itemDtos.map { ChecklistItem(text = it.text, sortOrder = it.sortOrder) }
    )
}

fun FavoriteEntity.toDomain() = Favorite(
    id = id,
    contentId = contentId,
    contentType = ContentType.valueOf(contentType),
    addedAt = addedAt
)

fun UserNoteEntity.toDomain() = UserNote(
    id = id,
    contentId = contentId,
    text = text,
    updatedAt = updatedAt
)
```

- [ ] **Step 2: Create ContentRepositoryImpl**

`app/src/main/java/com/lefesafety/liveoff/data/repository/ContentRepositoryImpl.kt`:
```kotlin
package com.lefesafety.liveoff.data.repository

import com.lefesafety.liveoff.data.local.ContentImporter
import com.lefesafety.liveoff.data.local.dao.CardDao
import com.lefesafety.liveoff.data.local.dao.CategoryDao
import com.lefesafety.liveoff.data.local.dao.ChecklistDao
import com.lefesafety.liveoff.data.local.toDomain
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.model.Checklist
import com.lefesafety.liveoff.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val cardDao: CardDao,
    private val checklistDao: ChecklistDao,
    private val contentImporter: ContentImporter
) : ContentRepository {

    override fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories().map { list -> list.map { it.toDomain() } }

    override suspend fun getCategoryById(id: String): Category? =
        categoryDao.getCategoryById(id)?.toDomain()

    override fun getCardsByCategory(categoryId: String): Flow<List<Card>> =
        cardDao.getCardsByCategory(categoryId).map { list -> list.map { it.toDomain() } }

    override fun getCardById(id: String): Flow<Card?> =
        cardDao.observeCard(id).map { it?.toDomain() }

    override fun getAllCards(): Flow<List<Card>> =
        cardDao.getAllCards().map { list -> list.map { it.toDomain() } }

    override fun searchCards(query: String): Flow<List<Card>> =
        cardDao.search("$query*").map { list -> list.map { it.toDomain() } }

    override fun getChecklistsByCategory(categoryId: String): Flow<List<Checklist>> =
        checklistDao.getChecklistsByCategory(categoryId).map { list -> list.map { it.toDomain() } }

    override fun getChecklistById(id: String): Flow<Checklist?> =
        checklistDao.observeChecklist(id).map { it?.toDomain() }

    override fun getAllChecklists(): Flow<List<Checklist>> =
        checklistDao.getAllChecklists().map { list -> list.map { it.toDomain() } }

    override suspend fun importContent() {
        contentImporter.importIfNeeded()
    }
}
```

- [ ] **Step 3: Create UserDataRepositoryImpl**

`app/src/main/java/com/lefesafety/liveoff/data/repository/UserDataRepositoryImpl.kt`:
```kotlin
package com.lefesafety.liveoff.data.repository

import com.lefesafety.liveoff.data.local.dao.UserDataDao
import com.lefesafety.liveoff.data.local.entity.ChecklistProgressEntity
import com.lefesafety.liveoff.data.local.entity.FavoriteEntity
import com.lefesafety.liveoff.data.local.entity.ReadStatusEntity
import com.lefesafety.liveoff.data.local.entity.UserNoteEntity
import com.lefesafety.liveoff.data.local.toDomain
import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.model.Favorite
import com.lefesafety.liveoff.domain.model.UserNote
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepositoryImpl @Inject constructor(
    private val userDataDao: UserDataDao
) : UserDataRepository {

    override fun getAllFavorites(): Flow<List<Favorite>> =
        userDataDao.getAllFavorites().map { list -> list.map { it.toDomain() } }

    override suspend fun isFavorite(contentId: String, contentType: ContentType): Boolean =
        userDataDao.getFavorite(contentId, contentType.name) != null

    override suspend fun toggleFavorite(contentId: String, contentType: ContentType) {
        val existing = userDataDao.getFavorite(contentId, contentType.name)
        if (existing != null) {
            userDataDao.deleteFavorite(contentId, contentType.name)
        } else {
            userDataDao.insertFavorite(
                FavoriteEntity(
                    contentId = contentId,
                    contentType = contentType.name,
                    addedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override fun observeNote(contentId: String): Flow<UserNote?> =
        userDataDao.observeNote(contentId).map { it?.toDomain() }

    override fun getAllNotes(): Flow<List<UserNote>> =
        userDataDao.getAllNotes().map { list -> list.map { it.toDomain() } }

    override suspend fun saveNote(contentId: String, text: String) {
        userDataDao.upsertNote(
            UserNoteEntity(
                contentId = contentId,
                text = text,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun deleteNote(id: Long) {
        userDataDao.deleteNote(id)
    }

    override fun getChecklistProgress(checklistId: String): Flow<Map<Int, Boolean>> =
        userDataDao.getChecklistProgress(checklistId).map { list ->
            list.associate { it.itemIndex to it.isChecked }
        }

    override suspend fun setChecklistItemChecked(checklistId: String, itemIndex: Int, isChecked: Boolean) {
        userDataDao.upsertChecklistProgress(
            ChecklistProgressEntity(
                checklistId = checklistId,
                itemIndex = itemIndex,
                isChecked = isChecked
            )
        )
    }

    override suspend fun resetChecklistProgress(checklistId: String) {
        userDataDao.resetChecklistProgress(checklistId)
    }

    override suspend fun markCardAsRead(cardId: String) {
        userDataDao.markAsRead(ReadStatusEntity(cardId = cardId, readAt = System.currentTimeMillis()))
    }

    override suspend fun isCardRead(cardId: String): Boolean =
        userDataDao.getReadStatus(cardId) != null

    override fun getAllReadCardIds(): Flow<Set<String>> =
        userDataDao.getAllReadStatuses().map { list -> list.map { it.cardId }.toSet() }
}
```

- [ ] **Step 4: Verify compilation**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/data/
git commit -m "feat: add repository implementations and entity mappers"
```

---

## Task 8: Hilt DI Modules

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/di/DatabaseModule.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/di/RepositoryModule.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/LiveOffApplication.kt`
- Modify: `app/src/main/AndroidManifest.xml`

- [ ] **Step 1: Create DatabaseModule**

`app/src/main/java/com/lefesafety/liveoff/di/DatabaseModule.kt`:
```kotlin
package com.lefesafety.liveoff.di

import android.content.Context
import androidx.room.Room
import com.lefesafety.liveoff.data.local.dao.CardDao
import com.lefesafety.liveoff.data.local.dao.CategoryDao
import com.lefesafety.liveoff.data.local.dao.ChecklistDao
import com.lefesafety.liveoff.data.local.dao.UserDataDao
import com.lefesafety.liveoff.data.local.db.LiveOffDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LiveOffDatabase =
        Room.databaseBuilder(context, LiveOffDatabase::class.java, "liveoff.db")
            .build()

    @Provides fun provideCategoryDao(db: LiveOffDatabase): CategoryDao = db.categoryDao()
    @Provides fun provideCardDao(db: LiveOffDatabase): CardDao = db.cardDao()
    @Provides fun provideChecklistDao(db: LiveOffDatabase): ChecklistDao = db.checklistDao()
    @Provides fun provideUserDataDao(db: LiveOffDatabase): UserDataDao = db.userDataDao()
}
```

- [ ] **Step 2: Create RepositoryModule**

`app/src/main/java/com/lefesafety/liveoff/di/RepositoryModule.kt`:
```kotlin
package com.lefesafety.liveoff.di

import com.lefesafety.liveoff.data.repository.ContentRepositoryImpl
import com.lefesafety.liveoff.data.repository.UserDataRepositoryImpl
import com.lefesafety.liveoff.domain.repository.ContentRepository
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindContentRepository(impl: ContentRepositoryImpl): ContentRepository

    @Binds
    @Singleton
    abstract fun bindUserDataRepository(impl: UserDataRepositoryImpl): UserDataRepository
}
```

- [ ] **Step 3: Create Application class and update manifest**

`app/src/main/java/com/lefesafety/liveoff/LiveOffApplication.kt`:
```kotlin
package com.lefesafety.liveoff

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LiveOffApplication : Application()
```

Update `app/src/main/AndroidManifest.xml` — add `android:name` and the Activity:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.CALL_PHONE" />

    <application
        android:name=".LiveOffApplication"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.LiveOFF">

        <activity
            android:name=".ui.MainActivity"
            android:exported="true"
            android:theme="@style/Theme.LiveOFF">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

- [ ] **Step 4: Verify compilation**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/di/ app/src/main/java/com/lefesafety/liveoff/LiveOffApplication.kt app/src/main/AndroidManifest.xml
git commit -m "feat: add Hilt DI modules, Application class, and manifest"
```

---

## Task 9: Compose Theme

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/theme/Color.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/theme/Type.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/theme/Theme.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/theme/CategoryColors.kt`

- [ ] **Step 1: Create Color.kt**

`app/src/main/java/com/lefesafety/liveoff/ui/theme/Color.kt`:
```kotlin
package com.lefesafety.liveoff.ui.theme

import androidx.compose.ui.graphics.Color

// Background
val DarkBackground = Color(0xFF1E2126)
val DarkSurface = Color(0xFF2A2E35)
val DarkSurfaceVariant = Color(0xFF333840)

// Text
val TextPrimary = Color(0xFFF0F0F0)
val TextSecondary = Color(0xFFAAAAAA)
val TextMuted = Color(0xFF888888)

// Accents
val AccentOrange = Color(0xFFFF8C00)
val AccentRed = Color(0xFFE74C3C)
val AccentSosRed = Color(0xFFFF2200)
val AccentBlue = Color(0xFF3498DB)
val AccentGreen = Color(0xFF2ECC71)
val AccentPurple = Color(0xFFA29BFE)

// Material3 scheme
val PrimaryDark = AccentOrange
val OnPrimaryDark = Color(0xFF1E2126)
val ErrorDark = AccentSosRed
```

- [ ] **Step 2: Create CategoryColors.kt**

`app/src/main/java/com/lefesafety/liveoff/ui/theme/CategoryColors.kt`:
```kotlin
package com.lefesafety.liveoff.ui.theme

import androidx.compose.ui.graphics.Color

object CategoryColors {
    private val colorMap = mapOf(
        "#ff8c00" to AccentOrange,
        "#3498db" to AccentBlue,
        "#e74c3c" to AccentRed,
        "#2ecc71" to AccentGreen,
        "#ff2200" to AccentSosRed,
        "#a29bfe" to AccentPurple
    )

    fun fromHex(hex: String): Color =
        colorMap[hex.lowercase()] ?: AccentOrange
}
```

- [ ] **Step 3: Create Type.kt**

`app/src/main/java/com/lefesafety/liveoff/ui/theme/Type.kt`:
```kotlin
package com.lefesafety.liveoff.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LiveOffTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        color = TextPrimary
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = TextPrimary
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        color = TextPrimary
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = TextPrimary
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = TextSecondary
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp,
        color = TextPrimary
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        color = TextMuted
    )
)
```

- [ ] **Step 4: Create Theme.kt**

`app/src/main/java/com/lefesafety/liveoff/ui/theme/Theme.kt`:
```kotlin
package com.lefesafety.liveoff.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = ErrorDark
)

@Composable
fun LiveOffTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = LiveOffTypography,
        content = content
    )
}
```

- [ ] **Step 5: Verify compilation**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/theme/
git commit -m "feat: add Compose dark theme with category colors"
```

---

## Task 10: Navigation & MainActivity

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/Screen.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/MainActivity.kt`

- [ ] **Step 1: Create Screen sealed class**

`app/src/main/java/com/lefesafety/liveoff/ui/navigation/Screen.kt`:
```kotlin
package com.lefesafety.liveoff.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable data object Home : Screen
    @Serializable data object Sos : Screen
    @Serializable data object ChecklistCategories : Screen
    @Serializable data class ChecklistList(val categoryId: String) : Screen
    @Serializable data class ChecklistDetail(val checklistId: String) : Screen
    @Serializable data object CardCategories : Screen
    @Serializable data class CardList(val categoryId: String) : Screen
    @Serializable data class CardDetail(val cardId: String) : Screen
    @Serializable data object Search : Screen
    @Serializable data object Favorites : Screen
    @Serializable data object Settings : Screen
    @Serializable data object MorseAlphabet : Screen
    @Serializable data object MorseTrainer : Screen
    @Serializable data object MorseTransmit : Screen
}
```

- [ ] **Step 2: Create NavGraph with placeholder screens**

`app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`:
```kotlin
package com.lefesafety.liveoff.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

@Composable
fun LiveOffNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable<Screen.Home> { Placeholder("Home") }
        composable<Screen.Sos> { Placeholder("SOS") }
        composable<Screen.ChecklistCategories> { Placeholder("Checklist Categories") }
        composable<Screen.ChecklistList> { entry ->
            val route = entry.toRoute<Screen.ChecklistList>()
            Placeholder("Checklists: ${route.categoryId}")
        }
        composable<Screen.ChecklistDetail> { entry ->
            val route = entry.toRoute<Screen.ChecklistDetail>()
            Placeholder("Checklist: ${route.checklistId}")
        }
        composable<Screen.CardCategories> { Placeholder("Card Categories") }
        composable<Screen.CardList> { entry ->
            val route = entry.toRoute<Screen.CardList>()
            Placeholder("Cards: ${route.categoryId}")
        }
        composable<Screen.CardDetail> { entry ->
            val route = entry.toRoute<Screen.CardDetail>()
            Placeholder("Card: ${route.cardId}")
        }
        composable<Screen.Search> { Placeholder("Search") }
        composable<Screen.Favorites> { Placeholder("Favorites") }
        composable<Screen.Settings> { Placeholder("Settings") }
        composable<Screen.MorseAlphabet> { Placeholder("Morse Alphabet") }
        composable<Screen.MorseTrainer> { Placeholder("Morse Trainer") }
        composable<Screen.MorseTransmit> { Placeholder("Morse Transmit") }
    }
}

@Composable
private fun Placeholder(name: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name)
    }
}
```

- [ ] **Step 3: Create MainActivity**

`app/src/main/java/com/lefesafety/liveoff/ui/MainActivity.kt`:
```kotlin
package com.lefesafety.liveoff.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lefesafety.liveoff.ui.navigation.LiveOffNavGraph
import com.lefesafety.liveoff.ui.navigation.Screen
import com.lefesafety.liveoff.ui.theme.AccentSosRed
import com.lefesafety.liveoff.ui.theme.LiveOffTheme
import com.lefesafety.liveoff.ui.theme.TextPrimary
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveOffTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                val showSosFab = currentRoute != null &&
                    !currentRoute.contains("Sos")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        if (showSosFab) {
                            FloatingActionButton(
                                onClick = { navController.navigate(Screen.Sos) },
                                containerColor = AccentSosRed,
                                contentColor = TextPrimary
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = "SOS")
                            }
                        }
                    }
                ) { innerPadding ->
                    LiveOffNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
```

- [ ] **Step 4: Build and verify app launches**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL. APK can be installed and shows "Home" placeholder with SOS FAB.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/
git commit -m "feat: add navigation graph, MainActivity with SOS FAB"
```

---

## Task 11: JSON Content File (base.json)

**Files:**
- Create: `app/src/main/assets/content/base.json`

- [ ] **Step 1: Create base.json with all MVP content**

Create `app/src/main/assets/content/base.json` with the full content structure. This is a large JSON file with all 6 categories, 14 cards, and 3 checklists from the spec. The file should follow the schema defined in the spec section 8.

Categories:
- `fire` — "Огонь и тепло" (#ff8c00, sortOrder 1)
- `water` — "Вода и питание" (#3498db, sortOrder 2)
- `firstaid` — "Первая помощь" (#e74c3c, sortOrder 3)
- `radiation` — "Радиация / Химия / Воздушные атаки" (#e74c3c, sortOrder 4)
- `navigation` — "Навигация и инструменты" (#2ecc71, sortOrder 5)
- `evacuation` — "Сценарии / Эвакуация" (#2ecc71, sortOrder 6)

Cards (14 total, each with briefSteps, detailedContent in Markdown, warnings, difficulty, estimatedTime, tools, tags):
1. fire-matches — Как развести огонь: спички/зажигалка
2. fire-friction — Как развести огонь: примусы и трение
3. fire-shelter — Устройство примитивного укрытия (лес)
4. water-purify — Как очистить воду
5. water-plants — Съедобные растения
6. water-mushrooms — Съедобные и ядовитые грибы
7. firstaid-bleeding — Остановка наружного кровотечения
8. firstaid-fracture — Иммобилизация перелома
9. firstaid-burn — Первая помощь при ожоге
10. radiation-airstrike — Поведение при воздушной тревоге
11. radiation-chemical — Действия при химическом выбросе
12. radiation-nuclear — Действия при радиационном заражении
13. navigation-compass — Ориентирование без компаса
14. navigation-firewood — Как колоть дрова

Checklists (3):
1. evacuation-lost — Потерялся в лесу
2. evacuation-home — Экстренная эвакуация из дома
3. evacuation-night — Ночь на открытом месте

The full JSON content should be written by the implementing agent using the example texts from the spec (section 3.2-3.5) and expanding all 17 items with realistic, practical survival content in Russian.

- [ ] **Step 2: Verify JSON is valid**

Run: `python3 -c "import json; json.load(open('app/src/main/assets/content/base.json')); print('Valid JSON')"`
Expected: "Valid JSON"

- [ ] **Step 3: Commit**

```bash
git add app/src/main/assets/content/base.json
git commit -m "feat: add MVP content — 14 cards, 3 checklists, 6 categories"
```

---

## Task 12: Home Screen

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/home/HomeViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/home/HomeScreen.kt`
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Create HomeViewModel**

`app/src/main/java/com/lefesafety/liveoff/ui/screen/home/HomeViewModel.kt`:
```kotlin
package com.lefesafety.liveoff.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lefesafety.liveoff.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            contentRepository.importContent()
        }
    }
}
```

- [ ] **Step 2: Create HomeScreen**

`app/src/main/java/com/lefesafety/liveoff/ui/screen/home/HomeScreen.kt`:
```kotlin
package com.lefesafety.liveoff.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lefesafety.liveoff.ui.theme.AccentBlue
import com.lefesafety.liveoff.ui.theme.AccentGreen
import com.lefesafety.liveoff.ui.theme.AccentOrange
import com.lefesafety.liveoff.ui.theme.AccentPurple
import com.lefesafety.liveoff.ui.theme.AccentSosRed
import com.lefesafety.liveoff.ui.theme.DarkSurface
import com.lefesafety.liveoff.ui.theme.TextPrimary

data class HomeMenuItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    onNavigateToSos: () -> Unit,
    onNavigateToChecklists: () -> Unit,
    onNavigateToCards: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToMorse: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val menuItems = listOf(
        HomeMenuItem("SOS", Icons.Default.Warning, AccentSosRed, onNavigateToSos),
        HomeMenuItem("Чек-листы", Icons.Default.Checklist, AccentGreen, onNavigateToChecklists),
        HomeMenuItem("Карточки", Icons.Default.LibraryBooks, AccentOrange, onNavigateToCards),
        HomeMenuItem("Поиск", Icons.Default.Search, AccentBlue, onNavigateToSearch),
        HomeMenuItem("Избранное", Icons.Default.Favorite, AccentOrange, onNavigateToFavorites),
        HomeMenuItem("Морзе", Icons.Default.Settings, AccentPurple, onNavigateToMorse),
        HomeMenuItem("Настройки", Icons.Default.Settings, Color.Gray, onNavigateToSettings),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "LiveOFF",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Руководство по выживанию",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(menuItems.size) { index ->
                val item = menuItems[index]
                HomeMenuCard(item)
            }
        }
    }
}

@Composable
private fun HomeMenuCard(item: HomeMenuItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .clickable(onClick = item.onClick)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.color,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
```

- [ ] **Step 3: Wire HomeScreen into NavGraph**

Update `NavGraph.kt` — replace the `composable<Screen.Home>` placeholder:

```kotlin
composable<Screen.Home> {
    HomeScreen(
        onNavigateToSos = { navController.navigate(Screen.Sos) },
        onNavigateToChecklists = { navController.navigate(Screen.ChecklistCategories) },
        onNavigateToCards = { navController.navigate(Screen.CardCategories) },
        onNavigateToSearch = { navController.navigate(Screen.Search) },
        onNavigateToFavorites = { navController.navigate(Screen.Favorites) },
        onNavigateToMorse = { navController.navigate(Screen.MorseAlphabet) },
        onNavigateToSettings = { navController.navigate(Screen.Settings) }
    )
}
```

Add import: `import com.lefesafety.liveoff.ui.screen.home.HomeScreen`

- [ ] **Step 4: Build and verify**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/screen/home/ app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt
git commit -m "feat: add Home screen with menu grid and content import"
```

---

## Task 13: Card Screens (Categories, List, Detail)

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/cards/CardCategoriesViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/cards/CardCategoriesScreen.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/cards/CardListViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/cards/CardListScreen.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/cards/CardDetailViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/cards/CardDetailScreen.kt`
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`

This task implements the full cards flow: categories grid → card list → card detail with brief/expanded mode, favorites, and read status. Each ViewModel collects from the respective repository Flow. CardDetailScreen shows briefSteps by default with an expandable detailedContent section, warnings, and action buttons (favorite, mark read).

Wire all three screens into NavGraph replacing their placeholders. Pass `navController::navigate` lambdas for transitions.

- [ ] **Step 1: Create CardCategoriesViewModel + Screen**

CardCategoriesViewModel collects `contentRepository.getAllCategories()` as StateFlow. Screen renders a grid of category cards with colored left border matching `accentColor`.

- [ ] **Step 2: Create CardListViewModel + Screen**

CardListViewModel takes `SavedStateHandle` to get `categoryId`, collects `getCardsByCategory(categoryId)`. Screen shows a list of cards with title, difficulty badge, estimatedTime, tools tags.

- [ ] **Step 3: Create CardDetailViewModel + Screen**

CardDetailViewModel takes `SavedStateHandle` for `cardId`, collects card data, isFavorite, isRead states. Exposes actions: toggleFavorite, markAsRead. Screen shows:
- Category label + accent color
- Title, meta badges (time, difficulty, tools)
- briefSteps as numbered list with accent-colored left border
- "Подробная инструкция" expandable section rendering detailedContent
- Warnings section at bottom
- Favorite + Read action buttons

- [ ] **Step 4: Wire into NavGraph**

Replace `Screen.CardCategories`, `Screen.CardList`, `Screen.CardDetail` placeholders with real screens.

- [ ] **Step 5: Build and verify**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/screen/cards/ app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt
git commit -m "feat: add Card screens — categories, list, detail with expand"
```

---

## Task 14: Checklist Screens (Categories, List, Detail)

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/checklists/ChecklistCategoriesScreen.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/checklists/ChecklistListViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/checklists/ChecklistListScreen.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/checklists/ChecklistDetailViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/checklists/ChecklistDetailScreen.kt`
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`

Similar pattern to cards. ChecklistCategories reuses the same category grid (only shows categories that have checklists). ChecklistDetailScreen shows interactive checkboxes with progress persisted via `UpdateChecklistProgress` use case. Includes a "Reset" button to clear progress.

- [ ] **Step 1: Create ChecklistCategoriesScreen** (shares ViewModel pattern with CardCategories — filters categories that have checklists)

- [ ] **Step 2: Create ChecklistListViewModel + Screen** (list of checklists for a category)

- [ ] **Step 3: Create ChecklistDetailViewModel + Screen** (interactive checkboxes, progress bar, reset button)

- [ ] **Step 4: Wire into NavGraph**

- [ ] **Step 5: Build and verify**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/screen/checklists/ app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt
git commit -m "feat: add Checklist screens with interactive progress"
```

---

## Task 15: Search Screen

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/search/SearchViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/search/SearchScreen.kt`
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Create SearchViewModel**

SearchViewModel holds a `query` StateFlow. On query change (debounced 300ms), calls `searchContent(query)`. Collects results as StateFlow. Also collects `getAllCategories()` for filter chips.

- [ ] **Step 2: Create SearchScreen**

TextField at top, category filter chips below, results list. Each result item is clickable → navigates to CardDetail. Shows "No results" when empty + query is non-blank.

- [ ] **Step 3: Wire into NavGraph**

- [ ] **Step 4: Build and verify**

Run: `./gradlew assembleDebug`

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/screen/search/ app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt
git commit -m "feat: add Search screen with FTS and category filters"
```

---

## Task 16: Favorites Screen

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/favorites/FavoritesViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/favorites/FavoritesScreen.kt`
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Create FavoritesViewModel**

Collects `getAllFavorites()` and `getAllNotes()`. Joins favorites with card/checklist data to show titles.

- [ ] **Step 2: Create FavoritesScreen**

Two tabs or sections: "Избранное" and "Заметки". Favorite items link to CardDetail or ChecklistDetail. Notes show content preview with delete option.

- [ ] **Step 3: Wire into NavGraph**

- [ ] **Step 4: Build and verify + Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/screen/favorites/ app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt
git commit -m "feat: add Favorites screen with notes"
```

---

## Task 17: Settings Screen

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/data/local/SettingsDataStore.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/settings/SettingsViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/settings/SettingsScreen.kt`
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Create SettingsDataStore**

`app/src/main/java/com/lefesafety/liveoff/data/local/SettingsDataStore.kt`:
```kotlin
package com.lefesafety.liveoff.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val NIGHT_MODE = booleanPreferencesKey("night_mode")
        val ANIMATIONS_ENABLED = booleanPreferencesKey("animations_enabled")
    }

    val nightMode: Flow<Boolean> = context.dataStore.data.map { it[Keys.NIGHT_MODE] ?: true }
    val animationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.ANIMATIONS_ENABLED] ?: true }

    suspend fun setNightMode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.NIGHT_MODE] = enabled }
    }

    suspend fun setAnimationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.ANIMATIONS_ENABLED] = enabled }
    }
}
```

- [ ] **Step 2: Create SettingsViewModel + Screen**

Screen with toggle switches for night mode and animations. Version info at bottom.

- [ ] **Step 3: Wire into NavGraph + Build + Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/data/local/SettingsDataStore.kt app/src/main/java/com/lefesafety/liveoff/ui/screen/settings/ app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt
git commit -m "feat: add Settings screen with DataStore preferences"
```

---

## Task 18: SOS Screen

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/sos/SosScreen.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/sos/SosViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/sos/AlarmPlayer.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/sos/FlashlightController.kt`
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Create FlashlightController**

`app/src/main/java/com/lefesafety/liveoff/ui/screen/sos/FlashlightController.kt`:
```kotlin
package com.lefesafety.liveoff.ui.screen.sos

import android.content.Context
import android.hardware.camera2.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashlightController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val cameraId: String? = cameraManager.cameraIdList.firstOrNull()

    fun turnOn() {
        cameraId?.let { cameraManager.setTorchMode(it, true) }
    }

    fun turnOff() {
        cameraId?.let { cameraManager.setTorchMode(it, false) }
    }
}
```

- [ ] **Step 2: Create AlarmPlayer**

Uses MediaPlayer with a loud tone generator or built-in alarm sound. Plays 3 cycles then stops.

- [ ] **Step 3: Create SosViewModel**

Manages state for: alarm playing, flashlight on/off, quiet mode. Actions: toggleAlarm, toggleFlashlight, toggleQuietMode. Provides intents for call/SMS.

- [ ] **Step 4: Create SosScreen**

Dark red background (#1a0000). 2x3 grid of large buttons: Сигнал, Фонарик, Эвакуация (→ checklist), Контакты (→ phone intent), Первая помощь (→ firstaid cards), Тихий режим.

- [ ] **Step 5: Wire into NavGraph + Build + Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/screen/sos/ app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt
git commit -m "feat: add SOS screen with alarm, flashlight, quiet mode"
```

---

## Task 19: Morse Codec (with TDD)

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/domain/morse/MorseCodec.kt`
- Create: `app/src/test/java/com/lefesafety/liveoff/domain/morse/MorseCodecTest.kt`

- [ ] **Step 1: Write failing tests**

`app/src/test/java/com/lefesafety/liveoff/domain/morse/MorseCodecTest.kt`:
```kotlin
package com.lefesafety.liveoff.domain.morse

import org.junit.Assert.assertEquals
import org.junit.Test

class MorseCodecTest {

    private val codec = MorseCodec()

    @Test
    fun `SOS encodes correctly`() {
        assertEquals("... --- ...", codec.encode("SOS"))
    }

    @Test
    fun `Russian A encodes to dot-dash`() {
        assertEquals(".-", codec.encode("А"))
    }

    @Test
    fun `digits encode correctly`() {
        assertEquals("-----", codec.encode("0"))
        assertEquals(".----", codec.encode("1"))
        assertEquals("..---", codec.encode("2"))
    }

    @Test
    fun `full Russian word encodes`() {
        // СОС in Russian letters
        assertEquals("... --- ...", codec.encode("СОС"))
    }

    @Test
    fun `spaces become word separators`() {
        assertEquals("... --- ... / -.. .-", codec.encode("СОС ДА"))
    }

    @Test
    fun `decode reverses encode`() {
        val original = "ПОМОГИТЕ"
        val encoded = codec.encode(original)
        assertEquals(original, codec.decode(encoded))
    }

    @Test
    fun `unknown characters are skipped`() {
        assertEquals(".-", codec.encode("А@А").trim())
        // Only known chars are encoded, unknowns produce empty
    }

    @Test
    fun `getAlphabet returns all Russian letters and digits`() {
        val alphabet = codec.getAlphabet()
        assertEquals(33 + 10, alphabet.size) // 33 Russian letters + 10 digits
    }
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew :app:testDebugUnitTest --tests "com.lefesafety.liveoff.domain.morse.MorseCodecTest"`
Expected: FAIL — class not found

- [ ] **Step 3: Implement MorseCodec**

`app/src/main/java/com/lefesafety/liveoff/domain/morse/MorseCodec.kt`:
```kotlin
package com.lefesafety.liveoff.domain.morse

class MorseCodec {

    private val charToMorse = mapOf(
        // Russian alphabet (ITU mapping)
        'А' to ".-",     'Б' to "-...",   'В' to ".--",    'Г' to "--.",
        'Д' to "-..",    'Е' to ".",      'Ж' to "...-",   'З' to "--..",
        'И' to "..",     'Й' to ".---",   'К' to "-.-",    'Л' to ".-..",
        'М' to "--",     'Н' to "-.",     'О' to "---",    'П' to ".--.",
        'Р' to ".-.",    'С' to "...",    'Т' to "-",      'У' to "..-",
        'Ф' to "..-.",   'Х' to "....",   'Ц' to "-.-.",   'Ч' to "---.",
        'Ш' to "----",   'Щ' to "--.-",   'Ъ' to "--.--",  'Ы' to "-.--",
        'Ь' to "-..-",   'Э' to "..-..",  'Ю' to "..--",   'Я' to ".-.-",
        'Ё' to ".",
        // Digits
        '0' to "-----",  '1' to ".----",  '2' to "..---",  '3' to "...--",
        '4' to "....-",  '5' to ".....",  '6' to "-....",  '7' to "--...",
        '8' to "---..",  '9' to "----.",
        // Latin (SOS compatibility)
        'S' to "...",    'O' to "---"
    )

    private val morseToChar = charToMorse.entries
        .groupBy({ it.value }, { it.key })
        .mapValues { (_, chars) -> chars.first() }

    fun encode(text: String): String =
        text.uppercase()
            .split(" ")
            .joinToString(" / ") { word ->
                word.mapNotNull { char -> charToMorse[char] }
                    .joinToString(" ")
            }

    fun decode(morse: String): String =
        morse.split(" / ").joinToString(" ") { word ->
            word.split(" ").mapNotNull { code -> morseToChar[code] }.joinToString("")
        }

    fun getAlphabet(): List<Pair<Char, String>> =
        charToMorse.entries
            .filter { it.key in 'А'..'Я' || it.key == 'Ё' || it.key in '0'..'9' }
            .map { it.key to it.value }
            .sortedBy { (char, _) ->
                when {
                    char in 'А'..'Я' -> char.code
                    char == 'Ё' -> 'Е'.code + 1
                    else -> char.code + 10000
                }
            }
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew :app:testDebugUnitTest --tests "com.lefesafety.liveoff.domain.morse.MorseCodecTest"`
Expected: ALL TESTS PASS

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/domain/morse/ app/src/test/java/com/lefesafety/liveoff/domain/morse/
git commit -m "feat: add MorseCodec with Russian alphabet, digits, TDD"
```

---

## Task 20: Morse Player

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/components/MorsePlayer.kt`

- [ ] **Step 1: Create MorsePlayer**

`app/src/main/java/com/lefesafety/liveoff/ui/components/MorsePlayer.kt`:
```kotlin
package com.lefesafety.liveoff.ui.components

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.lefesafety.liveoff.ui.screen.sos.FlashlightController
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

enum class MorseOutputMode { SOUND, FLASHLIGHT, VIBRATION }

@Singleton
class MorsePlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val flashlightController: FlashlightController
) {
    private var isPlaying = false
    private val dotDuration = 200L  // ms — base unit

    fun stop() {
        isPlaying = false
        flashlightController.turnOff()
    }

    suspend fun play(morseCode: String, mode: MorseOutputMode) {
        isPlaying = true
        for (char in morseCode) {
            if (!isPlaying) break
            when (char) {
                '.' -> {
                    signalOn(mode)
                    delay(dotDuration)
                    signalOff(mode)
                    delay(dotDuration) // inter-element gap
                }
                '-' -> {
                    signalOn(mode)
                    delay(dotDuration * 3)
                    signalOff(mode)
                    delay(dotDuration) // inter-element gap
                }
                ' ' -> delay(dotDuration * 2) // inter-char gap (total 3 with element gap)
                '/' -> delay(dotDuration * 4) // inter-word gap (total 7 with surrounding spaces)
            }
        }
        isPlaying = false
    }

    private fun signalOn(mode: MorseOutputMode) {
        when (mode) {
            MorseOutputMode.FLASHLIGHT -> flashlightController.turnOn()
            MorseOutputMode.SOUND -> {
                val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD)
            }
            MorseOutputMode.VIBRATION -> {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    vm.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(dotDuration * 3, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(dotDuration * 3)
                }
            }
        }
    }

    private fun signalOff(mode: MorseOutputMode) {
        when (mode) {
            MorseOutputMode.FLASHLIGHT -> flashlightController.turnOff()
            else -> {} // sound/vibration auto-stop
        }
    }
}
```

- [ ] **Step 2: Verify compilation + Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/components/MorsePlayer.kt
git commit -m "feat: add MorsePlayer with flashlight, sound, vibration modes"
```

---

## Task 21: Morse Screens (Alphabet, Trainer, Transmitter)

**Files:**
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/morse/MorseAlphabetViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/morse/MorseAlphabetScreen.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/morse/MorseTrainerViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/morse/MorseTrainerScreen.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/morse/MorseTransmitViewModel.kt`
- Create: `app/src/main/java/com/lefesafety/liveoff/ui/screen/morse/MorseTransmitScreen.kt`
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Create MorseAlphabetScreen**

Grid of characters from `MorseCodec.getAlphabet()`. Each cell shows letter + morse code. Tap plays via MorsePlayer (sound by default). Bottom navigation buttons: "Тренажёр", "Передатчик".

- [ ] **Step 2: Create MorseTrainerScreen**

Shows random letter in large text. Two buttons: "Точка" (.) and "Тире" (-). User taps sequence, then "Проверить". ViewModel compares input with expected morse. Shows green/red feedback. Score counter. "Следующая" button.

- [ ] **Step 3: Create MorseTransmitScreen**

TextField for text input. Radio buttons for output mode (flashlight/sound/vibration). "Передать" button starts playback. Quick signal buttons: SOS, Помогите, Да, Нет — each triggers immediate playback.

- [ ] **Step 4: Wire into NavGraph**

Replace MorseAlphabet, MorseTrainer, MorseTransmit placeholders.

- [ ] **Step 5: Build and verify**

Run: `./gradlew assembleDebug`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/lefesafety/liveoff/ui/screen/morse/ app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt
git commit -m "feat: add Morse screens — alphabet, trainer, transmitter"
```

---

## Task 22: Final Integration & Polish

**Files:**
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/navigation/NavGraph.kt` (remove all remaining Placeholder calls)
- Modify: `app/src/main/java/com/lefesafety/liveoff/ui/MainActivity.kt`

- [ ] **Step 1: Verify no Placeholder composables remain in NavGraph**

All routes should point to real screens. Delete the `Placeholder` function from NavGraph.kt.

- [ ] **Step 2: Remove old test files that reference deleted code**

Delete `app/src/test/java/com/lefesafety/liveoff/ExampleUnitTest.kt` and `app/src/androidTest/java/com/lefesafety/liveoff/ExampleInstrumentedTest.kt` if they fail to compile.

- [ ] **Step 3: Full build**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Run all tests**

Run: `./gradlew testDebugUnitTest`
Expected: ALL PASS

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "feat: complete LiveOFF MVP — all screens integrated"
```
