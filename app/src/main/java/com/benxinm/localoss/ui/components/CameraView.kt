package com.benxinm.localoss.ui.components

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.benxinm.localoss.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Composable
fun CameraView(
    outputDirectory: File,
    executor: Executor,
    navController: NavController,
    onImageCaptured: (File) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current
    val preview=Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val imageCapture:ImageCapture= remember {
        ImageCapture.Builder().build()
    }
    val cameraSelector=CameraSelector.Builder().requireLensFacing(lensFacing).build()
    LaunchedEffect(key1 = lensFacing){
        val cameraProvider=context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,cameraSelector,preview,imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({previewView}, modifier = Modifier.fillMaxSize())
        IconButton(modifier = Modifier.padding(bottom = 20.dp),onClick = {
            takePhoto(
                filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                imageCapture=imageCapture,
                outputDirectory=outputDirectory,
                executor = executor,
                onImageCaptured=onImageCaptured,
                onError=onError
                )
//            navController.popBackStack()
        }) {
            Icon(painter = painterResource(id = R.drawable.ic_lens), contentDescription ="拍照", tint = Color.White, modifier = Modifier.size(100.dp).padding(1.dp).border(1.dp,
                Color.White, CircleShape))
        }
    }
}

private fun takePhoto(
    filenameFormat:String,
    imageCapture: ImageCapture,
    outputDirectory:File,
    executor: Executor,
    onImageCaptured:(File)->Unit,
    onError:(ImageCaptureException)->Unit
){
    val photoFile=File(
        outputDirectory,
        "app_"+SimpleDateFormat(filenameFormat, Locale.CHINA).format(System.currentTimeMillis())+".jpg"
    )
    val outputOptions =ImageCapture.OutputFileOptions.Builder(photoFile).build()
    imageCapture.takePicture(outputOptions,executor,object :ImageCapture.OnImageSavedCallback{
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            onImageCaptured(photoFile)
        }

        override fun onError(exception: ImageCaptureException) {
            onError(exception)
        }
    })
}
private suspend fun Context.getCameraProvider():ProcessCameraProvider= suspendCoroutine {continuation ->
    ProcessCameraProvider.getInstance(this).also {cameraProvider->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        },ContextCompat.getMainExecutor(this))
    }
}