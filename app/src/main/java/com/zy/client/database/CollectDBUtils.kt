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

import com.zy.client.utils.thread.ThreadUtils
import com.zy.client.utils.thread.CustomTask
import org.litepal.LitePal
import java.util.*

object CollectDBUtils {

    private fun save(collectModel: CollectModel): Boolean {
        if (collectModel.uniqueKey.isNullOrBlank() || collectModel.videoId.isNullOrBlank() || collectModel.sourceKey.isNullOrBlank()) {
            return false
        }
        if (collectModel.isSaved) {
            collectModel.delete()
        }
        return collectModel.save()
    }

    private fun searchAll(): List<CollectModel>? {
        return LitePal.findAll(CollectModel::class.java) as? ArrayList<CollectModel>
    }

    fun search(uniqueKey: String?): CollectModel? {
        if (uniqueKey.isNullOrBlank()) return null
        return LitePal.where("uniqueKey = ?", uniqueKey).findFirst(CollectModel::class.java)
    }

    fun saveAsync(collectModel: CollectModel, callback: ((Boolean) -> Unit)? = null) {
        ThreadUtils.executeByCached(CustomTask({
            save(collectModel)
        }, {
            callback?.invoke(it ?: false)
        }))
    }

    fun searchAllAsync(callback: ((List<CollectModel>?) -> Unit)?) {
        ThreadUtils.executeByCached(CustomTask({
            searchAll()
        }, {
            callback?.invoke(it)
        }))
    }

    fun searchAsync(uniqueKey: String?, callback: ((CollectModel?) -> Unit)?) {
        ThreadUtils.executeByCached(CustomTask<CollectModel?>({
            search(uniqueKey)
        }, {
            callback?.invoke(it)
        }))
    }

    fun deleteAll(): Boolean {
        return LitePal.deleteAll(CollectModel::class.java) > 0
    }

    fun delete(uniqueKey: String?): Boolean {
        if (uniqueKey.isNullOrBlank()) return false

        return LitePal.where("uniqueKey = ?", uniqueKey).findFirst(CollectModel::class.java)
            ?.delete() ?: 0 > 0
    }
}