package uz.itschool.handybook.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.handybook.R
import uz.itschool.handybook.api.APIClient
import uz.itschool.handybook.api.APIService
import uz.itschool.handybook.databinding.FragmentSignInBinding
import uz.itschool.handybook.model.SignUp
import uz.itschool.handybook.model.User
import uz.itschool.handybook.shared_pref.SharedPrefHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSignInBinding
    private val api = APIClient.getInstance().create(APIService::class.java)
    private lateinit var shared_pref: SharedPrefHelper

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
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        shared_pref = SharedPrefHelper.getInstance(requireContext())


        binding.signupBackButtonIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signupBtn.setOnClickListener {
            if (binding.inputUsername.text.toString() == "") {
                Toast.makeText(requireContext(), "Foydalnuvchi nomini kiriting", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.inputPassword.text.toString() == "") {
                Toast.makeText(requireContext(), "Parol kiriting", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val signUp = SignUp(
                username = binding.inputUsername.text.toString().trim(),
                fullname = binding.inputName.text.toString()
                    .trim() + " " + binding.inputSurname.text.toString().trim(),
                password = binding.inputPassword.text.toString().trim(),
                email = binding.inputEmail.text.toString()
            )
            if (!validate(signUp)) return@setOnClickListener

            api.signup(signUp).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Log.d("TAG", "$response")

                    if (!response.isSuccessful) {
                        Toast.makeText(requireContext(), "Ro'yhatdan o'tishda xatolik", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val user = response.body()!!
                    shared_pref.setUser(user)
                    findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("TAG", "$t")
                }
            })
        }



        return binding.root
    }

    private fun validate(signUp: SignUp): Boolean {
        var out = true
        if (binding.inputName.text.toString() == "") {
            binding.incorrectName.visibility = View.VISIBLE
            out = false
        } else binding.incorrectName.visibility = View.GONE

        if (binding.inputSurname.text.toString() == "") {
            binding.incorrectSurname.visibility = View.VISIBLE
            out = false
        } else binding.incorrectSurname.visibility = View.GONE

        if (signUp.username.length < 5) {
            binding.incorrectUsername.visibility = View.VISIBLE
            out = false
        } else binding.incorrectUsername.visibility = View.GONE

        if (!signUp.email.contains("@")) {
            binding.incorrectEmail.visibility = View.VISIBLE
            out = false
        } else binding.incorrectEmail.visibility = View.GONE

        if (signUp.password.length < 8) {
            binding.incorrectPassword.visibility = View.VISIBLE
            binding.inputPassword.setText("")
            binding.incorrectRepeatPassword.setText("")
            out = false
        } else binding.incorrectPassword.visibility = View.GONE

        if (signUp.password.length >= 8 && binding.incorrectRepeatPassword.text.toString()
                .trim() != signUp.password
        ) {
            binding.incorrectRepeatPassword.visibility = View.GONE
            binding.inputPassword2.setText("")
            out = false
        }
        return out
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}