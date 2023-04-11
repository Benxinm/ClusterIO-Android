package com.benxinm.localoss.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.theme.MainColor

@Composable
fun NavigationBar(allPages: List<Page>,
                  onTabSelected: (Page) -> Unit,
                  currentPage: Page,
                  boolean: Boolean
) {
    AnimatedVisibility(
        visible = boolean, enter = slideInVertically(initialOffsetY = { it })
    ){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp), elevation = 22.dp
        ) {
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
            ) {
                allPages.forEach { page ->
                    PageTab(
                        text = page.text,
                        onSelected = { onTabSelected(page) },
                        selected = currentPage == page,
                        iconInt = page.iconInt,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
@Composable
private fun PageTab(
    text: String,
    onSelected: () -> Unit,
    selected: Boolean,
    iconInt: Int,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(50))
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
            .clearAndSetSemantics { contentDescription = text },
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Icon(painter = painterResource(id = iconInt),contentDescription = text, tint = if (selected) MainColor else Color.LightGray,
            modifier = Modifier.height(25.dp))
        Text(text = text, fontSize = 12.sp)
    }
}