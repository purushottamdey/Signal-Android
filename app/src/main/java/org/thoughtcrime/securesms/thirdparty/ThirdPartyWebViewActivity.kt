package org.thoughtcrime.securesms.thirdparty

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.thoughtcrime.securesms.keyvalue.SignalStore
import org.thoughtcrime.securesms.main.ThirdPartyContextAction

class ThirdPartyWebViewActivity : ComponentActivity() {
  companion object {
    const val EXTRA_ACTION = "extra_action"
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val actionName = intent.getStringExtra(EXTRA_ACTION) ?: ThirdPartyContextAction.SWIGGY.name
    val action = runCatching { ThirdPartyContextAction.valueOf(actionName) }.getOrDefault(ThirdPartyContextAction.SWIGGY)

    val url = when (action) {
      ThirdPartyContextAction.SWIGGY -> "https://www.swiggy.com/"
      ThirdPartyContextAction.ZOMATO -> "https://www.zomato.com/"
      ThirdPartyContextAction.UBER -> "https://m.uber.com/"
    }

    val phoneE164 = SignalStore.account.e164

    setContent {
      Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        val webViewState = remember { mutableStateOf<WebView?>(null) }

        DisposableEffect(Unit) {
          val manager = CookieManager.getInstance()
          manager.setAcceptCookie(true)
          webViewState.value?.let { manager.setAcceptThirdPartyCookies(it, true) }
          onDispose { }
        }

        AndroidWebView(
          url = url,
          onWebViewReady = { webView ->
            webViewState.value = webView

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            // Prefill phone number if fields exist
            phoneE164?.let { number ->
              webView.evaluateJavascript(
                """
                (function(){
                  try {
                    var inputs = document.querySelectorAll('input[type=tel], input[name*=phone], input[id*=phone]');
                    if (inputs && inputs.length > 0) {
                      inputs[0].value = '${number.replace("+", "")}';
                      inputs[0].dispatchEvent(new Event('input', { bubbles: true }));
                    }
                  } catch(e) {}
                })();
                """.trimIndent(),
                ValueCallback { }
              )
            }
          }
        )
      }
    }
  }
}

@Composable
private fun AndroidWebView(url: String, onWebViewReady: (WebView) -> Unit) {
  val webView = remember { WebView(androidx.compose.ui.platform.LocalContext.current) }

  DisposableEffect(Unit) {
    val parent = webView.parent
    if (parent is ViewGroup) parent.removeView(webView)

    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
      }
    }
    webView.webChromeClient = object : WebChromeClient() {}

    onWebViewReady(webView)

    webView.loadUrl(url)

    onDispose {
      webView.stopLoading()
      webView.destroy()
    }
  }
}