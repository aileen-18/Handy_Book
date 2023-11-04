package uz.itschool.handybook.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.handybook.R
import uz.itschool.handybook.api.APIClient
import uz.itschool.handybook.api.APIService
import uz.itschool.handybook.databinding.FragmentRatingBinding
import uz.itschool.handybook.model.Book
import uz.itschool.handybook.model.Comment
import uz.itschool.handybook.model.CommentData
import uz.itschool.handybook.shared_pref.SharedPrefHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RatingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RatingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Book? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Book
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRatingBinding.inflate(inflater, container, false)

//        binding.ratingBar.rating
        val shared = SharedPrefHelper.getInstance(requireContext())
        val user = shared.getLoginData()
        val api = APIClient.getInstance().create(APIService::class.java)

        binding.jonatish.setOnClickListener {
//            Log.d("TAG", "onCreateView: ${user.get(0).id}")
            var commentData = CommentData(
                book_id = param1!!.id,
                reyting = binding.ratingBar.rating.toInt(),
                text = binding.commentsss.text.toString(),
                user_id = user.get(0).id
            )


//            api.addComment(commentData).enqueue(object : Callback<CommentData> {
//                override fun onResponse(call: Call<CommentData>, response: Response<CommentData>) {
//                    Log.d("TAG6", "onResponse: ${response.body()}")
//                    Toast.makeText(requireContext(), "sent", Toast.LENGTH_SHORT).show()
//                    parentFragmentManager.beginTransaction().replace(R.id.main,CommentFragment()).commit()
//                }
//
//                override fun onFailure(call: Call<CommentData>, t: Throwable) {
//                    Log.d("TAG", "onFailure: $t")
//                }
//
//            })
        }

            binding.textView7.setText(param1!!.name + " romani sizga qanchalik manzur keldi?")


            return binding.root
        }

    }