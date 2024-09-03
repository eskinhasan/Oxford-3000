package com.example.oxford3000.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.oxford3000.util.Words.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


class ImageApiService {

    private var bitmapImage:Bitmap?=null

    @Singleton
    private val retrofit=Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ImageApi::class.java)

 suspend fun imageResource(name: String,context:Context)=
     withContext(context = Dispatchers.IO) {

         val response = retrofit.searchImages(name)
         withContext(Dispatchers.Main) {
             if (response.isSuccessful) {
                 response.body()?.let {
                     val url = it.hits.get(1).previewURL
                     bitmapImage = setImageURI(url, context)
                 }
             }
         }

         return@withContext bitmapImage
     }




    private suspend fun setImageURI(url:String,context: Context) :Bitmap{
        val loader= ImageLoader(context )
        val request= ImageRequest.Builder(context)
            .data(url)
            .build()



        val result=(loader.execute(request) as SuccessResult).drawable
        //val bitmap=result.toBitmap(300,300)
        //(result as BitmapDrawable).bitmap

        return (result as BitmapDrawable).bitmap


    }



}