package com.kikopalomares.googlesheetsexample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.JsonArray

class RecyclerPetAdapter(private val jsonArray: JsonArray) : RecyclerView.Adapter<RecyclerPetAdapter.ViewHolder>() {

    class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView){
        val username = rootView.findViewById(R.id.username) as TextView
        val email = rootView.findViewById(R.id.email) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerPetAdapter.ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.row_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val idPlusUsername = jsonArray[position].asJsonObject["id"].asString + " - " + jsonArray[position].asJsonObject["name"].asString
        holder.username.text = idPlusUsername
        holder.email.text = jsonArray[position].asJsonObject["species"].asString
    }

    override fun getItemCount() = jsonArray.size()
}