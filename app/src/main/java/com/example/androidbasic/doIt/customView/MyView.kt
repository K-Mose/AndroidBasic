package com.example.androidbasic.doIt.customView

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.androidbasic.R
import com.example.androidbasic.databinding.ViewMyCustomBinding

/*
 * CustomView
 */
@TargetApi(Build.VERSION_CODES.BASE)
class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {
    // View 상속, @JvmOverloads 사용 안 할 때
//    constructor(context: Context) : this(context, null)
//    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    init {
        val binding = ViewMyCustomBinding.inflate(LayoutInflater.from(context), this)
//        LayoutInflater.from(context).inflate(R.layout.view_my_custom,this, true)
        orientation = VERTICAL
        attrs?.let{
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.CustomView,
                0,
                0
            )
            val title = resources.getText(typedArray
                .getResourceId(
                    R.styleable.CustomView_my_custom_title,
                    R.string.custom_view1
                ))
            // 리소스 id에 값 저장
            binding.myTitle.text = title
            binding.myEdit.hint = resources.getString(R.string.hint_text)

            typedArray.recycle() // 재활용
        }
    }
}