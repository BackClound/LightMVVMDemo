package cn.yun.onetouch.basemvvmlibrary.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.KeyEvent
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

    /**
     * 获取一个耗时等待对话框
     */
    fun showIndeterminateProgressDialog(context: Context, title: String, horizontal : Boolean):MaterialDialog.Builder {
        return MaterialDialog.Builder(context).title(title).progress(true,0).progressIndeterminateStyle(horizontal)
            .canceledOnTouchOutside(false).backgroundColorRes(R.color.white)
            .keyListener { dialog, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN){
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                    }
                }
                return@keyListener false
            }
    }

    /**
     * 获取基本对话框 不带内容
     */
    fun showBasicDialog(context: Context, title: String): MaterialDialog.Builder {
        return MaterialDialog.Builder(context)
            .title(title).positiveText("确定").negativeText("取消")
    }

    /**
     * 获取基本对话框 不带标题
     */
    fun showBasicDialogNoTitle(context: Context, content: String): MaterialDialog.Builder {
        return MaterialDialog.Builder(context).content(content).positiveText("确定").negativeText("取消")
    }

    /**
     * 获取基本对话框 没有取消按钮
     */
    fun showBasicDialogNoCancel(context: Context,title: String,content: String): MaterialDialog.Builder{
        return MaterialDialog.Builder(context).title(title).content(content).positiveText("OK")

    }

    /**
     * 获取基本对话框
     */
    fun showBasicDialog(context: Context,title: String,content: String):MaterialDialog.Builder{
        return MaterialDialog.Builder(context).title(title).content(content).positiveText("OK").negativeText("NO")
    }

}