package org.thoughtcrime.securesms.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.thoughtcrime.securesms.R

@Composable
fun ThirdPartyActions(
  onOpenSwiggy: () -> Unit,
  onOpenZomato: () -> Unit,
  onOpenUber: () -> Unit,
  onNewChat: (() -> Unit)? = null,
  onNewCall: (() -> Unit)? = null,
  onOpenCamera: (() -> Unit)? = null
) {
  Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
    Text(
      text = stringResource(id = R.string.third_party_actions_title),
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(vertical = 8.dp)
    )

    Divider()

    Spacer(modifier = Modifier.height(8.dp))

    // Existing actions section (if provided)
    if (onNewChat != null || onNewCall != null || onOpenCamera != null) {
      onNewChat?.let {
        Text(
          text = stringResource(id = R.string.conversation_list_fragment__fab_content_description),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = it)
            .padding(vertical = 16.dp)
        )
        Divider()
      }

      onNewCall?.let {
        Text(
          text = stringResource(id = R.string.CallLogFragment__start_a_new_call),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = it)
            .padding(vertical = 16.dp)
        )
        Divider()
      }

      onOpenCamera?.let {
        Text(
          text = stringResource(id = R.string.conversation_list_fragment__open_camera_description),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = it)
            .padding(vertical = 16.dp)
        )
        Divider()
      }
    }

    // Third-party actions
    Text(
      text = stringResource(id = R.string.third_party_actions_swiggy),
      style = MaterialTheme.typography.bodyLarge,
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onOpenSwiggy)
        .padding(vertical = 16.dp)
    )

    Divider()

    Text(
      text = stringResource(id = R.string.third_party_actions_zomato),
      style = MaterialTheme.typography.bodyLarge,
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onOpenZomato)
        .padding(vertical = 16.dp)
    )

    Divider()

    Text(
      text = stringResource(id = R.string.third_party_actions_uber),
      style = MaterialTheme.typography.bodyLarge,
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onOpenUber)
        .padding(vertical = 16.dp)
    )
  }
}