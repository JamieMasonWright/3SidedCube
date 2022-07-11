package com.jj.a3sidedcube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jj.a3sidedcube.databinding.NetworkStateItemBinding

class LoadingAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        val progress = holder.binding.progressBarItem
        val txtErrorMessage = holder.binding.errorMsgItem
        val errorBtn = holder.binding.retyBtn

        if (loadState is LoadState.Loading) {
            progress.isVisible = true
            txtErrorMessage.isVisible = false
            errorBtn.isVisible = false
        }else{
            progress.isVisible = false
        }
        if (loadState is LoadState.Error) {
            txtErrorMessage.isVisible = true
            txtErrorMessage.text = loadState.error.localizedMessage
            errorBtn.isVisible = true
            progress.isVisible = false
        }
        errorBtn.setOnClickListener {
            retry.invoke()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            NetworkStateItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class LoadStateViewHolder(val binding: NetworkStateItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}