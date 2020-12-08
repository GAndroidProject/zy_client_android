/**
 * Copyright 2020 javakam
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zy.client.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import com.zy.client.R
import com.zy.client.database.SearchHistoryDBUtils

/**
 * 搜索历史
 *
 * @author javakam
 */
class SearchHistoryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var tagGroup: TagContainerLayout
    private var ivDelete: ImageView

    init {
        // 添加Popup窗体内容View
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.layout_search_history, this, false)
        // 事先隐藏，等测量完毕恢复，避免View影子跳动现象。
        contentView.alpha = 0f
        addView(contentView)

        ivDelete = contentView.findViewById(R.id.ivDelete)
        tagGroup = contentView.findViewById(R.id.tagGroup)

        ivDelete.setOnClickListener {
            //清除全部记录
            if (SearchHistoryDBUtils.deleteAll()) {
                tagGroup.tags = emptyList()
            }
        }

        tagGroup.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onTagClick(position: Int, text: String?) {
                if (!text.isNullOrBlank()) {
                    onSelectListener?.invoke(text)
                }
            }

            override fun onTagLongClick(position: Int, text: String?) {
            }

            override fun onSelectedTagDrag(position: Int, text: String?) {
            }

            override fun onTagCrossClick(position: Int) {
            }
        })

        contentView.alpha = 1f
    }

    var onSelectListener: ((searchWord: String) -> Unit)? = null

    fun updateHistory() {
        SearchHistoryDBUtils.searchAllAsync {
            if (!it.isNullOrEmpty()) {
                tagGroup.tags = it.map { model -> model.searchWord }
            }
        }
    }

}