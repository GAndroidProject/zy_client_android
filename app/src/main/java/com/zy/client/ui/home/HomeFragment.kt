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
package com.zy.client.ui.home

import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.zy.client.R
import com.zy.client.http.ConfigManager
import com.zy.client.http.NetRepository
import com.zy.client.base.BaseFragment
import com.zy.client.common.AppRouter
import com.zy.client.common.isHealthLife
import com.zy.client.common.switchHealthLife
import com.zy.client.utils.ext.noNull
import com.zy.client.utils.ext.toastShort

class HomeFragment : BaseFragment() {

    private lateinit var floatBtn: FloatingActionButton
    private lateinit var ivHistory: ImageView
    private lateinit var tvSearch: TextView
    private var mContainer: HomeTabPagerFragment? = null
    private var mRepo: NetRepository? = null
    private var mSourceDialog: BasePopupView? = null

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        super.initView()
        mRepo = ConfigManager.curUseSourceConfig()
        floatBtn = rootView.findViewById(R.id.floatBtn)
        ivHistory = rootView.findViewById(R.id.iv_home_history)
        tvSearch = rootView.findViewById(R.id.tv_home_search)
    }

    override fun initListener() {
        super.initListener()
        ivHistory.setOnLongClickListener {
            val isHealthyLife = isHealthLife()
            switchHealthLife(!isHealthyLife)
            if (isHealthyLife) toastShort("健康生活已关闭!") else toastShort("健康生活已开启!")
            initData()
            true
        }
        ivHistory.setOnClickListener {
            AppRouter.toHistoryActivity(baseActivity)
//            PermissionManager.proceedStoragePermission(baseActivity){
//                if (it) {
//                    val intent = Intent(baseActivity, DownloadService::class.java)
//                    baseActivity.startService(intent)
//                }
//            }
        }

        tvSearch.setOnClickListener {
            AppRouter.toSearchActivity(baseActivity)
        }

        floatBtn.setOnClickListener {
            //选择视频源
            if (mSourceDialog == null) {
                val values = ConfigManager.sourceConfigs.values
                val keys = ConfigManager.sourceConfigs.keys.toTypedArray()
                mSourceDialog = XPopup.Builder(requireActivity())
                    .asCenterList("视频源",
                        values.map { it.name }.toTypedArray(),
                        null,
                        keys.indexOfFirst { it == mRepo?.req?.key }
                    ) { position, _ ->
                        mRepo = ConfigManager.generateSource(keys[position])
                        ConfigManager.saveCurUseSourceConfig(mRepo?.req?.key)
                        initData()
                    }
                    .bindLayout(R.layout.fragment_search_result)
            }
            mSourceDialog?.show()
        }
    }

    override fun initData() {
        super.initData()
        tvSearch.hint = mRepo?.req?.name.noNull("搜索")
        mContainer = HomeTabPagerFragment()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.container, mContainer ?: return)
            .commitNowAllowingStateLoss()
    }

    override fun refreshData() {
        super.refreshData()
        if (isAdded) mContainer?.refreshData()
    }

}