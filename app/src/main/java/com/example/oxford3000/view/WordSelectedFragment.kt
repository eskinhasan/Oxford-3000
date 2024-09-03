package com.example.oxford3000.view



import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import androidx.room.Room
import com.example.oxford3000.R
import com.example.oxford3000.adapter.WordAdapter

import com.example.oxford3000.databinding.FragmentWordSelectedBinding

import com.example.oxford3000.service.WordDatabase
import kotlinx.coroutines.launch


class WordSelectedFragment : Fragment() {
   private var _binding:FragmentWordSelectedBinding?=null
    private val binding
        get() = _binding!!

    private lateinit var db: WordDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentWordSelectedBinding.inflate(inflater,container,false)


        val view=binding.root

        return view
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        val recyclerView: RecyclerView =view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

           try {
               lifecycleScope.launch {
                   db= Room.databaseBuilder(requireContext(),WordDatabase::class.java,"WordDatabase").build()
                   val userDao=db.wordDao()
                   val itemsWord=WordAdapter(userDao.getAll())
                   recyclerView.adapter=itemsWord
               }

           }catch (e:Exception){
               println(e.printStackTrace())
           }







    }




    override fun onDestroy() {
        _binding=null
        db.close()

        super.onDestroy()




    }


}