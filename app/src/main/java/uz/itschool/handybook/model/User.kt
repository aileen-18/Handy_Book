package uz.itschool.handybook.model

import java.io.Serializable

data class User(
    val access_token: String,
    val id: Int,
    val fullname:String,
    val username: String
):Serializable