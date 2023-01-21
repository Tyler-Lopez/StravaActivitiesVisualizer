package com.activityartapp.presentation.editArtScreen.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.LowEmphasisButton
import com.activityartapp.presentation.common.type.Body
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.*
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.R

@Composable
fun EditArtDialogNavigateUp(
    eventReceiver: EventReceiver<EditArtViewEvent>,
    isVisible: Boolean
) {
    /** Only intercept when the dialog box is not visible **/
    BackHandler(enabled = !isVisible) {
        eventReceiver.onEvent(NavigateUpClicked)
    }

    if (isVisible) {
        Dialog(
            onDismissRequest = {
                eventReceiver.onEvent(DialogNavigateUpCancelled)
            },
            content = {
                Card {
                    Column(
                        modifier = Modifier.padding(
                            start = spacing.medium,
                            end = spacing.medium,
                            top = spacing.medium,
                            bottom = spacing.small
                        ),
                        verticalArrangement = Arrangement.spacedBy(spacing.medium)
                    ) {
                        Body(text = stringResource(R.string.edit_art_dialog_exit_confirmation_prompt))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            LowEmphasisButton(
                                size = ButtonSize.SMALL,
                                text = stringResource(R.string.edit_art_dialog_exit_confirmation_no)
                            ) { eventReceiver.onEvent(DialogNavigateUpCancelled) }
                            Spacer(modifier = Modifier.width(spacing.medium))
                            LowEmphasisButton(
                                size = ButtonSize.SMALL,
                                text = stringResource(R.string.edit_art_dialog_exit_confirmation_yes)
                            ) { eventReceiver.onEvent(DialogNavigateUpConfirmed) }
                        }
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}