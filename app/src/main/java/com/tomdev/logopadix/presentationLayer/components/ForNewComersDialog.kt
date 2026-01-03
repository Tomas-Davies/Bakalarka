package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.CURRENT_WELCOME_POPUP_VERSION
import com.tomdev.logopadix.WELCOME_POPUP_VERSION_KEY
import com.tomdev.logopadix.datastore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewComersDialog(
    modifier: Modifier = Modifier,
    headingsAndTexts: List<Triple<Int, Int, Int>>,
    btnLabelNext: String,
    onEnterClick: () -> Unit
) {
    val ctx = LocalContext.current
    val dataFlow = ctx.datastore.data.map { pref -> pref[WELCOME_POPUP_VERSION_KEY] ?: 0 }
    val savedWelcomePopUpVersion by dataFlow.collectAsStateWithLifecycle(initialValue = 0)
    val showWelcomePopUp = savedWelcomePopUpVersion < CURRENT_WELCOME_POPUP_VERSION
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    if (showWelcomePopUp) {
        val scope = rememberCoroutineScope()
        BasicAlertDialog(
            modifier = Modifier
                .padding(top = 18.dp, bottom = 18.dp)
                .then(modifier),
            onDismissRequest = { }
        ) {
            Surface(
                modifier = Modifier
                    .border(
                        3.dp,
                        MaterialTheme.colorScheme.surfaceVariant,
                        AlertDialogDefaults.shape
                    )
                    .fillMaxWidth(),
                shape = AlertDialogDefaults.shape,
                tonalElevation = AlertDialogDefaults.TonalElevation,
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(42.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(pagerState.pageCount) { iteration ->
                                val color =
                                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(12.dp)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ){
                            if (pagerState.currentPage < pagerState.pageCount - 1) {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    }
                                ) {
                                    Text(text = btnLabelNext)
                                }
                            } else {
                                DeleteButton(
                                    onClick = {
                                        scope.launch {
                                            ctx.datastore.edit { pref ->
                                                pref[WELCOME_POPUP_VERSION_KEY] =
                                                    CURRENT_WELCOME_POPUP_VERSION
                                            }
                                        }
                                        onEnterClick()
                                    }
                                )
                            }
                        }
                    }

                    HorizontalPager(
                        modifier = Modifier.fillMaxWidth(),
                        state = pagerState,
                        contentPadding = PaddingValues(top = 18.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(headingsAndTexts[pagerState.currentPage].first),
                                style = MaterialTheme.typography.headlineMedium
                            )
                            HorizontalDivider(Modifier.padding(vertical = 18.dp))
                            Text(
                                text = stringResource(headingsAndTexts[pagerState.currentPage].second)
                            )
                            Spacer(Modifier.height(36.dp))
                            Image(
                                modifier = Modifier.sizeIn(maxHeight = 300.dp),
                                painter = painterResource(headingsAndTexts[pagerState.currentPage].third),
                                contentDescription = "decoration"
                            )
                        }
                    }
                }
            }
        }
    }
}
