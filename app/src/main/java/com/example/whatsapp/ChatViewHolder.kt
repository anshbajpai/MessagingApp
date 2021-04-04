package com.example.whatsapp

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Inbox, onClick: (name: String, photo: String, id: String) -> Unit) =
        with(itemView) {
            CountTv.isVisible = item.count > 0
            CountTv.text = item.count.toString()
            TimeTv.text = item.time.formatAsListItem(context)

            TitleTv.text = item.name
            SubTitleTv.text = item.msg
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.ic_iconfinder_jee_75_2180662)
                .error(R.drawable.ic_iconfinder_jee_75_2180662)
                .into(userImageView1)
            setOnClickListener {
                onClick.invoke(item.name, item.image, item.from)
            }
        }
}