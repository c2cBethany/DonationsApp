package com.example.donationsapp.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.donationsapp.R
import com.example.donationsapp.datasource.DataClassDonation

class ImagesAdapter(private val context: Context, private var dataList: List<DataClassDonation>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].dataImage)
            .into(holder.recImage)
        holder.postOrganization.text = dataList[position].dataOrganization
        holder.postCaption.text = dataList[position].dataCaption
        holder.postTitle.text = dataList[position].dataTitle
        holder.postType.text = dataList[position].dataType
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: List<DataClassDonation>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recImage: ImageView
    var postTitle: TextView
    var postCaption: TextView
    var postOrganization: TextView
    var postType: TextView
    var recCard: CardView
    init {
        recImage = itemView.findViewById(R.id.eachItem)
        postTitle = itemView.findViewById(R.id.postTitleDisplay)
        postCaption = itemView.findViewById(R.id.postCaptionDisplay)
        postOrganization = itemView.findViewById(R.id.postOrganizationDisplay)
        postType = itemView.findViewById(R.id.postTypeDisplay)
        recCard = itemView.findViewById(R.id.recCard)
    }
}