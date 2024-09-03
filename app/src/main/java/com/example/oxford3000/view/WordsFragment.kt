package com.example.oxford3000.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import androidx.room.RoomDatabase
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.oxford3000.databinding.FragmentWordsBinding
import com.example.oxford3000.model.Word
import com.example.oxford3000.model.WordsSelected
import com.example.oxford3000.service.ImageApiService
import com.example.oxford3000.service.WordDatabase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random


class WordsFragment : Fragment(),TextToSpeech.OnInitListener {

    private val imageApiService = ImageApiService()
    private var tts:TextToSpeech?=null

    private var _binding: FragmentWordsBinding? = null
    private val binding
        get() = _binding!!

    private var random=Random.nextInt(0,3000)
    private var word:Word?=null
    private lateinit var db:WordDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentWordsBinding.inflate(inflater, container, false)
        val view = binding.root



        return view


    }

    @SuppressLint("DiscouragedApi")
    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tts=TextToSpeech(binding.root.context,this)

        word= wordJson(random.toString())
        binding.word = word

        lifecycleScope.launch {
           val bitmapImage= imageApiService.imageResource(word!!.eng,binding.root.context)
            binding.imageView.setImageBitmap(bitmapImage)

        }

        binding.changeWord.setOnClickListener{
            random= Random.nextInt(0,3000)
            word= wordJson(random.toString())
            binding.word = word

            lifecycleScope.launch {
                val bitmapImage= imageApiService.imageResource(word!!.eng,binding.root.context)
                binding.imageView.setImageBitmap(bitmapImage)

            }
        }

        binding.listenWord.setOnClickListener{
            Speak()
        }

        binding.saveWord.setOnClickListener {
            try {
                lifecycleScope.launch {
                    db= Room.databaseBuilder(requireContext(),WordDatabase::class.java,"WordDatabase").build()
                    val userDao=db.wordDao()
                    val wordsSelected=WordsSelected(eng = word!!.eng, tr = word!!.tr)
                    userDao.insert(wordsSelected)
                }

            }catch (e:Exception){
                println(e.printStackTrace())
            }

        }




    }

    private fun Speak() {
        tts!!.speak(word?.eng!!.toString(),TextToSpeech.QUEUE_FLUSH,null,"")
    }
    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){
            val result=tts!!.setLanguage(java.util.Locale.US)
            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                println("Language not supported")
            }else{
                binding.listenWord.isEnabled=true
            }
        }else{
            println("TTS Initialization Failed")
        }
    }


    private fun  wordJson(id:String):Word{
        val jsonData = requireActivity().applicationContext.resources.openRawResource(
            requireActivity().applicationContext.resources.getIdentifier(
                "db",
                "raw",
                requireActivity().applicationContext.packageName
            )
        ).bufferedReader().use { it.readText() }

        val outputJson = JSONObject(jsonData)

        val users = outputJson.getJSONArray("words") as JSONArray
        val word: Word = Word(
            id.let { users.getJSONObject(it.toInt()).getString("id") }.toString(),
            id.let { users.getJSONObject(it.toInt()).getString("categoryId") }.toString(),
            id.let { users.getJSONObject(it.toInt()).getString("ENG") }.toString(),
            id.let { users.getJSONObject(it.toInt()).getString("TR") }.toString(),

            )
        return word
    }



    override fun onDestroy() {
        if (tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        super.onDestroy()

    }
}







private suspend fun setImageURI(url:String,context: Context) :Bitmap{
    val loader= ImageLoader(context )
    val request=ImageRequest.Builder(context)
        .data(url)
        .build()

    val result=(loader.execute(request) as SuccessResult).drawable
    //val bitmap=result.toBitmap(300,300)
    //(result as BitmapDrawable).bitmap

    return (result as BitmapDrawable).bitmap


}






/*
    @OptIn(DelicateCoroutinesApi::class)
    private fun getDataAPI(q:String)=GlobalScope.async{


        imageApiService.imageData(q).enqueue(object : Callback<ApiResponse>{
            override fun onResponse(p0: Call<ApiResponse>, p1: Response<ApiResponse>) {
               if (p1.isSuccessful){

                    p1.body()?.let { it ->
                        for (i in it.hits){


                        }
                    }
               }
           }

            override fun onFailure(p0: Call<ApiResponse>, p1: Throwable) {
                TODO("Not yet implemented")
            }


        })

        /*
        try {


            disposable.add(
                imageApiService.imageData(q)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object :DisposableSingleObserver<List<ImageResult>>(){
                        override fun onSuccess(t: List<ImageResult>) {
                            println(t)
                        }

                        override fun onError(e: Throwable) {
                            println(e.message)
                        }
                    })
            )



        }catch (e:Exception){
            println(e.printStackTrace())
        }
*/
    }




}










/*
    private fun getDataAPI(){
        disposable.add(
            countryApiService.imageSearch()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        println(t)
                    }

                    override fun onError(e: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        )

    }
*/




/*
            for (i in 0 until users.length()){
                val id=users.getJSONObject(i).getString("id")
                val categoryId=users.getJSONObject(i).getString("categoryId")
                val eng=users.getJSONObject(i).getString("ENG")
                val tr=users.getJSONObject(i).getString("TR")
                wordList.add(Word(id,categoryId,eng,tr))
            }
            val word=wordList[1].id

            val bundle = bundleOf("id" to word)
            findNavController().navigate(R.id.action_baseFragment_to_wordsFragment,bundle)
    */