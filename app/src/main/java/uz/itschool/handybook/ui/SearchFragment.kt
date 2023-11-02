package uz.itschool.handybook.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.handybook.R
import uz.itschool.handybook.adapter.BooksAdapter
import uz.itschool.handybook.api.APIClient
import uz.itschool.handybook.api.APIService
import uz.itschool.handybook.databinding.FragmentSearchBinding
import uz.itschool.handybook.model.Book

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var searchLast = ""

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
       val binding = FragmentSearchBinding.inflate(inflater, container,false)

        val api = APIClient.getInstance().create(APIService::class.java)
        // Search products
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == searchLast) return false

                api.searchByName(newText!!).enqueue(object : Callback<List<Book>>{
                    override fun onResponse(
                        call: Call<List<Book>>,
                        response: Response<List<Book>>
                    ) {
                        val books = response.body()!!
                        binding.rvAllBooks.adapter = BooksAdapter(books,object : BooksAdapter.BookCLicked {
                            override fun OnClick(book: Book) {
                                val bundle = Bundle()
                                bundle.putSerializable("book", book)
                                findNavController().navigate(
                                    R.id.action_mainFragment_to_bookFragment,
                                    bundle)
                            }
                        })

                    }

                    override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                        Log.d("TAG", "$t")
                    }

                })
                searchLast = newText

                return true
            }

            override fun onQueryTextSubmit(newText: String?): Boolean {
                return true
            }

        })

        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}