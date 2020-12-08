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
package com.zy.client.database

import com.zy.client.bean.VideoHistory
import com.zy.client.utils.thread.ThreadUtils
import com.zy.client.utils.thread.CustomTask
import org.litepal.LitePal

object HistoryDBUtils {

    private fun save(history: VideoHistory?): Boolean {
        if (history == null || history.uniqueId?.isBlank() == true) {
            return false
        }

        //if (history.isSaved) return true
        LitePal.where(" uniqueId = ? ", history.uniqueId)
            .findFirst(VideoHistory::class.java)?.delete()
        return history.save()
    }

    private fun searchAll(): List<VideoHistory>? {
        return LitePal.findAll(VideoHistory::class.java)
    }

    private fun search(uniqueId: String?): VideoHistory? {
        if (uniqueId.isNullOrBlank()) return null
        return LitePal.where(" uniqueId = ? ", uniqueId)
            .findFirst(VideoHistory::class.java)
    }

    fun isExit(): Boolean = LitePal.isExist(VideoHistory::class.java)

    fun count(): Int {
        return LitePal.count(VideoHistory::class.java)
    }

    fun saveAllAsync(histories: List<VideoHistory>, callback: ((Boolean) -> Unit)? = null) {
        ThreadUtils.executeByCached(CustomTask({
            LitePal.saveAll(histories)
        }, {
            callback?.invoke(it ?: false)
        }))
    }

    /**
     *  val uniqueId = "${mVideoDetail?.sourceKey}${mVideoDetail?.tid}${mVideoDetail?.id}"
     */
    fun saveAsync(history: VideoHistory, callback: ((Boolean) -> Unit)? = null) {
        ThreadUtils.executeByCached(CustomTask({
            save(history)
        }, {
            callback?.invoke(it ?: false)
        }))
    }

    fun searchAllAsync(callback: ((List<VideoHistory>?) -> Unit)?) {
        ThreadUtils.executeByCached(CustomTask({
            searchAll()
        }, {
            callback?.invoke(it)
        }))
    }

    fun searchAsync(
        uniqueId: String?,
        callback: ((VideoHistory?) -> Unit)?
    ) {
        ThreadUtils.executeByCached(CustomTask<VideoHistory?>({
            search(uniqueId)
        }, {
            callback?.invoke(it)
        }))
    }

    fun deleteAll(): Boolean {
        return LitePal.deleteAll(VideoHistory::class.java) > 0
    }

    fun delete(uniqueId: String?): Boolean {
        if (uniqueId.isNullOrBlank()) return false
        return LitePal.where(" uniqueId = ? ", uniqueId)
            .findFirst(VideoHistory::class.java)
            ?.delete() ?: 0 > 0
    }

}