package com.example.oxford3000.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oxford3000.R
import com.example.oxford3000.adapter.WordAdapter
import com.example.oxford3000.adapter.WordSentencesAdapter
import com.example.oxford3000.databinding.FragmentSentencesBinding
import com.example.oxford3000.databinding.FragmentWordsBinding
import com.example.oxford3000.model.Sentences
import com.example.oxford3000.model.SentencesWord
import com.example.oxford3000.model.Word
import com.example.oxford3000.service.ImageApiService
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale
import kotlin.random.Random


class SentencesFragment : Fragment() ,TextToSpeech.OnInitListener{

    private var _binding:FragmentSentencesBinding?=null
    private val binding
        get() = _binding!!

    private var random= Random.nextInt(0,60)
    private var tts: TextToSpeech?=null

    private val imageApiService = ImageApiService()
    private var wordSentences:Sentences?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentSentencesBinding.inflate(inflater,container,false)

        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tts=TextToSpeech(binding.root.context,this)



        wordSentences=sentencesJson(random.toString())

        binding.sentencesTrText.text= wordSentences!!.trSentences
        binding.sentencesEngText.text= wordSentences!!.engSentences

        lifecycleScope.launch {
            val bitmapImage= imageApiService.imageResource(wordSentences!!.engSentences,binding.root.context)
            binding.sentencesImageView.setImageBitmap(bitmapImage)
        }

       

        binding.sentencesListenButton.setOnClickListener{

            Speak()
        }

        val recyclerView: RecyclerView =view.findViewById(R.id.sentencesCard)
        recyclerView.layoutManager = LinearLayoutManager(context)

        var sentences= wordSentences!!.engSentences.split(" ","?",".","!")
        var sentencesWord= arrayListOf<SentencesWord>()
        for(i in sentences){
            if (i.isNotEmpty()){
                if (wordJson(i).eng!=" "){
                    sentencesWord.add(wordJson(i))
                }

            }
        }


        val tts = TextToSpeech(requireContext()) {
            Log.i("MainActivity", "onCreate: $it")
        }
        tts.language = Locale.US
        val itemsWord= WordSentencesAdapter(tts,sentencesWord)
        recyclerView.adapter=itemsWord



        wordJson(sentences[1])


        binding.SentencesChangeButton.setOnClickListener{
            random= Random.nextInt(0,60)
            wordSentences=sentencesJson(random.toString())

            binding.sentencesTrText.text= wordSentences!!.trSentences
            binding.sentencesEngText.text= wordSentences!!.engSentences

            lifecycleScope.launch {
                val bitmapImage= imageApiService.imageResource(wordSentences!!.engSentences,binding.root.context)
                binding.sentencesImageView.setImageBitmap(bitmapImage)
            }

            val recyclerView: RecyclerView =view.findViewById<RecyclerView>(R.id.sentencesCard)
            recyclerView.layoutManager = LinearLayoutManager(context)

            var sentences= wordSentences!!.engSentences.split(" ","?",".","!")
            var sentencesWord= arrayListOf<SentencesWord>()
            for(i in sentences){
                if (i.isNotEmpty()){
                    if (wordJson(i).eng!=" "){
                        sentencesWord.add(wordJson(i))
                    }

                }
            }

            val itemsWord= WordSentencesAdapter(tts,sentencesWord)
            recyclerView.adapter=itemsWord

            wordJson(sentences[1])




        }


    }
   private fun Speak() {
        tts!!.speak(wordSentences?.engSentences!!.toString(),TextToSpeech.QUEUE_FLUSH,null,"")
    }




    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){
            val result=tts!!.setLanguage(java.util.Locale.US)
            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                println("Language not supported")
            }else{
                binding.sentencesListenButton.isEnabled=true
            }
        }else{
            println("TTS Initialization Failed")
        }
    }

    private fun  wordJson(engWord:String) :SentencesWord{
        var wordSentences :SentencesWord=SentencesWord(engWord,"Değeri yok")
        val jsonData = requireActivity().applicationContext.resources.openRawResource(
            requireActivity().applicationContext.resources.getIdentifier(
                "db",
                "raw",
                requireActivity().applicationContext.packageName
            )
        ).bufferedReader().use { it.readText() }

        val outputJson = JSONObject(jsonData)

        val users = outputJson.getJSONArray("words") as JSONArray
        for (i in users.length() - 1 downTo 0) {
            if (engWord.uppercase() == users.getJSONObject(i).getString("ENG").uppercase()) {

                wordSentences=  SentencesWord(
                        users.getJSONObject(i).getString("ENG"),
                        users.getJSONObject(i).getString("TR")
                    )

            }


        }

        return  wordSentences




    }

    private fun  sentencesJson(id: String): Sentences {
        try {
            val jsonData = requireActivity().applicationContext.resources.openRawResource(
                requireActivity().applicationContext.resources.getIdentifier(
                    /* name = */ "db_sentences",
                    /* defType = */ "raw",
                    /* defPackage = */ requireActivity().applicationContext.packageName
                )
            ).bufferedReader().use { it.readText() }


            val outputJson = JSONObject(jsonData)

            val users = outputJson.getJSONArray("sentences") as JSONArray

            val sentences: Sentences = Sentences(
                id.let { users.getJSONObject(it.toInt()).getString("id") }.toString(),
                id.let { users.getJSONObject(it.toInt()).getString("categoryId") }.toString(),
                id.let { users.getJSONObject(it.toInt()).getString("engSentences") }.toString(),
                id.let { users.getJSONObject(it.toInt()).getString("trSentences") }.toString(),

                )

            println(sentences)
            return sentences
        }catch (e:Exception){
         //   println(e.stackTrace)
        }
       return Sentences("","","","")
    }

    override fun onDestroy() {
        if (tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()

    }


}