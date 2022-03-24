package com.example.harumub_front

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class resultpageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter : LogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        Log.d("태그", "inflater")

        adapter = LogAdapter()
        Log.d("태그", "어댑터")

//        val layoutManager = LinearLayoutManager(context)
//        view.findViewById<RecyclerView>(R.id.resultpage_recycler).layoutManager = layoutManager
//        view.findViewById<RecyclerView>(R.id.resultpage_recycler).adapter = adapter


        // 수정할 부분 - DB 연결 필요
//        adapter.items.add(LogData("00:01:23", "happy"))
//        adapter.items.add(LogData("00:04:56", "scary"))
//        adapter.items.add(LogData("00:07:00", "angry"))
//        adapter.items.add(LogData("11:11:11", "anger"))

        return view


    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            resultpageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
