package com.benxinm.localoss

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import com.benxinm.localoss.ui.LocalOSSApp
import com.benxinm.localoss.ui.pages.BucketManagement
import com.benxinm.localoss.ui.pages.LoginPage
import com.benxinm.localoss.ui.pages.transferDate
import com.benxinm.localoss.ui.theme.LocalOSSTheme
import com.benxinm.localoss.ui.util.saveMediaToStorage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LocalOSSTheme {
                rememberSystemUiController().setStatusBarColor(
                    Color.Transparent,
                    darkIcons = MaterialTheme.colors.isLight
                )
                Surface(modifier = Modifier.systemBarsPadding()) {
                    LocalOSSApp{sourceUri->
                        val file =File(this.filesDir,"cropped_pic_${transferDate(System.currentTimeMillis())}.jpg")
                        val destinationUri = file.toUri()
                        UCrop.of(sourceUri,destinationUri).start(this)
                        val bitmap: Bitmap? =
                            BitmapFactory.decodeFile(file.path)
                        bitmap?.let { bitmap ->
                            bitmap.saveMediaToStorage(
                                this,
                                bitmap
                            )
                        }
                    }
                }
            }
        }
    }
}

