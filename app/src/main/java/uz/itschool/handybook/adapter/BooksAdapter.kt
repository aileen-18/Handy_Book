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
        val imageIV: ImageView = itemView.findViewById(R.id.book_img)
        val bookmarkIV: ImageView = itemView.findViewById(R.id.book_saved_icon)
        val bookMarkCV: CardView = itemView.findViewById(R.id.book_item_bookmark_cardview)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        return BookHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false))
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val book = books[position]
        holder.title.text = book.name
        if (book.audio == null) holder.audio.visibility = View.GONE
        holder.author.text = book.author
        holder.imageIV.load(book.image)
        holder.rating.text = book.reyting.toString()
        holder.bookmarkIV.setOnClickListener {

        }
        holder.itemView.setOnClickListener {
            bookclicked.OnClick(book)
        }

    }

    interface BookCLicked{
        fun OnClick(book: Book)
    }
}