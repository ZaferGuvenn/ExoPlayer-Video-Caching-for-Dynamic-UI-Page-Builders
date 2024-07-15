package com.siro.android.base

//import com.siro.android.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment()/*, ConnectivityListener*/ {
    protected lateinit var className: String
    protected lateinit var binding: ViewDataBinding
    private var isFirstTime = true
    private var isRecreate = false

    public var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        className = javaClass.name

        getViewModel()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this@BaseFragment.savedInstanceState = savedInstanceState


        if (isRecreate.not()) {
            if (!this::binding.isInitialized) {
                binding = DataBindingUtil.inflate(
                    inflater,
                    getFragmentLayout(),
                    container,
                    false
                )
            }
        } else {
            binding = DataBindingUtil.inflate(
                inflater,
                getFragmentLayout(),
                container,
                false
            )
            isFirstTime = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstTime) {
            getViewBinding()
            setLanguageData()
            init()
            setListeners()

            isFirstTime = false
        }

        updateListener()

    }

    open fun updateListener() {

    }

    abstract fun setLanguageData()
    abstract fun init()
    abstract fun getFragmentLayout(): Int
    abstract fun getViewModel()
    abstract fun getViewBinding()
    abstract fun setListeners()

    //////////////////////////////////////////////////////


    fun closeKeypad() {
        val activity = activity
        if (activity is BaseActivity) (activity).closeKeypad()
    }

    fun recreateBinding(isRecreate: Boolean) {
        this.isRecreate = isRecreate
    }

    fun showKeypad() {
        val activity = activity
        if (activity is BaseActivity) (activity).showKeypad()
    }

}