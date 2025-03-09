package com.example.diary_final.adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diary_final.R
import com.example.diary_final.ui.DiaryDetailActivity
import com.example.diary_final.room.DiaryEntity
import com.example.diary_final.databinding.DiaryItemBinding

class DiaryAdapter(
    private val context: Context,
    private var diaries: List<DiaryEntity>
) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    inner class DiaryViewHolder(val binding: DiaryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = diaries.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = DiaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val diary = diaries[position]
        holder.binding.apply {
            diaryTitle.text = diary.title
            diaryArticle.text = diary.article
            diaryDate.text = diary.date
            diaryLocation.text = diary.location
            diaryWeather.text = diary.weather

            // 处理图片加载
            diaryImage.setImageDrawable(null) // 清空旧图片，防止 RecyclerView 复用导致错位

            val imageUrl = diary.localImagePath?.takeIf { it.isNotBlank() } ?: diary.networkImageLink
            Glide.with(context)
                .load(imageUrl?.let { Uri.parse(it) }) // 兼容 file:// 和 http://
                .placeholder(R.drawable.loading_placeholder) // 占位图，优化用户体验
                .error(R.drawable.no) // 加载失败时的错误图
                .into(diaryImage)

            // 显示或隐藏 ImageView（Glide 会自动处理）
            diaryImage.visibility = if (imageUrl.isNullOrEmpty()) View.GONE else View.VISIBLE

            // 点击事件
            root.setOnClickListener {
                val intent = Intent(context, DiaryDetailActivity::class.java).apply {
                    putExtra("diaryId", diary.id)
                }
                context.startActivity(intent)
            }
        }
    }


    // 使用 DiffUtil 优化数据更新
    fun updateData(newDiaries: List<DiaryEntity>) {
        val diffCallback = DiaryDiffCallback(diaries, newDiaries)//DiaryDiffCallback 是一个自定义的 DiffUtil.Callback 类。
        //它的作用是比较 diaries 和 newDiaries，找出不同之处（新增、删除、修改的项目）
        val diffResult = DiffUtil.calculateDiff(diffCallback)//DiffUtil.Callback 是干嘛的？
        //DiffUtil 是 Android 提供的智能计算列表差异的工具。
        //它能找出新旧列表之间的变化，而不是直接刷新整个 RecyclerView，提高性能。
        diaries = newDiaries
        diffResult.dispatchUpdatesTo(this) // 这一步通知 RecyclerView 只更新有变化的部分。，提高性能
    }

    // DiffUtil 计算数据变化，提高 RecyclerView 刷新效率
    class DiaryDiffCallback(
        private val oldList: List<DiaryEntity>,
        private val newList: List<DiaryEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    fun getCurrentDiaries(): List<DiaryEntity> = diaries
}
