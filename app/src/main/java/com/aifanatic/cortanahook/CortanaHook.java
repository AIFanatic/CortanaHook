package com.aifanatic.cortanahook;


import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class CortanaHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static final String TAG = "CortanaHook ";

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.microsoft.cortana")) {
            return;
        }
        // common method hook for textviews
        XC_MethodHook textMethodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                //CharSequence actualText = (CharSequence) methodHookParam.args[0];
                TextView textView = (TextView) methodHookParam.thisObject;

                String id = textView.getResources().getResourceName(textView.getId());

                if (id.equals("com.microsoft.cortana:id/autocompleteTextView")) {
                    String actualText = textView.getText().toString();

                    if (actualText != null) {
                        XposedBridge.log(TAG + actualText + " : " + id);
                    }
                }
            }
        };

        // common method hook for textviews
        XC_MethodHook webViewHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                XposedBridge.log(TAG + "webView:loadUrlA: " + param.args[0]);

                WebView webView = (WebView) param.thisObject;

                webView.getSettings().setJavaScriptEnabled(true);

                webView.evaluateJavascript(
                        "(function() { return document.getElementsByClassName('b_poleContent')[0].innerHTML })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String resp) {
                                try {
                                    File myFile = new File("/sdcard/mysdfile.txt");
                                    myFile.createNewFile();
                                    FileOutputStream fOut = new FileOutputStream(myFile);
                                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                                    XposedBridge.log(TAG + "webView:loadUrlA:HTML: " + resp);

                                    myOutWriter.append(resp);
                                    myOutWriter.close();
                                    fOut.close();
                                } catch (Exception e) {
                                }
                            }
                        });

                webView.evaluateJavascript(
                        "(function() { return document.getElementsByClassName('b_anno b_anim')[0].innerHTML='CortanaHook - Only text, for now..' })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String resp) {
                            }
                        });

                XposedBridge.log(TAG + "webView:loadUrlA: " + webView.getUrl());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

            }
        };
        try {
            XposedBridge.hookMethod(TextView.class.getDeclaredMethod("setText", CharSequence.class, TextView.BufferType.class), textMethodHook);
        } catch (NoSuchMethodException e) {  }

        try {
            XposedBridge.hookMethod(WebView.class.getDeclaredMethod("loadUrl", String.class),webViewHook);
        } catch (NoSuchMethodException e) {  }
    }
}