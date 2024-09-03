package com.example.oxford3000.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.Flag

@Entity
data class WordsSelected(
    @ColumnInfo("eng")
    val eng:String,
    @ColumnInfo("tr")
    val tr:String,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0

}