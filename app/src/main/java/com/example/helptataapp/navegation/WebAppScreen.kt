package com.example.helptataapp.navegation

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.helptataapp.session.AppSession
import com.example.helptataapp.session.TokenStore

private const val BASE_WEB_URL = "https://helptata.cl"

private class AndroidBridge(private val ctx: Context, private val onLogout: () -> Unit) {
    @JavascriptInterface
    fun logout() {
        TokenStore.clear(ctx)
        AppSession.token = ""
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(ctx, "Sesión cerrada correctamente.", Toast.LENGTH_SHORT).show()
            onLogout()
        }
    }
}

@Composable
fun WebAppScreen(onLogout: () -> Unit) {
    val url = "$BASE_WEB_URL/auth-callback?token=${Uri.encode(AppSession.token)}"
    val context = LocalContext.current
    val activity = context as? Activity

    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var customView by remember { mutableStateOf<View?>(null) }
    var customViewCallback by remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }

    // Back: ocultar pantalla completa → historial web → minimizar app
    BackHandler {
        when {
            customView != null -> customViewCallback?.onCustomViewHidden()
            webViewRef?.canGoBack() == true -> webViewRef?.goBack()
            else -> activity?.moveTaskToBack(true)
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                addJavascriptInterface(AndroidBridge(ctx, onLogout), "Android")
                webViewClient = WebViewClient()
                webChromeClient = object : WebChromeClient() {
                    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                        customView = view
                        customViewCallback = callback
                        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        val decor = activity?.window?.decorView as? FrameLayout
                        decor?.addView(
                            view,
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )
                        view.visibility = View.VISIBLE
                        // Modo inmersivo: oculta nav bar y status bar
                        // Aparecen al deslizar desde el borde y se ocultan solos
                        @Suppress("DEPRECATION")
                        activity?.window?.decorView?.systemUiVisibility = (
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                        )
                    }

                    override fun onHideCustomView() {
                        val decor = activity?.window?.decorView as? FrameLayout
                        decor?.removeView(customView)
                        customView = null
                        customViewCallback = null
                        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        // Restaurar UI normal al salir de pantalla completa
                        @Suppress("DEPRECATION")
                        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }
                loadUrl(url)
            }.also { webViewRef = it }
        }
    )
}