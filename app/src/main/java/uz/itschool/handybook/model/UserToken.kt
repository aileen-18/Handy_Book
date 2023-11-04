package uz.itschool.handybook.model

data class UserToken(
    var id: Int,
    var username: String,
    var fullname:String,
    var access_token: String
)
