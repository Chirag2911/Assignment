package com.proxymitylab.demo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.LinearLayoutManager
import com.proxymitylab.demo.R
import com.proxymitylab.demo.adapter.AqiListAdapter
import com.proxymitylab.demo.databinding.ActivityMainBinding
import com.proxymitylab.demo.databinding.FragmentLayoutBinding
import com.proxymitylab.demo.interfaces.LaunchFragmentInterface
import com.proxymitylab.demo.model.AQIData
import com.proxymitylab.demo.model.DataChangeModel
import com.proxymitylab.demo.network.CallResponseStatus
import com.proxymitylab.demo.viewmodel.MainViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class AqiListFragment : Fragment(), AqiListAdapter.OnClickRecipeInterface {
    private var binding: FragmentLayoutBinding? = null

    private var aqiListAdapter: AqiListAdapter? = null
    private var aqiList = ArrayList<AQIData>()
    private var mainList = ArrayList<DataChangeModel>()
    private var launchFragmentInterface: LaunchFragmentInterface? = null
    private val aqiViewModel: MainViewModel by viewModels()
    var map = LinkedHashMap<String, DataChangeModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLayoutBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindView()
        retrieveRepositories()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LaunchFragmentInterface) {
            launchFragmentInterface = context
        }
    }

    private fun bindView() {
        setAqiObserver()
    }

    private fun setAqiObserver() {
        aqiViewModel.aqiMutableLiveData.observe(
            viewLifecycleOwner,
            Observer {
                it?.let { output ->
                    when (output.status) {
                        CallResponseStatus.Status.SUCCESS -> {
                            binding?.progressbar?.visibility = View.GONE
                            it.data?.let {
                                aqiList = it as ArrayList<AQIData>
                                val dataAqiList = ArrayList<DataChangeModel>()

                                for (i in aqiList) {
                                    map.put(
                                        i.city!!,
                                        DataChangeModel(
                                            i.city!!,
                                            i.value,
                                            System.currentTimeMillis()
                                        )
                                    )
                                }

                                dataAqiList.add(DataChangeModel("City", "AQI", 0))

                                map.forEach { k, v ->
                                    map.get("$k")?.let { it1 ->
                                        dataAqiList.add(it1)
                                    }
                                }
                                setAdapter(dataAqiList)
                            }

                        }
                        CallResponseStatus.Status.ERROR -> {
                            binding?.progressbar?.visibility = View.GONE
                            setErrorMessage(it.message)

                        }
                    }
                }
            })


    }

    private fun setErrorMessage(it: String?) {
        activity?.let { it1 ->
            AlertDialog.Builder(it1).setTitle(R.string.error)
                .setMessage(it)
                .setPositiveButton(android.R.string.ok) { _, _ -> activity?.finish() }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }

    private fun setAdapter(it: List<DataChangeModel>) {
        aqiListAdapter?.dataSetChanged(it)

    }

    private fun initView() {
        aqiListAdapter = AqiListAdapter(mainList, this)
        val linearLayoutManager = LinearLayoutManager(activity)
        binding?.recylerView?.layoutManager = linearLayoutManager
        binding?.recylerView?.adapter = aqiListAdapter
    }

    private fun retrieveRepositories() {
        binding?.progressbar?.visibility = View.VISIBLE
        aqiViewModel.subscribeToSocketEvents()
    }

    override fun onClick(repoDto: DataChangeModel?) {
        val bundle = Bundle()
        bundle.putParcelable(LiveTrackingGraphCityFragment.KEY_AQI_DATA, repoDto)
//        launchFragmentInterface?.launchFragment(bundle, LiveTrackingGraphCityFragment())
    }

    override fun onDestroy() {
        super.onDestroy()
        aqiViewModel.onCleared()
        binding = null
    }


}