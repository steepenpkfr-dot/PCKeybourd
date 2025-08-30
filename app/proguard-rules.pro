# ============================
# ProGuard Rules for PCKeyboard
# ============================

# نگه داشتن تمام کلاس‌ها و کدهای داخل پکیج پروژه
-keep class com.example.pckeyboard.** { *; }

# جلوگیری از حذف یا تغییر نام سرویس کیبورد
-keep public class * extends android.inputmethodservice.InputMethodService
-keep public class * extends android.inputmethodservice.KeyboardView

# جلوگیری از حذف annotation ها
-keepattributes *Annotation*

# جلوگیری از خطا هنگام reflection
-keepclassmembers class * {
    public <init>(...);
}

# جلوگیری از حذف Log ها (اختیاری، برای دیباگ)
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}