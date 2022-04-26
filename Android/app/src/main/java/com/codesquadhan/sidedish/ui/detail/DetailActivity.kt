package com.codesquadhan.sidedish.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.codesquadhan.sidedish.R
import com.codesquadhan.sidedish.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var detailAdapter: DetailAdapter
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val id = intent.getIntExtra("id", 0)
        Log.d("AppTest", "menuId : $id")

        setViewPagerListener()
        setViewPager()
        setDetail()
        setDetailInfo()
        countUpOrDownQuantity()
        setOrderFoodQuantity()
        orderFood(id)
        detailViewModel.getMenuDetail(id)
    }

    private fun setViewPagerListener() {
        // 페이지 리스너 달아보기
        binding.vpDetail.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvCurrentPage.text = (position + 1).toString()
            }
        })

    }

    private fun setDetailInfo() {
        detailViewModel.detailResponseLd.observe(this) {
            binding.detailInfo = it
        }
    }

    private fun setViewPager() {
        viewPagerAdapter = ViewPagerAdapter()
        binding.vpDetail.apply {
            adapter = viewPagerAdapter
            //orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        detailViewModel.vpImageListLd.observe(this) {
            viewPagerAdapter.submitList(it.toList())
            binding.tvTotalPage.text = it.size.toString()
        }
    }

    private fun setDetail() {
        detailAdapter = DetailAdapter()
        binding.rvFoodDetail.apply {
            adapter = detailAdapter
        }

        detailViewModel.detailImageListLd.observe(this) {
            detailAdapter.submitList(it.toList())
        }
    }

    private fun countUpOrDownQuantity() {
        binding.ivCountDown.setOnClickListener {
            detailViewModel.countUpOrDownOrderFoodQuantity(-1)
        }

        binding.ivCountUp.setOnClickListener {
            detailViewModel.countUpOrDownOrderFoodQuantity(0)
        }
    }

    private fun setOrderFoodQuantity() {
        detailViewModel.orderedFoodQuantityLD.observe(this) {
            binding.tvItemCount.text = it.toString()
        }
    }

    private fun orderFood(menuId: Int) {
        binding.btnOrder.setOnClickListener {
            detailViewModel.orderFood(menuId)
        }
    }

}