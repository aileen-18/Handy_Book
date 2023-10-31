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
import uz.itschool.handybook.databinding.FragmentLogInBinding
import uz.itschool.handybook.model.SignIn
import uz.itschool.handybook.model.User
import uz.itschool.handybook.shared_pref.SharedPrefHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentLogInBinding
    private val api = APIClient.getInstance().create(APIService::class.java)
    private lateinit var shared : SharedPrefHelper


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
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        shared = SharedPrefHelper.getInstance(requireContext())
        binding.signupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signInFragment)
        }

        binding.kirishBtn.setOnClickListener {
            val signIn = SignIn(
                binding.usernameEdit.text.toString().trim(),
                binding.passwordEdit.text.toString().trim()
            )
            if (signIn.password == "" || signIn.username == "") return@setOnClickListener
            if (!validate(signIn)) return@setOnClickListener
            api.login(signIn).enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (!response.isSuccessful) {
                        binding.passwordEdit.setText("")
                        Toast.makeText(requireContext(), "Noto'g'ri username yoki parol", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val user: User = response.body()!!
                    if (binding.rememberMe.isChecked) shared.setRememberMe(user.username) else shared.setRememberMe("")
                    shared.setUser(user)
                    findNavController().navigate(R.id.action_logInFragment_to_mainFragment)
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("TAG", "$t")
                }

            })
        }





        RememberMe()
        return binding.root
    }
    private fun validate(signIn: SignIn): Boolean {
        var out = true
        if (signIn.username.length < 5) {
            binding.loginIncorrectUsername.visibility = View.VISIBLE
            binding.passwordEdit.setText("")
            out = false
        }else{
            binding.loginIncorrectUsername.visibility = View.GONE
        }
        if (signIn.password.length < 8) {
            binding.loginIncorrectPassword.visibility = View.VISIBLE
            binding.passwordEdit.setText("")
            out = false
        }else{
            binding.loginIncorrectPassword.visibility = View.GONE
        }
        return out
    }

  private fun RememberMe(){
      val username = shared.getRememberMe()
      if (username != null) {
          binding.rememberMe.isChecked =true
          binding.usernameEdit.setText(username)
      }
  }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}