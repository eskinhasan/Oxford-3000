package com.example.oxford3000.adapter

import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.QUEUE_FLUSH
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleService
import androidx.recyclerview.widget.RecyclerView
import com.example.oxford3000.R
import com.example.oxford3000.model.Sentences
import com.example.oxford3000.model.SentencesWord
import com.example.oxford3000.service.ImageApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class WordSentencesAdapter(private val tts: TextToSpeech,val sentencesWord: List<SentencesWord>): RecyclerView.Adapter<WordSentencesAdapter.WordViewHolder>(),TextToSpeech.OnInitListener {
    private val imageApiService = ImageApiService()
   inner class WordViewHolder(view:View):RecyclerView.ViewHolder(view) {
        init {

            itemView.findViewById<Button>(R.id.sentences_listen).setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(sentencesWord[adapterPosition].eng, TextToSpeech.QUEUE_FLUSH, null, sentencesWord[adapterPosition].eng)
                }
                else {
                    tts.speak(sentencesWord[adapterPosition].eng, TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.word_sentences_card,parent,false)

        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
       return sentencesWord.size
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        if (sentencesWord[position].eng!=" "){
            holder.itemView.findViewById<TextView>(R.id.trText).text=sentencesWord[position].tr
            holder.itemView.findViewById<TextView>(R.id.engText).text=sentencesWord[position].eng
        }
       val speak=sentencesWord[position].eng




       GlobalScope.launch(Dispatchers.Main){

           try {
               val bitmapImage= imageApiService.imageResource(sentencesWord[position].eng,holder.itemView.context)
               holder.itemView.findViewById<ImageView>(R.id.flagImage).setImageBitmap(bitmapImage)
           }catch (e:Exception){
               println(e.stackTrace)
           }

       }






    }
    private fun Speak(speak: String) {
        tts!!.speak(speak, QUEUE_FLUSH,null,"")
    }

    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){
            val result=tts!!.setLanguage(java.util.Locale.US)
            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                println("Language not supported")
            }else{

            }
        }else{
            println("TTS Initialization Failed")
        }
    }
}