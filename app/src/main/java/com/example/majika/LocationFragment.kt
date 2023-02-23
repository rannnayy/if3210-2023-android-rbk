package com.example.majika

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.LocItemAdapter
import com.example.majika.data.LocDatasource
import com.example.majika.model.Loc
import com.example.majika.retrofit.MajikaAPI
import com.example.majika.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class LocationFragment : Fragment() {
    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    private lateinit var adapter: LocItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var locsList: List<Loc>
    private var locds: LocDatasource = LocDatasource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        toolbarMajika = view.findViewById(R.id.majikaToolbar)
        toolbarMajikaText = toolbarMajika.findViewById(R.id.majikaToolbarTitle)
        toolbarMajika.title = "Cabang"
        toolbarMajikaText.setText(toolbarMajika.title)
        (activity as AppCompatActivity).setSupportActionBar(toolbarMajika)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        val quotesApi = RetrofitClient.getInstance().create(MajikaAPI::class.java)

        GlobalScope.launch {
            try{
                val result = quotesApi.getBranches();
                if (result != null){
                    // Log.d("BRANCHES: ", result.body()?.data.toString())

                    locds.fillList(result.body()?.data!!)
                }
                withContext(Dispatchers.Main) {
                    locsList = locds.loadList()
                    val layoutManager = LinearLayoutManager(context)
                    recyclerView = view.findViewById(R.id.LocRecyclerView)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.setHasFixedSize(true)
                    adapter = LocItemAdapter(locsList)
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {

            }

        }

        return view
    }

}