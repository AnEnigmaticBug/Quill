package com.anenigmaticbug.quill.screens.editnote.view.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.anenigmaticbug.quill.R
import kotlinx.android.synthetic.main.viw_fancy_date_viewer.view.*
import org.threeten.bp.LocalDateTime

class FancyDateViewer(ctx: Context, attrs: AttributeSet? = null) : ConstraintLayout(ctx, attrs) {

    init {
        inflate(context, R.layout.viw_fancy_date_viewer, this)

        val attributeValues = context.obtainStyledAttributes(attrs, R.styleable.FancyDateViewer, 0, 0)
        val description = attributeValues.getString(R.styleable.FancyDateViewer_description)

        attributeValues.recycle()

        this.descriptionLBL.text = description
    }

    var datetime: LocalDateTime = LocalDateTime.of(2018, 11, 21, 10, 29)
        set(value) {
            field = value
            this.datePartLBL.text = value.dayOfMonth.doubleDigitify()
            val month = value.month.name.substring(0..2)
            this.nonDatePartLBL.text = "$month ${value.year} ${value.hour.doubleDigitify()}: ${value.minute.doubleDigitify()}"
        }

    /**
     * This is to add a zero as a padding to single digit positive numbers. Double digit positive
     * numbers are left unchanged. All other numbers cause an exception to be thrown.
     * */
    private fun Int.doubleDigitify(): String {
        return when(this) {
            in 0..9   -> "0$this"
            in 10..99 -> this.toString()
            else      -> throw IllegalArgumentException("Can't double digitify $this")
        }
    }
}