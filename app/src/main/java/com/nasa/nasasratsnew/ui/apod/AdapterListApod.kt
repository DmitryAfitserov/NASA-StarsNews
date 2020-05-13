package com.nasa.nasasratsnew.ui.apod

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.nasa.nasasratsnew.R
import com.nasa.nasasratsnew.data.ApodData

class AdapterListApod(activity: FragmentActivity, list: MutableList<Any?>) :

    ArrayAdapter<Any>(activity, R.layout.custom_item_apod, list) {

    var vi: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val id:Int = 155

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val retView: View



        if(getItem(position) is ApodData){


            if(convertView == null || convertView.id == id){
                retView = vi.inflate(R.layout.custom_item_apod, null)
                holder = ViewHolder()

                holder.text = retView.findViewById(R.id.text) as TextView?

                holder.text?.text = (getItem(position) as ApodData).id.toString()


                (getItem(position) as ApodData).bitmap?.let {
                    holder.image = retView.findViewById(R.id.imageView) as ImageView?
                    holder.image!!.setImageBitmap(it) }

                retView.tag = holder

            } else {
                holder = convertView.tag as ViewHolder
                holder.text?.text = (getItem(position) as ApodData).id.toString()

                (getItem(position) as ApodData).bitmap?.let {
                    if(holder.image == null){
                        holder.image = convertView.findViewById(R.id.imageView) as ImageView?
                    }
                    holder.image?.setImageBitmap(it) }


                return convertView
            }
        } else {

            if(getItem(position) == true){
                retView = vi.inflate(R.layout.custom_item_apod_error, null)
                retView.id = id
                holder = ViewHolder()

                holder.text = retView.findViewById(R.id.text) as TextView?


                retView.tag = holder
            } else {

                    retView = vi.inflate(R.layout.custom_item_apod_loading, null)
                    retView.id = id
                    holder = ViewHolder()

                //    holder.text = retView.findViewById(R.id.text) as TextView?


                    retView.tag = holder

            }
        }

        return retView
    }



    internal class ViewHolder {
        var text: TextView? = null
        var image: ImageView? = null
    }


}