package uz.itschool.handybook.model

import java.io.Serializable

data class Comment(
    val text: String,
    val username: String
):Serializable