package com.tomdev.logopadix.presentationLayer.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.CURRENT_WELCOME_POPUP_VERSION
import com.tomdev.logopadix.CURRENT_WHATS_NEW_POPUP_VERSION
import com.tomdev.logopadix.WELCOME_POPUP_VERSION_KEY
import com.tomdev.logopadix.WHATS_NEW_POPUP_VERSION_KEY
import com.tomdev.logopadix.datastore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsNewDialog(
    modifier: Modifier = Modifier,
    mainHeading: String,
    headingsAndTexts: List<Triple<Int, Int, Int>>,
    btnLabelNext: String,
    btnLabelPrev: String,
    onEnterClick: () -> Unit
) {
    val ctx = LocalContext.current
    val welcomeDataFlow = ctx.datastore.data.map { pref -> pref[WELCOME_POPUP_VERSION_KEY] ?: 0 }
    val oldWelcomePopUpVersion by welcomeDataFlow.collectAsStateWithLifecycle(initialValue = 0)

    val whatsNewDataFlow = ctx.datastore.data.map { pref -> pref[WHATS_NEW_POPUP_VERSION_KEY] ?: 0 }
    val oldWhatsNewPopUpVersion by whatsNewDataFlow.collectAsStateWithLifecycle(initialValue = Int.MAX_VALUE)

    var showWelcomePopUp by rememberSaveable { mutableStateOf(false) }
    var showWhatsNewPopUp by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(oldWhatsNewPopUpVersion) {
        showWelcomePopUp = oldWelcomePopUpVersion < CURRENT_WELCOME_POPUP_VERSION
        showWhatsNewPopUp = oldWhatsNewPopUpVersion < CURRENT_WHATS_NEW_POPUP_VERSION
    }

    val pagerState = rememberPagerState(pageCount = { headingsAndTexts.size })
    val coroutineScope = rememberCoroutineScope()

    if (showWhatsNewPopUp && !showWelcomePopUp) {
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
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (pagerState.currentPage == pagerState.pageCount - 1) {
                            DeleteButton(
                                onClick = {
                                    coroutineScope.launch {
                                        ctx.datastore.edit { pref ->
                                            pref[WHATS_NEW_POPUP_VERSION_KEY] =
                                                CURRENT_WHATS_NEW_POPUP_VERSION
                                        }
                                    }
                                    onEnterClick()
                                }
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = mainHeading,
                        style = MaterialTheme.typography.headlineMedium
                    )

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
                                modifier = Modifier.align(Alignment.CenterHorizontally),
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val currPage = pagerState.currentPage
                                    if (currPage > 0)
                                        pagerState.animateScrollToPage(currPage - 1)
                                }
                            }
                        ) {
                            Text(btnLabelPrev)
                        }
                        Spacer(Modifier.weight(1f))
                        PagerDots(pagerState = pagerState)
                        Spacer(Modifier.weight(1f))
                        Button(
                            enabled = pagerState.currentPage < pagerState.pageCount - 1,
                            onClick = {
                                coroutineScope.launch {
                                    val currPage = pagerState.currentPage
                                    if (currPage < pagerState.pageCount - 1)
                                        pagerState.animateScrollToPage(currPage + 1)
                                }
                            }
                        ) {
                            Text(btnLabelNext)
                        }
                    }
                }
            }
        }
    }
}
