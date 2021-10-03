package com.proxymitylab.demo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.PointsGraphSeries
import com.proxymitylab.demo.R
import com.proxymitylab.demo.databinding.FragmentGraphDetailBinding
import com.proxymitylab.demo.model.AQIData
import com.proxymitylab.demo.model.DataChangeModel
import com.proxymitylab.demo.network.CallResponseStatus
import com.proxymitylab.demo.viewmodel.MainViewModel
import java.util.concurrent.TimeUnit


class LiveTrackingGraphCityFragment : Fragment() {
    private lateinit var binding: FragmentGraphDetailBinding

    var xySeries: PointsGraphSeries<DataPoint>? = null
    var xyValue: ArrayList<DataChangeModel> = ArrayList()
    private var aqiList = ArrayList<AQIData>()
    private val aqiViewModel: MainViewModel by viewModels()
    var map = LinkedHashMap<String, DataChangeModel>()
    var dataChangeModel: DataChangeModel? = null
    var xValue: Double=0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphDetailBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    companion object {
        val KEY_AQI_DATA = "aqi_data"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
    }


    private fun bindView() {
        xySeries = PointsGraphSeries()
        createScatterPlot()


        setAqiObserver()
        aqiViewModel.subscribeToSocketEvents()
        dataChangeModel = arguments?.get(KEY_AQI_DATA) as DataChangeModel

    }

    private fun setAqiObserver() {
        aqiViewModel?.aqiMutableLiveData?.observe(
            viewLifecycleOwner,
            Observer {
                it?.let { output ->
                    when (output.status) {
                        CallResponseStatus.Status.SUCCESS -> {
                            it.data?.let {
                                aqiList = it as ArrayList<AQIData>

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

                                map.forEach { k, v ->
                                    if (k.equals(dataChangeModel?.city)) {
                                        map.get("$k")?.let { it1 ->
                                            xyValue.add(it1)
                                        }
                                    }
                                }


                                if (xyValue.size > 0) {
                                    val i = xyValue.size - 1
                                   xValue++;

                                    val y: Double = xyValue.get(i).aqiValue?.toDouble()!!
                                    xySeries?.appendData(DataPoint(xValue, y), true, 30)
                                }

                                binding.mScatterPlot.animate()
                            }

                        }
                        CallResponseStatus.Status.ERROR -> {
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

    private fun createScatterPlot() {

        //sort the array of xy values

        //add the data to the series


        //set some properties
        xySeries?.setShape(PointsGraphSeries.Shape.RECTANGLE)
        xySeries?.setColor(Color.BLUE)
        xySeries?.setSize(20f)

        //set Scrollable and Scaleable
        binding.mScatterPlot.getViewport().setScalable(true)
        binding.mScatterPlot.getViewport().setScalableY(true)
        binding.mScatterPlot.getViewport().setScrollable(true)
        binding.mScatterPlot.getViewport().setScrollableY(true)

        //set manual x bounds
        binding.mScatterPlot.getViewport().setYAxisBoundsManual(false)
        binding.mScatterPlot.getViewport().setMaxY(1000.0)
        binding.mScatterPlot.getViewport().setMinY(0.0)

        //set manual y bounds
        binding.mScatterPlot.getViewport().setXAxisBoundsManual(false)
        binding.mScatterPlot.getViewport().setMaxX(30.0)
        binding.mScatterPlot.getViewport().setMinX(0.0)
        binding.mScatterPlot.addSeries(xySeries)
    }


    override fun onDestroy() {
        super.onDestroy()
        aqiViewModel.onCleared()
    }

}