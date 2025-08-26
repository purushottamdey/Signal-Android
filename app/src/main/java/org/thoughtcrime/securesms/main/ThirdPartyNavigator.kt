package org.thoughtcrime.securesms.main

import android.content.Context
import android.content.Intent
import org.thoughtcrime.securesms.thirdparty.ThirdPartyWebViewActivity

enum class ThirdPartyContextAction {
  SWIGGY,
  ZOMATO,
  UBER
}

object ThirdPartyNavigator {
  fun openThirdPartyWeb(context: Context, contextAction: ThirdPartyContextAction) {
    val intent = Intent(context, ThirdPartyWebViewActivity::class.java)
    intent.putExtra(ThirdPartyWebViewActivity.EXTRA_ACTION, contextAction.name)
    context.startActivity(intent)
  }
}