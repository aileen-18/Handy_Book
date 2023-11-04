package uz.itschool.handybook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.itschool.handybook.R
import uz.itschool.handybook.model.Comment

class CommentAdapter(var commentList: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var user = itemView.findViewById<TextView>(R.id.comment_username)
        var text = itemView.findViewById<TextView>(R.id.comment_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        var view = CommentHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
        return view
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        var comment = commentList[position]
        holder.user.text = comment.username
        holder.text.text = comment.text
    }
}