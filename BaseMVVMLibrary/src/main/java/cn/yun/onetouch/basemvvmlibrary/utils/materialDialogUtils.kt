package cn.yun.onetouch.basemvvmlibrary.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import cn.yun.onetouch.basemvvmlibrary.R
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme

object materialDialogUtils {

    fun showThemed(context: Context, title: String, content: String) {
        MaterialDialog.Builder(context).title(title).content(content).positiveText("agree")
            .negativeText("disagree").positiveColorRes(R.color.white)
            .negativeColorRes(R.color.white).titleGravity(GravityEnum.CENTER)
            .titleColorRes(R.color.white).contentColorRes(R.color.white)
            .backgroundColorRes(R.color.material_blue_grey_800).dividerColorRes(R.color.white)
            .btnSelector(R.drawable.md_selector, DialogAction.POSITIVE)
            .positiveColor(Color.WHITE)
            .theme(Theme.DARK).autoDismiss(true)
            .showListener { //Dialog show
            }.cancelListener {
                //dialog dis
            }.dismissListener {

            }.show()
    }
}
}