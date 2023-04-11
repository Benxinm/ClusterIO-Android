package com.benxinm.localoss.ui

import CameraViewPermission
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.benxinm.localoss.ui.components.AnimatedCircleButton
import com.benxinm.localoss.ui.components.FloatingButton
import com.benxinm.localoss.ui.components.NavigationBar
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.pages.*
import com.benxinm.localoss.viewModel.BucketViewModel
import com.benxinm.localoss.viewModel.TransportViewModel
import com.benxinm.localoss.viewModel.UserViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LocalOSSApp(onImageCrop:(Uri)->Unit) {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val allPages = Page.values().copyOfRange(fromIndex = 0, toIndex = 2).toList()
    val currentPage = Page.fromRoute(backStackEntry.value?.destination?.route)
    val bucketViewModel:BucketViewModel = viewModel()
    val transportViewModel:TransportViewModel = viewModel()
    val userViewModel:UserViewModel= viewModel()
    var expanded by remember {
        mutableStateOf(false)
    }
    var check by remember {
        mutableStateOf(true)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AnimatedVisibility(visible = check, exit = scaleOut(), enter = scaleIn()) {
                FloatingButton(expanded){
                    expanded=!expanded
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            NavigationBar(allPages = allPages, onTabSelected = { page ->
                navController.navigate(page.name)
            }, currentPage =currentPage, boolean = check)
        }) { paddingValues ->
        NavHost(navController = navController, startDestination = Page.Login.name, modifier = Modifier.padding(paddingValues)){
            composable(Page.Login.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                LoginPage(navController = navController, userViewModel =userViewModel )
            }
            composable(Page.Buckets.name){
                LaunchedEffect(key1 = true){
                    check=true
                }
                BucketManagement(navController,bucketViewModel,userViewModel.token)
            }
            composable(Page.Transport.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                TransportPage(navController,transportViewModel)
            }
            composable(Page.Detail.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                BucketDetail(navController,userViewModel.token,bucketViewModel,transportViewModel)
            }
            composable(Page.Me.name){
                LaunchedEffect(key1 = true){
                    check=true
                }
                expanded=false
                MePage(navController,userViewModel.token, onImageCrop = onImageCrop)
            }
            composable(Page.CreateBucket.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                CreateBucket(navController = navController,userViewModel.token)
            }
            composable(Page.PermissionManagement.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                PermissionManagement(navController = navController,bucketViewModel,userViewModel.token)
            }
            composable(Page.UserPermissionManagement.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                UserPermissionManagement(navController = navController,userViewModel.token,bucketViewModel)
            }
            composable(Page.AddUser.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                AddUserPage(navController = navController, token = userViewModel.token, bucketId =bucketViewModel.currentBucket!!.id )
            }
            composable(Page.Camera.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                CameraViewPermission(navController = navController,userViewModel.token,bucketViewModel)
            }
            composable(Page.Upload.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                UploadPage(navController = navController, token = userViewModel.token, bucketViewModel =bucketViewModel ,transportViewModel)
            }
            composable(Page.SelectBucket.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                SelectBucket(navController = navController, bucketViewModel = bucketViewModel)
            }
            composable(Page.BackupPage.name){
                LaunchedEffect(key1 = true){
                    check=false
                }
                expanded=false
                BackupPage(navController = navController,userViewModel.token)
            }
        }
        if (expanded){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(listOf(Color.LightGray, Color.LightGray)),
                    alpha = 0.6f
                )) {
            }
        }
        AnimatedCircleButton(expanded = expanded,bucketViewModel,navController){
            expanded=!expanded
        }
    }
}

