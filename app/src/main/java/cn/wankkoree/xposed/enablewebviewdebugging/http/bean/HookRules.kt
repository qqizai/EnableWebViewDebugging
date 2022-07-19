package cn.wankkoree.xposed.enablewebviewdebugging.http.bean

import com.google.gson.annotations.SerializedName
// TODO: 添加更多 hook 方法
data class HookRules (
    @SerializedName("hookWebView")
    val hookWebView: List<HookRuleWebView>?,
    @SerializedName("hookWebViewClient")
    val hookWebViewClient: List<HookRuleWebViewClient>?,
    @SerializedName("replaceNebulaUCSDK")
    val replaceNebulaUCSDK: List<ReplaceNebulaUCSDK>?,
    @SerializedName("hookCrossWalk")
    val hookCrossWalk: List<HookCrossWalk>?,
    @SerializedName("hookXWebPreferences")
    val hookXWebPreferences: List<HookXWebPreferences>?,
    @SerializedName("hookXWebView")
    val hookXWebView: List<HookXWebView>?,
) {
    class HookRule (
        @SerializedName("name")
        val name: String = "",
        @SerializedName("remark")
        val remark: String = "",
    )
    class HookRuleWebView (
        @SerializedName("name")
        val name: String = "",
        @SerializedName("remark")
        val remark: String = "",
        @SerializedName("Class_WebView")
        val Class_WebView: String = "android.webkit.WebView",
        @SerializedName("Method_getSettings")
        val Method_getSettings: String = "getSettings",
        @SerializedName("Method_setWebContentsDebuggingEnabled")
        val Method_setWebContentsDebuggingEnabled: String = "setWebContentsDebuggingEnabled",
        @SerializedName("Method_setJavaScriptEnabled")
        val Method_setJavaScriptEnabled: String = "setJavaScriptEnabled",
        @SerializedName("Method_loadUrl")
        val Method_loadUrl: String = "loadUrl",
        @SerializedName("Method_setWebViewClient")
        val Method_setWebViewClient: String = "setWebViewClient",
    )
    class HookRuleWebViewClient (
        @SerializedName("name")
        val name: String = "",
        @SerializedName("remark")
        val remark: String = "",
        @SerializedName("Class_WebView")
        val Class_WebView: String = "android.webkit.WebView",
        @SerializedName("Class_WebViewClient")
        val Class_WebViewClient: String = "android.webkit.WebViewClient",
        @SerializedName("Method_onPageFinished")
        val Method_onPageFinished: String = "onPageFinished",
        @SerializedName("Method_evaluateJavascript")
        val Method_evaluateJavascript: String = "evaluateJavascript",
        @SerializedName("Class_ValueCallback")
        val Class_ValueCallback: String = "android.webkit.ValueCallback",
    )
    class ReplaceNebulaUCSDK (
        @SerializedName("name")
        val name: String = "",
        @SerializedName("remark")
        val remark: String = "",
        @SerializedName("Class_UcServiceSetup")
        val Class_UcServiceSetup: String = "com.alipay.mobile.nebulauc.impl.UcServiceSetup",
        @SerializedName("Method_updateUCVersionAndSdcardPath")
        val Method_updateUCVersionAndSdcardPath: String = "updateUCVersionAndSdcardPath",
        @SerializedName("Field_sInitUcFromSdcardPath")
        val Field_sInitUcFromSdcardPath: String = "sInitUcFromSdcardPath",
    )
    class HookCrossWalk (
        @SerializedName("name")
        val name: String = "",
        @SerializedName("remark")
        val remark: String = "",
        @SerializedName("Class_XWalkView")
        val Class_XWalkView: String = "org.xwalk.core.XWalkView",
        @SerializedName("Method_getSettings")
        val Method_getSettings: String = "getSettings",
        @SerializedName("Method_setJavaScriptEnabled")
        val Method_setJavaScriptEnabled: String = "setJavaScriptEnabled",
        @SerializedName("Method_loadUrl")
        val Method_loadUrl: String = "loadUrl",
        @SerializedName("Method_setResourceClient")
        val Method_setResourceClient: String = "setResourceClient",
        @SerializedName("Class_XWalkPreferences")
        val Class_XWalkPreferences: String = "org.xwalk.core.XWalkPreferences",
        @SerializedName("Method_setValue")
        val Method_setValue: String = "setValue",
    )
    class HookXWebPreferences (
        @SerializedName("name")
        val name: String = "",
        @SerializedName("remark")
        val remark: String = "",
        @SerializedName("Class_XWebPreferences")
        val Class_XWebPreferences: String = "org.xwalk.core.XWalkPreferences",
        @SerializedName("Method_setValue")
        val Method_setValue: String = "setValue",
    )
    class HookXWebView (
        @SerializedName("name")
        val name: String = "",
        @SerializedName("remark")
        val remark: String = "",
        @SerializedName("Class_XWebView")
        val Class_XWebView: String = "com.tencent.xweb.WebView",
        @SerializedName("Method_initWebviewCore")
        val Method_initWebviewCore: String = "initWebviewCore",
        @SerializedName("Method_isXWalk")
        val Method_isXWalk: String = "isXWalk",
        @SerializedName("Method_isPinus")
        val Method_isPinus: String = "isPinus",
        @SerializedName("Method_isX5")
        val Method_isX5: String = "isX5",
        @SerializedName("Method_isSys")
        val Method_isSys: String = "isSys",
    )
}