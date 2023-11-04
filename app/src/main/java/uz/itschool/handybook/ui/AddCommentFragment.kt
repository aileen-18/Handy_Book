package uz.itschool.handybook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.handybook.R
import uz.itschool.handybook.api.APIClient
import uz.itschool.handybook.api.APIService
import uz.itschool.handybook.databinding.FragmentAddCommentBinding
import uz.itschool.handybook.model.AddComment

/**
 * A simple [Fragment] subclass.
 * Use the [AddCommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class AddCommentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val binding = FragmentAddCommentBinding.inflate(inflater,container,false)
        binding.addCommentText.setOnClickListener{
            val text = binding.addCommetText.text
            val reyting = binding.addCommetReyting.text

            val c = AddComment(book_id = 2, user_id = 1, reyting = reyting.toString().toDouble(), text = text.toString())
            val api = APIClient.getInstance().create(APIService::class.java)
            api.addComment(c).enqueue(object : Callback<AddComment>{
                override fun onResponse(call: Call<AddComment>, response: Response<AddComment>) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView,CommentFragment())
                        .commit()
                }

                override fun onFailure(call: Call<AddComment>, t: Throwable) {
                    Log.d("TAG", "onFailure: $t")
                }

            })
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddCommentFragment.
         */
        // TODO: Rename and change types and number of paramet-ers
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}