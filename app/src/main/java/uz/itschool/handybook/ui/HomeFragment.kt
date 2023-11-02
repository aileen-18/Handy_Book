package uz.itschool.handybook.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.handybook.R
import uz.itschool.handybook.adapter.BooksAdapter
import uz.itschool.handybook.adapter.CategoryAdapter
import uz.itschool.handybook.api.APIClient
import uz.itschool.handybook.api.APIService
import uz.itschool.handybook.databinding.FragmentHomeBinding
import uz.itschool.handybook.model.Book
import uz.itschool.handybook.model.Category
import uz.itschool.handybook.shared_pref.SharedPrefHelper
import java.security.AccessControlContext
import java.security.AccessController.getContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentHomeBinding
    private val api = APIClient.getInstance().create(APIService::class.java)
    private lateinit var shared: SharedPrefHelper
    private lateinit var adapter: BooksAdapter
    private var allBooks = listOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.rvAllBooks.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        binding.rvAllBooks.setHasFixedSize(true)
        shared = SharedPrefHelper.getInstance(requireContext())
        adapter = BooksAdapter(listOf(), object:BooksAdapter.BookCLicked{
            override fun OnClick(book: Book) {
                TODO("Not yet implemented")
            }
        })







        getAllBooks()
        setMainBook()
       // SetCategory()
        return binding.root
    }

    private fun SetCategory() {
        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        api.getCategories().enqueue(object : Callback<List<Category>>{
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (!response.isSuccessful) return
                val categories = response.body()!!
                binding.rvCategory.adapter = CategoryAdapter(categories,requireContext(),
                  object:CategoryAdapter.CategoryClicked{
                        override fun onClick(category: String?) {
                            if (category == null){
                                setAllBooks(allBooks)
                                return
                            }
                            setCategoryChanger(category)
                        }

                    })
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })
    }

    private fun setCategoryChanger(category: String) {
        api.getBookByCategory(category).enqueue(object : Callback<List<Book>>{
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (!response.isSuccessful) return
                val books = response.body()!!
                setAllBooks(books)
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })
    }

    private fun setMainBook() {
        api.getMainBook().enqueue(object : Callback<Book>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                val mainBook = response.body()!!
                Log.d(TAG, "onResponse: ${response.body()}")
                binding.homeMainBookImage.load(mainBook.image)
                binding.homeMainBookText.text = """${mainBook.author}ning "${mainBook.name}" asari"""
                binding.homeMainBookReadNowMb.setOnClickListener {
                    // TODO Set listener
                }
            }
            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d("TAG", "$t")
            }

        })
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getAllBooks(){
        api.getBooks().enqueue(object : Callback<List<Book>>{
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                val books = response.body()!!
                Log.d(TAG, "onResponse: $books")
                allBooks = books
                adapter.notifyDataSetChanged()

                binding.rvAllBooks.adapter = BooksAdapter(books, object:BooksAdapter.BookCLicked{
                    override fun OnClick(book: Book) {
                        val bundle = Bundle()
                        bundle.putSerializable("book", book)
                        findNavController().navigate(
                            R.id.action_mainFragment_to_bookFragment,
                            bundle
                        )
                    }

                })

            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })
    }
    private fun cacheAllBooks(books: List<Book>) {
        allBooks = books
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun
            setAllBooks(books: List<Book>) {
        adapter.books = books
        adapter.notifyDataSetChanged()
        binding.homeNothingFound.visibility = if (books.isEmpty()) View.VISIBLE else View.GONE
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

