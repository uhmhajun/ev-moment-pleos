package io.pleos.evmoment

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

/**
 * EV MOMENT — Pleos Connect Vehicle Application Activity
 *
 * WebView prototype host with Android platform TextToSpeech and demo JavaScript hooks.
 * Pleos Vehicle/NaviHelper/FusedLocation SDKs and Virtual Console are NOT yet connected.
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var webView: WebView
    private var ttsEngine: TextToSpeech? = null
    private var isTtsReady = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ttsEngine = TextToSpeech(this, this)

        webView = WebView(this)
        setContentView(webView)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false
        settings.allowFileAccess = true

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        // Register JavaScript Interface (`window.PleosBridge`)
        webView.addJavascriptInterface(PleosJavascriptBridge(), "PleosBridge")

        // Load EV MOMENT 17-inch Pleos UI from local assets
        webView.loadUrl("file:///android_asset/ev_moment_pleos_app.html")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            ttsEngine?.language = Locale.KOREAN
            isTtsReady = true
        }
    }

    /**
     * Demo bridge hook for a future telemetry adapter. Not subscribed to Vehicle SDK or Virtual Console.
     */
    fun notifyVirtualConsoleTelemetry(socPercent: Int, remainingMinutes: Int, gearState: String = "P") {
        runOnUiThread {
            webView.evaluateJavascript(
                "if (window.onPleosVehicleUpdate) { window.onPleosVehicleUpdate($socPercent, $remainingMinutes, '$gearState'); }",
                null
            )
        }
    }

    inner class PleosJavascriptBridge {

        @JavascriptInterface
        fun speakTts(message: String) {
            if (isTtsReady) {
                ttsEngine?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "EV_MOMENT_TTS")
            }
        }

        @JavascriptInterface
        fun sendWaypointToNaviHelper(placeName: String, latitude: Double, longitude: Double): Boolean {
            // TODO: Integrate and verify the real Pleos NaviHelper SDK.
            // No waypoint is actually sent. Return false instead of reporting false success.
            return false
        }

        @JavascriptInterface
        fun simulateVirtualConsoleUpdate(socPercent: Int, remainingMinutes: Int) {
            notifyVirtualConsoleTelemetry(socPercent, remainingMinutes, "P")
        }
    }

    override fun onDestroy() {
        ttsEngine?.stop()
        ttsEngine?.shutdown()
        super.onDestroy()
    }
}
