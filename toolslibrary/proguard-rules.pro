# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android\tools\Android_Studio_Sdk/tools/proguard/proguard-android.txt
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
 ##--- For:fastjson ---
  -dontwarn com.alibaba.fastjson.**
  ##--- For:okio ---
  -dontwarn okio.**

  # otto混淆规则
  -keepclassmembers class ** {
      @com.squareup.otto.Subscribe public *;
      @com.squareup.otto.Produce public *;
  }

# #  ######## greenDao混淆  ##########
 # # -------------------------------------------
 -keep class de.greenrobot.dao.** {*;}
 -keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
     public static Java.lang.String TABLENAME;
 }
 -keep class **$Properties
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keeppackagenames com.ume.commontools.utils.*

  -keep public class *{
         public protected *;
  }