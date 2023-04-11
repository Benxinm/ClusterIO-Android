package com.benxinm.localoss.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.benxinm.localoss.ui.components.ListWithHeader
import com.benxinm.localoss.ui.components.TopBarWithBack
import com.benxinm.localoss.ui.components.TopBarWithBackNoIcon
import com.benxinm.localoss.ui.model.File
import com.benxinm.localoss.ui.model.FileType
import com.benxinm.localoss.ui.theme.LightWhite
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.viewModel.TransportViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TransportPage(navController: NavController, transportViewModel: TransportViewModel) {
    val pagerState = rememberPagerState()
    val pages = TransportPage.values()
    val scope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopBarWithBackNoIcon(
            text = "传输列表",
            navController = navController,
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = MainColor,
                        height = 4.dp

                    )
                }
            ) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        text = { Text(page.title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        unselectedContentColor = MainColor,
                        selectedContentColor = MainColor,
                        modifier = Modifier.background(Color.White)
                    )
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState,
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LightWhite),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (page) {
                        0 -> {
                            if (transportViewModel.uploadedList.isEmpty() && transportViewModel.uploadingList.isEmpty()) {
                                Text(text = "上传列表为空")
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    item {
                                        ListWithHeader("正在上传",fileList = transportViewModel.uploadingList)
                                    }
                                    item {
                                        ListWithHeader("上传完成",fileList = transportViewModel.uploadedList)
                                    }
                                }
                            }
                        }
                        1 -> {
                            if (transportViewModel.downloadedList.isEmpty() && transportViewModel.downloadingList.isEmpty()) {
                                Text(text = "下载列表为空")
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    item {
                                        ListWithHeader("正在下载",fileList = transportViewModel.downloadingList)
                                    }
                                    item {
                                        ListWithHeader("下传完成",fileList = transportViewModel.downloadedList)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class TransportPage(val title: String) {
    Upload("上传列表"), Download("下载列表")
}

