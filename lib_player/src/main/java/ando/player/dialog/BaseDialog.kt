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
package ando.player.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager

internal class BaseDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    override fun show() {
        // Set the dialog to not focusable.
        window!!.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        // Show the dialog with NavBar hidden.
        super.show()
        // Set the dialog to focusable again.
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    /**
     * 设置dialog消失显示广播
     */
    private fun initDefaultDialogListener() {
        setOnShowListener { }
        setOnDismissListener {
            //Log.w("123","Dismiss....................")
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //Log.w("123","onDetachedFromWindow....................")
    }

    init {
        initDefaultDialogListener()
    }
}