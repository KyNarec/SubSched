# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontobfuscate

# KTOR
# Keep Ktor classes for the Local Connect feature.
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { *; }

# Keep Netty classes for the Local Connect feature.
-keep class io.netty.** { *; }
-keepclassmembers class io.netty.** { *; }
# Ignore warnings related to io.netty package (e.g., unused classes or reflection issues)
-dontwarn io.netty.**

# KOIN
# Keep annotation definitions
-keep class org.koin.core.annotation.** { *; }
# Keep classes annotated with Koin annotations
-keep @org.koin.core.annotation.* class * { *; }
