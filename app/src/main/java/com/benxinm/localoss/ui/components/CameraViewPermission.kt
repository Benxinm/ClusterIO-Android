import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.benxinm.localoss.ui.components.CameraView
import com.benxinm.localoss.ui.util.saveMediaToStorage
import java.util.concurrent.Executors
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.util.Utils
import com.benxinm.localoss.viewModel.BucketViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.security.Permission
import java.util.concurrent.Executor

@Composable
fun CameraViewPermission(
    navController: NavController,
    token:String,bucketViewModel: BucketViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current
    PermissionView(permission = android.Manifest.permission.CAMERA, rationale = "请打开相机权限"){
        val executor=Executors.newSingleThreadExecutor()
        val outputDirectory=context.filesDir
        CameraView(outputDirectory = outputDirectory, executor =executor, navController, onImageCaptured = {file->
            val bitmap: Bitmap? = BitmapFactory.decodeFile(file.absolutePath)
            bitmap?.let { bitmap->
                bitmap.saveMediaToStorage(context,bitmap)
            }
            Handler(Looper.getMainLooper()).post(kotlinx.coroutines.Runnable {
                val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("originMd5", Utils.getFileMD5(file))
                    .addFormDataPart("bucketId",bucketViewModel.currentBucket!!.id.toString()).addFormDataPart("isZip",false.toString())
                val fileBody  = RequestBody.create(MediaType.parse("multipart/form-data"),file)
                builder.addFormDataPart("file",file.name,fileBody)
                val parts = builder.build().parts()
                Repository.uploadSingleFile(token,parts).observe(lifecycleOwner){res->
                    if (res.isSuccess){
                        Toast.makeText(context,"上传成功", Toast.LENGTH_SHORT).show()
                    }else{
                        res.onFailure {
                            Log.e("Network",it.message.toString())
                            Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                navController.popBackStack()
            })
            executor.shutdown()
        }, onError ={
            Log.e("LocalOSS","cameraFail")
        } )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionView(
    permission:String=android.Manifest.permission.CAMERA,
    rationale:String="该功能需要此权限,请打开该权限",
    content:@Composable ()->Unit={}
) {
    val permissionState = rememberPermissionState(permission = permission)
    if (permissionState.status.isGranted){
        content()
    }else{
        Column {
            val textToShow = if (permissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                rationale
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                rationale
            }
            Text(textToShow)
        }
    }
}