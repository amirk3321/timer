package timer.op.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timer.op.timer.utils.PrefUtil

class timeExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        PrefUtil.setTimerState(MainActivity.TimerState.stop,context)
        PrefUtil.setAlarmTime(0,context)
    }

}
