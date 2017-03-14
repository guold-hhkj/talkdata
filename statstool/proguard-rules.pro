# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\sdk/tools/proguard/proguard-android.txt
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


-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-keep public class com.hhkj.talkdata.TalkDataAgent
-keep public class com.hhkj.talkdata.hybrid.TalkDataJavaScriptInterface
-keep public class com.hhkj.talkdata.constant.TradeConstants
-keep class com.hhkj.talkdata.api.**{*;}
-keep class com.hhkj.talkdata.bean.**{*;}
-keep class com.hhkj.talkdata.permission.**{*;}

-keepclassmembers  class com.hhkj.talkdata.TalkDataAgent {  # 保持方法不被混淆
    public static com.hhkj.talkdata.TalkDataAgent getInstance();
    public void sendTradeInfo(java.lang.String, java.lang.String, java.lang.String,java.lang.String, java.lang.String);
    public void onResume();
    public void onPause();
    public void shouldGetPageExistData(java.lang.String);
    public void init(android.content.Context,java.lang.String);
    public void launch(android.content.Context);
    public void setChannelInZip();
    public void onEvent(java.lang.String);
    public void onFlowStart(java.lang.String);
    public void onFlowEnd(java.lang.String);
    public void onFlow(java.lang.String);
}
-keepclassmembers  class com.hhkj.talkdata.hybrid.TalkDataJavaScriptInterface {
    public void sendTradeInfo(java.lang.String, java.lang.String, java.lang.String,java.lang.String, java.lang.String);
    public void shouldGetPageExistData(java.lang.String);
}
-keepclassmembers  class com.hhkj.talkdata.constant.TradeConstants {
    public static final java.lang.String TRADE_BUY;
    public static final java.lang.String TRADE_REDEEM;
}
