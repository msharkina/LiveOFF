# ============================================================
# LiveOFF ProGuard / R8 Rules
# ============================================================

# --- Kotlinx Serialization ---
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.lefesafety.liveoff.**$$serializer { *; }
-keepclassmembers class com.lefesafety.liveoff.** {
    *** Companion;
}
-keepclasseswithmembers class com.lefesafety.liveoff.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# --- Room ---
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# --- Hilt ---
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel

# --- Compose ---
-dontwarn androidx.compose.**

# --- Coroutines ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# --- General Android ---
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
