package uz.itschool.handybook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.itschool.handybook.R
import uz.itschool.handybook.model.Book

class BooksAdapter(var books : List<Book>, private val bookclicked: BookCLicked): RecyclerView.Adapter<BooksAdapter.BookHolder>() {
     class BookHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.book_title)
        val audio: ImageView = itemView.findViewById(R.id.book_item_audio)
        val author: TextView = itemView.findViewById(R.id.book_author)
        val rating: TextView = itemView.findViewById(R.id.book_rate)
        val image: ImageView = itemView.findViewById(R.id.book_img)
        val bookmarkIV: ImageView = itemView.findViewById(R.id.book_saved_icon)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        return BookHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false))
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val book = books[position]
        var state= true
        holder.title.text = book.name
        holder.author.text = book.author
        holder.image.load(book.image)
        holder.rating.text = book.reyting.toString()
        holder.bookmarkIV.setOnClickListener {
            if (books.contains(book)){
                holder.bookmarkIV.setImageResource(R.drawable.saved_filled)
            }
        }
        holder.itemView.setOnClickListener {
            bookclicked.OnClick(book)
        }

    }

    interface BookCLicked{
        fun OnClick(book: Book)
    }
}