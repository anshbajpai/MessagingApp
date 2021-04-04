package com.example.whatsapp

import com.google.firebase.firestore.FieldValue

data class User(
    val name:String,
    val lastName:String,
    val imageUrl:String,
    val thumbImage:String,
    val uid:String,
    val deviceToken:String,
    val status:String,
    val onlineStatus: String,
) {
    constructor():this("","","","","","","","")

    constructor(name: String,lastName:String = "",imageUrl: String,thumbImage: String,uid: String):this(
        name,
        lastName,
        imageUrl,
        thumbImage,
        uid,
        "",
        "Hey there !!",
        "")
}