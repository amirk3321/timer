package timer.op.timer.utils

import android.content.Context
import android.preference.PreferenceManager
import timer.op.timer.MainActivity

class PrefUtil {
    companion object {
        //non_previous timer
        fun getTimeLenth(context : Context) : Int{
            return 1
        }
        val PREVIOUS_TIMER_LENTH_SECOND_ID="com.timer.length_second"
        //Previous timer set
        fun getPreviousTimeLenthSecond(context : Context) : Long{
            val preferences= PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENTH_SECOND_ID,0)
        }
        fun setPreviousTimerLenthSecound(second : Long,context: Context){
            val editor= PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENTH_SECOND_ID,second)
            editor.apply()
        }
        val TIMER_STATE_ID="com.timer_State"
        fun getTimerState(context: Context) : MainActivity.TimerState{
            val preferences= PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal=preferences.getInt(TIMER_STATE_ID,0)
            return MainActivity.TimerState.values()[ordinal]
        }
        fun setTimerState(state : MainActivity.TimerState,context: Context){
            val editor= PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal=state.ordinal
            editor.putInt(TIMER_STATE_ID,ordinal)
            editor.apply()
        }
        private const val SECUND_REMANNING="com.Second_Remaining"
        fun getSecundRemaining(context : Context) : Long{
            val preferences= PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECUND_REMANNING,0)
        }
        fun setSecundRemaing(second: Long,context: Context){
            val eidtor= PreferenceManager.getDefaultSharedPreferences(context).edit()
            eidtor.putLong(SECUND_REMANNING,second)
            eidtor.apply()
        }
        const val TIMER_ALARAM_ID="com.alarm_ID"
        fun getAlarmTime(context :Context):Long{
            val preference =PreferenceManager.getDefaultSharedPreferences(context)
            return preference.getLong(TIMER_ALARAM_ID,0)
        }
        fun setAlarmTime(time : Long,context : Context){
            val edit=PreferenceManager.getDefaultSharedPreferences(context).edit()
            edit.putLong(TIMER_ALARAM_ID,time)
            edit.apply()
        }
    }

}