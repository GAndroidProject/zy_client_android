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
package com.zy.client.utils.ext

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.zy.client.App

/**
 * Title: ToastExt
 */

fun Context.toastShort(text: String?) = ToastUtils.showShort(text)
fun Context.toastLong(text: String?) = ToastUtils.showLong(text)
fun Context.toastShort(@StringRes resId: Int) = ToastUtils.showShort(resId)
fun Context.toastLong(@StringRes resId: Int) = ToastUtils.showLong(resId)

fun Fragment.toastShort(text: String?) = activity?.toastShort(text)
fun Fragment.toastLong(text: String?) = activity?.toastLong(text)
fun Fragment.toastShort(@StringRes resId: Int) = activity?.toastShort(resId)
fun Fragment.toastLong(@StringRes resId: Int) = activity?.toastLong(resId)

fun View.toastShort(text: String?) = context?.toastShort(text)
fun View.toastLong(text: String?) = context?.toastLong(text)
fun View.toastShort(@StringRes resId: Int) = context?.toastShort(resId)
fun View.toastLong(@StringRes resId: Int) = context?.toastLong(resId)

object ToastUtils {

    private var toast: Toast? = null

    fun showShort(@StringRes resId: Int) {
        showShort(App.instance.resources.getString(resId))
    }

    fun showShort(text: String?) {
        if (text.isNullOrBlank()) return
        if (toast == null) {
            toast = Toast.makeText(App.instance, text, Toast.LENGTH_SHORT)
        } else {
            toast?.setText(text)
        }
        toast?.show()
    }

    fun showLong(@StringRes resId: Int) {
        showLong(App.instance.resources.getString(resId))
    }

    fun showLong(text: String?) {
        if (text.isNullOrBlank()) return
        if (toast == null) {
            toast = Toast.makeText(App.instance, text, Toast.LENGTH_LONG)
        } else {
            toast?.setText(text)
        }
        toast?.show()
    }

}