package com.groupec.cleanarchitecturesampleapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.groupec.cleanarchitecturesampleapp.core.designsystem.icon.AppIcons
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.Black
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.CleanArchitectureSampleAppTheme
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.White
import com.groupec.cleanarchitecturesampleapp.core.model.data.Attachment

@Composable
fun AttachmentCard(
    item: Attachment,
    onDownload: (Int, String) -> Unit,
    onDelete: (Int, String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(item.name) },
        supportingContent = {
            Text(
                text = stringResource(
                    R.string.create_date,
                    item.datecreation
                )
            )
        },
        trailingContent = {
            IconButton(onClick = { expanded = true }) {
                Icon(AppIcons.MoreVert, contentDescription = "More options")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(White)
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = AppIcons.Download,
                            contentDescription = "Edit Icon"
                        )
                    },
                    text = { Text(stringResource(R.string.download_item), color = Black) },
                    onClick = {
                        onDownload(item.id, item.name)
                        expanded = false // Close DropdownMenuItem
                    }
                )
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = AppIcons.Delete,
                            contentDescription = "Delete Icon"
                        )
                    },
                    text = { Text(stringResource(R.string.delete_item), color = Black) },
                    onClick = {
                        onDelete(item.id, item.name)
                        expanded = false // Close DropdownMenuItem
                    }
                )
            }
        }
    )
    HorizontalDivider()
}

@Preview
@Composable
fun OrderCardPreview() {
    CleanArchitectureSampleAppTheme {
        Attachment(
            id = 1,
            name = "my_file",
            datecreation = "2025-04-25 10:12:00"
        )
    }
}
