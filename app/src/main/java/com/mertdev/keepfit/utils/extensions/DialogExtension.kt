package com.mertdev.keepfit.utils.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window

fun Dialog.initDialog(layout: Int, cancelable: Boolean = false) {
    this.setCancelable(cancelable)
    this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    this.setContentView(layout)
}
