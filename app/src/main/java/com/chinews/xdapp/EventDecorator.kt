package com.chinews.xdapp

import android.content.Context
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import androidx.core.content.ContextCompat
import com.chinews.xdapp.R
import java.util.HashSet

class EventDecorator(dates: Collection<CalendarDay>?, private val cy: String, private val context: Context) : DayViewDecorator {
    //private int color;
    private val dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        when(cy){
            "志報" -> view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.chinews_background)!!)
            "葉の報" -> view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.yipnews_background)!!)
            "more" -> view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.yipchinews)!!)
            else -> view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.othernews)!!)
        }
    }
}