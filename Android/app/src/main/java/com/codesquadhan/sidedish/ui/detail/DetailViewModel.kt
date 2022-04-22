package com.codesquadhan.sidedish.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codesquadhan.sidedish.data.DetailImage

class DetailViewModel : ViewModel() {

    private val _detailImageListLd = MutableLiveData<MutableList<DetailImage>>()
    private val detailImageList = mutableListOf<DetailImage>()
    val detailImageListLd: LiveData<MutableList<DetailImage>> = _detailImageListLd

    private val _vpImageListLd = MutableLiveData<ArrayList<String>>()
    private val vpImageList = arrayListOf<String>()
    val vpImageListLd: LiveData<ArrayList<String>> = _vpImageListLd

    init {
        addMainListTest()
        addViewPagerImageTest()
    }

    private fun addMainListTest() {
        detailImageList.add(
            DetailImage(
                1,
                1,
                "data:image/jpeg;base64,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",
                false
            )
        )
        detailImageList.add(
            DetailImage(
                1,
                2,
                "https://mblogthumb-phinf.pstatic.net/MjAyMDA4MDJfNTAg/MDAxNTk2MzQzNjI3NzY5.9UeH-co5TMi4z_SppO8fzJeL0qPBud80IbylKvgs-a8g.BOgPOYHa9Vyhz0A-0JrMdkULkn-9Sw6WZWWRnBTHC3Qg.JPEG.baby0817/KIMEWMCID_CHUAL100.jpg?type=w800",
                false
            )
        )
        detailImageList.add(
            DetailImage(
                1,
                3,
                "https://mblogthumb-phinf.pstatic.net/MjAxNzAyMjdfMTky/MDAxNDg4MTk4ODQ3Mzgz.ndKHMVwsoIl8uScaxaQzIqsyZ18jSR0xuqHhOtsWbBAg.uawiyPKDdlNDz-WWoSSYfGiRPI9gAI7cOmu_aIyYZZMg.JPEG.barosk85/1.jpg?type=w800",
                false
            )
        )
        detailImageList.add(
            DetailImage(
                1,
                4,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNhyyRKcPQh98vaO5ePxXeQ2-STJSZy9Wo5A&usqp=CAU\n",
                false
            )
        )
        detailImageList.add(
            DetailImage(
                1,
                5,
                "https://i0.wp.com/chopchopchoi.com/wp-content/uploads/2020/03/KimchiFriedRice-scaled.jpg?fit=1024%2C682&ssl=1\n",
                false
            )
        )

        _detailImageListLd.value = detailImageList
    }

    fun addViewPagerImageTest(){
        vpImageList.add("https://ww.namu.la/s/32b66f77ef969034adcbce3840d362f66470c1ade8b3b67e9859954467a61a77ffe24051fd4db50a6a5251acd242a9140df6fc120653c85407c5508b00e763d8b2b09754bbe86f5ec315d6c2bfa597a8dc287028f3608155e80c67801d60595d")
        vpImageList.add("https://imagescdn.gettyimagesbank.com/500/21/442/935/0/1298312835.jpg")
        _vpImageListLd.value = vpImageList
    }

}