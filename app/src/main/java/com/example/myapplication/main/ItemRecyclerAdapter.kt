package com.example.myapplication.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CatalogItemBinding
import com.example.myapplication.models_and_DB.RealmItemData
import com.squareup.picasso.Picasso
import io.realm.kotlin.query.RealmResults

class ItemRecyclerAdapter(private val onNodeListener: IItemNodeListener):RecyclerView.Adapter<ItemRecyclerAdapter.ItemHolder>() {
    private var itemList: RealmResults<RealmItemData>?=null

    class ItemHolder(item: View, context: Context?, private val onNodeListener: IItemNodeListener) : RecyclerView.ViewHolder(item), View.OnClickListener{
        private val binding=CatalogItemBinding.bind(item)

        fun bindElement(item: RealmItemData)= with(binding){
            tvTitle.text=item.itemName
            //добавление изображения
            //Picasso.with(context).load(item._id).into(itemImg)
            Picasso.get().load(item._id).into(itemImg)
        }

        override fun onClick(v: View?) {
            onNodeListener.onItemClickListener(adapterPosition)
        }

        init {
            item.setOnClickListener(this)
        }
    }

    interface IItemNodeListener{
        fun onItemClickListener(position: Int)
    }

    fun getItems(): RealmResults<RealmItemData>?{
        return itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return  ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.catalog_item, parent, false), parent.context, onNodeListener)

    }

    override fun onBindViewHolder(holder: ItemHolder, postition: Int) {
        itemList?.get(postition)?.let {holder.bindElement(it)}

    }

    override fun getItemCount(): Int {
        return itemList?.size ?: return 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedList: RealmResults<RealmItemData>){
        itemList=updatedList
        notifyDataSetChanged()
    }
}