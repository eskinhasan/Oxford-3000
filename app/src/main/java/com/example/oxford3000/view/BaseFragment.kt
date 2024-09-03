package com.example.oxford3000.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

import androidx.navigation.fragment.findNavController
import com.example.oxford3000.R

import com.example.oxford3000.databinding.FragmentBaseBinding
import com.example.oxford3000.model.Word

import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random


class BaseFragment : Fragment() {


    private  var _binding: FragmentBaseBinding?=null
    private val binding
        get() = _binding!!

    private var wordList= mutableListOf<Word>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentBaseBinding.inflate(inflater,container,false)
        val view=binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    onClickListener()







    }


    @SuppressLint("UseRequireInsteadOfGet", "DiscouragedApi")
    fun onClickListener(){

        binding.wordsFragment.setOnClickListener {

            val jsonData= requireActivity().applicationContext.resources.openRawResource(
                requireActivity().applicationContext.resources.getIdentifier(
                    "db",
                    "raw",
                    activity!!.applicationContext.packageName
                )
            ).bufferedReader().use { it.readText() }

            val outputJson= JSONObject(jsonData)

            val users=outputJson.getJSONArray("words") as JSONArray

            for (i in 0 until users.length()){
                val id=users.getJSONObject(i).getString("id")
                val categoryId=users.getJSONObject(i).getString("categoryId")
                val eng=users.getJSONObject(i).getString("ENG")
                val tr=users.getJSONObject(i).getString("TR")
                wordList.add(Word(id,categoryId,eng,tr))
            }
            val random= Random.nextInt(0,3000)
            val word=wordList[random].id

            val bundle = bundleOf("id" to word)
            findNavController().navigate(R.id.action_baseFragment_to_wordsFragment,bundle)


        }

        binding.wordsSelectedFragment.setOnClickListener {
            findNavController().navigate(R.id.action_baseFragment_to_wordSelectedFragment)

        }

        binding.sentencesFragment.setOnClickListener {
            findNavController().navigate(R.id.action_baseFragment_to_sentencesFragment)
        }




    }



    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}

