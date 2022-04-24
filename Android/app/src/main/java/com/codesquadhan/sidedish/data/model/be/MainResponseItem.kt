package com.codesquadhan.sidedish.data.model.be

import com.codesquadhan.sidedish.ui.constant.ViewType.FOOD_VIEW_TYPE
import com.google.gson.annotations.SerializedName

data class MainResponseItem(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imagePath")
    val imagePath: String,
    @SerializedName("menuType")
    val menuType: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("saleType")
    val saleType: String,
    val viewType: Int = 0,
    val headerText: String? = ""
)