package com.siro.android.base

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseActivity : AppCompatActivity() {

    lateinit var className: String
    protected lateinit var binding: ViewDataBinding
    private lateinit var loaderDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        className = javaClass.name
        init()
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun init() {
        initLoader()

        binding = DataBindingUtil.setContentView(this, getActivityLayout())

        getViewBinding()

        setClickListeners()
    }

    abstract fun getActivityLayout(): Int

    abstract fun getViewBinding()

    abstract fun setClickListeners()


    private fun initLoader() {
        loaderDialog = Dialog(this@BaseActivity)
        loaderDialog.apply {
            window?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@BaseActivity,
                        android.R.color.transparent
                    )
                )
            )

            setCancelable(false)
        }



    }

    fun closeKeypad() {
        val view = this.currentFocus
        view?.let { v ->
            val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun showKeypad() {
        val view = this.currentFocus
        view?.let { v ->
            val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(v, 0)
        }
    }
}