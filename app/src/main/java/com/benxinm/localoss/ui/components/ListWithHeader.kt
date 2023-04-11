package com.benxinm.localoss.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benxinm.localoss.ui.model.File
import com.benxinm.localoss.ui.pages.transferDate
import com.benxinm.localoss.ui.theme.LightMainColor

@Composable
fun ListWithHeader(text:String,fileList:List<File>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()){
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight().background(LightMainColor)) {
            Text(text = text + " (${fileList.size})", modifier = Modifier.padding(vertical = 3.dp))
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight().padding(horizontal = 15.dp)){
            fileList.forEach{file ->
                FileView(date = transferDate(file.date), name = file.name, size =file.size , type =file.type ) {

                }
            }
        }
    }
}