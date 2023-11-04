package uz.itschool.handybook.shared_pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.itschool.handybook.model.Book
import uz.itschool.handybook.model.User
import uz.itschool.handybook.model.UserToken

class SharedPrefHelper private constructor(context: Context) {

     private val shared : SharedPreferences = context.getSharedPreferences("data", 0)
     private val edit: SharedPreferences.Editor = shared.edit()
     private val gson  = Gson()


    companion   object {
        private var instance: SharedPrefHelper? = null
        fun getInstance(context: Context): SharedPrefHelper {
            if (instance == null) instance = SharedPrefHelper(context)
            return instance!!
        }
    }

    private val userKEY = "user"
    private val rememberMeKEY = "rememberMe"

    fun setUser(user : User) {
        val temp = gson.toJson(user)
        edit.putString(userKEY, temp).apply()
    }
    fun getUser() : User? {
        val data = shared.getString(userKEY, "")
        if (data == "") return null
        val typeToken = object : TypeToken<User>() {}.type
        return gson.fromJson(data, typeToken)

    }

    fun logout(){
        edit.putString(userKEY, "").apply()
    }
    fun setRememberMe(username:String){
        if (username == "") {
            edit.remove(rememberMeKEY).apply()
        }else{
            edit.putString(rememberMeKEY, username).apply()
        }
    }
    fun getRememberMe(): String? {
        return shared.getString(rememberMeKEY, null)
    }
    fun setUserImage(user: User, url: String) {
        val type = object : TypeToken<List<User>>() {}.type
        val gson = Gson()

        val userList: MutableList<User>
        val str = shared.getString("Users", "")

        if (str == "") {
            userList = mutableListOf()
        } else {
            userList = gson.fromJson(str, type)
        }
        for (i in userList) {
            if (i.fullname == user.fullname) {
                userList.remove(i)
                userList.add(user)
            }
        }

        val edited = gson.toJson(userList)
        edit.putString("Users", edited).apply()
    }

    fun setLoginData(mutableList: MutableList<UserToken>){
        edit.putString("Login", gson.toJson(mutableList)).apply()
    }
    fun getLoginData(): MutableList<UserToken>{
        val data: String = shared.getString("Login", "")!!
        if (data == ""){
            return mutableListOf()
        }
        val typeToken = object : TypeToken<MutableList<UserToken>>(){}.type
        return gson.fromJson(data, typeToken)
    }


    fun GetSelectedBooks(): MutableList<Book>{
        val data: String = shared.getString("Selected", "")!!
        if (data == ""){
            return mutableListOf()
        }
        val typeToken = object : TypeToken<MutableList<Book>>(){}.type
        return gson.fromJson(data, typeToken)
    }
    fun SetSelectedBooks(mutableList: MutableList<Book>){
        edit.putString("Selected", gson.toJson(mutableList)).apply()
    }


}