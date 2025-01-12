package com.codesquadhan.sidedish.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesquadhan.sidedish.data.model.detail.DetailResponse
import com.codesquadhan.sidedish.data.model.detail.TopImageData
import com.codesquadhan.sidedish.data.repository.DetailRepository
import com.codesquadhan.sidedish.ui.common.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailRepository: DetailRepository) :
    ViewModel() {

    private val _detailResponseLiveData = MutableLiveData<DetailResponse>()
    val detailResponseLiveData = _detailResponseLiveData

    private val _detailImageListLiveData = MutableLiveData<List<TopImageData>>()
    private val detailImageList = mutableListOf<TopImageData>()
    val detailImageListLiveData: LiveData<List<TopImageData>> = _detailImageListLiveData

    private val _vpImageListLiveData = MutableLiveData<List<TopImageData>>()
    private val vpImageList = mutableListOf<TopImageData>()
    val vpImageListLiveData: LiveData<List<TopImageData>> = _vpImageListLiveData

    private val _orderedFoodQuantityLiveData = MutableLiveData(1)
    val orderedFoodQuantityLiveData: LiveData<Int> = _orderedFoodQuantityLiveData

    private val _orderSuccessLiveData = MutableLiveData<Boolean>()
    val orderSuccessLiveData = _orderSuccessLiveData

    private val _isNetworkError = MutableLiveData<Boolean>()
    val isNetworkError: LiveData<Boolean> = _isNetworkError

    // CEH
    private val ceh = CoroutineExceptionHandler { _, exception ->
        // 네트워크 통신 실패 시 화면 보여주기 처리하기
        _isNetworkError.value = true

    }

    private val cehOrder = CoroutineExceptionHandler { _, exception ->
        // 수량 초과 시 or 네트워크 오류 시
        _orderSuccessLiveData.value = false
    }

    fun getMenuDetail(id: Int) {
        viewModelScope.launch(ceh) {
            val detailResponse =
                detailRepository.getMenuDetail(id) ?: throw RuntimeException("why..?")
            _detailResponseLiveData.value = detailResponse

            detailResponse.mainImage.forEachIndexed { index, url ->
                vpImageList.add(TopImageData(index + 1, url))
            }
            detailResponse.detailImage.forEachIndexed { index, url ->
                detailImageList.add(TopImageData(index + 1, url))
            }

            _isNetworkError.value = false
            _vpImageListLiveData.value = vpImageList
            _detailImageListLiveData.value = detailImageList
        }
    }

    fun countUpOrDownOrderFoodQuantity(countUpOrDown: Common.Count) {
        _orderedFoodQuantityLiveData.value?.let {
            when (countUpOrDown) {
                Common.Count.UP -> _orderedFoodQuantityLiveData.value = it + 1
                Common.Count.DOWN -> _orderedFoodQuantityLiveData.value = if (it - 1 < 1) 1 else it - 1
            }
        }
    }

    // 재고 초과한 값으로 주문 시 response의 isSuccessful이 false가 되면서 repository에서 던진 예외가 ceh에서 잡힌다
    fun orderFood(menuId: Int) {
        _orderedFoodQuantityLiveData.value?.let {
            viewModelScope.launch(cehOrder) {
                detailRepository.orderFood(menuId, it)
                _orderSuccessLiveData.value = true
            }
        }
    }

}