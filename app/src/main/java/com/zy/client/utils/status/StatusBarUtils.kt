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
package com.zy.client.utils.status

import android.R
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.zy.client.utils.status.OSUtils.isFlyme
import com.zy.client.utils.status.OSUtils.isMiui
import com.zy.client.utils.status.OSUtils.isOppo

/**
 * Title:StatusBarUtils
 *
 * Description: https://github.com/wuhenzhizao/android-titlebar
 */
object StatusBarUtils {
    /**
     * 设置状态栏背景
     * v21
     * <item name="android:windowTranslucentStatus">false</item>
     * <item name="android:windowTranslucentNavigation">true</item>
     * <item name="android:statusBarColor">@android:color/transparent</item>
     */
    fun setStatusBarView(activity: Activity, @ColorRes statusBarColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = activity.findViewById<View>(R.id.content) as FrameLayout
            val count = decorView.childCount
            if (count > 0) {
                val statusBarHeight = getStatusBarHeight(activity)
                val layout = decorView.getChildAt(0)
                val layoutParams = layout.layoutParams as FrameLayout.LayoutParams
                layoutParams.topMargin = statusBarHeight
                val statusBarView: View
                if (count > 1) {
                    statusBarView = decorView.getChildAt(1) as View
                } else {
                    statusBarView = View(activity)
                    val viewParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight
                    )
                    statusBarView.layoutParams = viewParams
                    decorView.addView(statusBarView)
                }
                statusBarView.setBackgroundResource(statusBarColor)
            }
        }
    }

    fun supportTransparentStatusBar(): Boolean {
        return (isMiui
                || isFlyme
                || isOppo && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    }

    /**
     * 设置状态栏透明
     */
    @Suppress("DEPRECATION")
    fun transparentStatusBar(window: Window) {
        if (isMiui || isFlyme) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                transparentStatusBarAbove21(window)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        } else if (isOppo && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transparentStatusBarAbove21(window)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            transparentStatusBarAbove21(window)
        }
    }

    @Suppress("DEPRECATION")
    @TargetApi(21)
    private fun transparentStatusBarAbove21(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 设置状态栏图标白色主题
     */
    fun setLightMode(window: Window) {
        if (isMiui) {
            setMIUIStatusBarDarkMode(window, false)
        } else if (isFlyme) {
            setFlymeStatusBarDarkMode(window, false)
        } else if (isOppo) {
            setOppoStatusBarDarkMode(window, false)
        } else {
            setStatusBarDarkMode(window, false)
        }
    }

    /**
     * 设置状态栏图片黑色主题
     */
    fun setDarkMode(window: Window) {
        when {
            isMiui -> {
                setMIUIStatusBarDarkMode(window, true)
            }
            isFlyme -> {
                setFlymeStatusBarDarkMode(window, true)
            }
            isOppo -> {
                setOppoStatusBarDarkMode(window, true)
            }
            else -> {
                setStatusBarDarkMode(window, true)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setStatusBarDarkMode(window: Window, darkMode: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (darkMode) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }

    @SuppressLint("PrivateApi")
    private fun setMIUIStatusBarDarkMode(window: Window, darkMode: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val clazz: Class<out Window> = window.javaClass
            try {
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                val darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                extraFlagField.invoke(window, if (darkMode) darkModeFlag else 0, darkModeFlag)
            } catch (e: Exception) {
            }
        }
        setStatusBarDarkMode(window, darkMode)
    }

    private fun setFlymeStatusBarDarkMode(window: Window, darkMode: Boolean) {
        FlymeStatusBar.setStatusBarDarkIcon(window, darkMode)
    }

    private const val SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010

    @Suppress("DEPRECATION")
    private fun setOppoStatusBarDarkMode(window: Window, darkMode: Boolean) {
        var vis = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vis = if (darkMode) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vis = if (darkMode) {
                vis or SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT
            } else {
                vis and SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT.inv()
            }
        }
        window.decorView.systemUiVisibility = vis
    }

    /**
     * 设置状态栏颜色和透明度
     */
    fun setStatusBarColor(window: Window, @ColorInt color: Int, alpha: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = calculateStatusColor(color, alpha)
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取导航栏高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 检测是否有虚拟导航栏
     */
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            @SuppressLint("PrivateApi") val systemPropertiesClass =
                Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return hasNavigationBar
    }
}