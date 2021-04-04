package com.example.whatsapp

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    fun bind(user:User, onClick: (name:String,photo:String,id:String)->Unit) = with(itemView){
        CountTv.isVisible = false
        TimeTv.isVisible = false


            TitleTv.text = user.name + " " +  user.lastName
            SubTitleTv.text  = user.status
            Picasso.get()
                .load(user.thumbImage)
                .placeholder(R.drawable.ic_profile_outline_40)
                .error(R.drawable.ic_profile_outline_40)
                .into(userImageView1)

        setOnClickListener {
              onClick.invoke(user.name+" "+user.lastName,user.thumbImage,user.uid)
        }

    }

}