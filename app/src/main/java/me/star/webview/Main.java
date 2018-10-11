package me.star.webview;

import android.webkit.WebView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {
    private static final String TAG = "XposedWebView";
    String currentPackageName = "";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        this.currentPackageName = loadPackageParam.packageName;
        XposedBridge.hookAllConstructors(WebView.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                XposedHelpers.callStaticMethod(WebView.class, "setWebContentsDebuggingEnabled", true);
                XposedBridge.log("WebView(): " + Main.this.currentPackageName);
            }
        });
        XposedBridge.hookAllMethods(WebView.class, "setWebContentsDebuggingEnabled", new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.args[0] = true;
                XposedBridge.log("setWebContentsDebuggingEnabled: " + Main.this.currentPackageName);
            }
        });
    }
}
