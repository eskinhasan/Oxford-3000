package com.example.oxford3000.adapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oxford3000.R
import com.example.oxford3000.model.WordsSelected
import com.example.oxford3000.service.ImageApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WordAdapter(private val wordSelected: List<WordsSelected>): RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private val imageApiService = ImageApiService()


    class WordViewHolder (view:View): RecyclerView.ViewHolder(view) {


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.word_card,parent,false)




        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wordSelected.size
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {





        holder.itemView.findViewById<TextView>(R.id.trText).text=wordSelected[position].tr
        holder.itemView.findViewById<TextView>(R.id.engText).text=wordSelected[position].eng
        val eng=wordSelected[position].eng
        val selected=holder.itemView.findViewById<ImageView>(R.id.flagImage)




        GlobalScope.launch {
            try {
                val bitmapImage= imageApiService.imageResource(eng,holder.itemView.context)
                selected.setImageBitmap(bitmapImage)
            }catch (e:Exception){
                println(e.stackTrace)
            }

        }
    }








}

