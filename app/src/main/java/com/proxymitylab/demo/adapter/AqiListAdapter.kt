package  com.proxymitylab.demo.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proxymitylab.demo.R
import com.proxymitylab.demo.model.DataChangeModel
import com.proxymitylab.demo.util.TimeUtils
import kotlinx.android.synthetic.main.item_aqi_list.view.*
import java.text.DecimalFormat

class AqiListAdapter(var aqiList: List<DataChangeModel>, context: OnClickRecipeInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var interfaceRecipeClickHandler: OnClickRecipeInterface? = null

    init {
        interfaceRecipeClickHandler = context
    }

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==0){
            return ViewHolderHeader(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_aqi_list, parent, false)
            )
        }
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_aqi_list, parent, false)
        )

    }

    override fun onBindViewHolder(holderItem: RecyclerView.ViewHolder, position: Int) {
       if( holderItem.itemViewType!=0) {

           val holder: ViewHolder = holderItem as ViewHolder

           val repoModel = aqiList.get(position)
           holder.itemView.setOnClickListener {
               interfaceRecipeClickHandler?.onClick(repoModel)
           }

           holder.itemView.city.text = repoModel.city
           holder.itemView.aqi_time.text = TimeUtils.getRelativeTime(repoModel.timeMilli)
           val number: Double? = repoModel.aqiValue?.toDouble()
           val dec = DecimalFormat("##.00")
           number?.let {
               if (it <= 50) {
                   holder.itemView.aqi_value.setBackgroundColor(
                       ContextCompat.getColor(
                           holder.itemView.context,
                           R.color.color_green
                       )
                   )
               } else if (it <= 100) {
                   holder.itemView.aqi_value.setBackgroundColor(
                       ContextCompat.getColor(
                           holder.itemView.context,
                           R.color.color_dark_green
                       )
                   )
               } else if (it <= 200) {
                   holder.itemView.aqi_value.setBackgroundColor(
                       ContextCompat.getColor(
                           holder.itemView.context,
                           R.color.color_yellow
                       )
                   )
               } else if (it <= 300) {
                   holder.itemView.aqi_value.setBackgroundColor(
                       ContextCompat.getColor(
                           holder.itemView.context,
                           R.color.color_orange
                       )
                   )
               } else if (it <= 400) {
                   holder.itemView.aqi_value.setBackgroundColor(
                       ContextCompat.getColor(
                           holder.itemView.context,
                           R.color.color_red
                       )
                   )
               } else {
                   holder.itemView.aqi_value.setBackgroundColor(
                       ContextCompat.getColor(
                           holder.itemView.context,
                           R.color.color_dark_red
                       )
                   )
               }
               holder.itemView.aqi_value.text = dec.format(number)

           }

       }else{
           val holder: ViewHolderHeader = holderItem as ViewHolderHeader
           holder.itemView.parent_layout.setBackgroundColor( ContextCompat.getColor(
               holder.itemView.context,
               R.color.color_light_grey
           ))
           holder.itemView.city.typeface= Typeface.DEFAULT_BOLD;
           holder.itemView.aqi_value.typeface= Typeface.DEFAULT_BOLD;
           holder.itemView.aqi_time.typeface= Typeface.DEFAULT_BOLD;
       }
    }


    override fun getItemCount() = aqiList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class ViewHolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun dataSetChanged(newValues: List<DataChangeModel>) {
        aqiList = newValues
        notifyDataSetChanged()
    }


    interface OnClickRecipeInterface {
        fun onClick(repoDto: DataChangeModel?)
    }

}