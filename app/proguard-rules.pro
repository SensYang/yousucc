# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\ProgramFiles\adt-bundle-windows-x86_64_20140101\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#
#-optimizationpasses 5
#
#-dontusemixedcaseclassnames
#
#-dontskipnonpubliclibraryclasses
#
#-dontoptimize
#-dontshrink
#-dontpreverify
#-verbose
#-dontobfuscate
#
#-dontwarn com.baidu.**
#
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
#-keep public class * extends android.app.Activity
#
#-keep public class * extends android.app.Application
#
#-keep public class * extends android.app.Service
#
#-keep public class * extends android.content.BroadcastReceiver
#
#-keep public class * extends android.content.ContentProvider
#
#-keep public class * extends android.app.backup.BackupAgentHelper
#
#-keep public class * extends android.preference.Preference
#####################################################################################################
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#-keep public class com.tencent.bugly.**{*;}
-dontwarn android.support.**
#云通讯SDK
-keep class com.yuntongxun.ecsdk.** {*; }

#######################  趣拍  #############################
-dontobfuscate
-keep class com.duanqu.qupai.jni.Releasable
-keep class com.duanqu.qupai.jni.ANativeObject
-dontwarn com.google.common.primitives.**
-dontwarn com.google.common.cache.**
-dontwarn com.google.auto.common.**
-dontwarn com.google.auto.factory.processor.**
-dontwarn com.fasterxml.jackson.**
-dontwarn net.jcip.annotations.**
-dontwarn javax.annotation.**
-dontwarn org.apache.http.client.utils.URIUtils
-keep class javax.annotation.** { *; }

-keep class * extends com.duanqu.qupai.jni.ANativeObject
-keep @com.duanqu.qupai.jni.AccessedByNative class *
-keep class com.duanqu.qupai.bean.DIYOverlaySubmit
-keep public interface com.duanqu.qupai.android.app.QupaiServiceImpl$QupaiService {*;}
-keep class com.duanqu.qupai.android.app.QupaiServiceImpl

-keep class com.duanqu.qupai.BeautySkinning
-keep class com.duanqu.qupai.render.BeautyRenderer
-keep public interface com.duanqu.qupai.render.BeautyRenderer$Renderer {*;}
-keepclassmembers @com.duanqu.qupai.jni.AccessedByNative class * {
    *;
}
-keepclassmembers class * {
    @com.duanqu.qupai.jni.AccessedByNative *;
}
-keepclassmembers class * {
    @com.duanqu.qupai.jni.CalledByNative *;
}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclassmembers class * {
    native <methods>;
}
-keepclassmembers class com.duanqu.qupai.** {
    *;
}

-keep class com.duanqu.qupai.recorder.EditorCreateInfo$VideoSessionClientImpl {
    *;
}
-keep class com.duanqu.qupai.recorder.EditorCreateInfo$SessionClientFctoryImpl {
    *;
}
-keep class com.duanqu.qupai.recorder.EditorCreateInfo{
    *;
}

-keepattributes Signature
-keepnames class com.fasterxml.jackson.** { *; }
##############################  趣拍 end  ################################
##############################  高德  ################################
-keep class com.amap.api.location.**{*;}

-keep class com.amap.api.fence.**{*;}

-keep class com.autonavi.aps.amapapi.model.**{*;}
##############################  趣拍 end  ################################
