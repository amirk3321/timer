package timer.op.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import timer.op.timer.utils.PrefUtil
import java.util.*

class MainActivity : AppCompatActivity() {
    enum class TimerState{
        running,pause,stop
    }

    companion object {
        val RC_ALARM=123
        fun toast(context: Context,msg:String){
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun setAlaram(context: Context, nowSec : Long, remaingSecund : Long) : Long{
            val wekeupTime=(nowSec+remaingSecund) * 1000
            val alramManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent=Intent(context,timeExpiredReceiver::class.java)
            val pandingIntent=PendingIntent.getBroadcast(context,RC_ALARM,intent,0)
            alramManager.setExact(AlarmManager.RTC_WAKEUP,wekeupTime,pandingIntent)
            PrefUtil.setAlarmTime(nowSec,context)
            return wekeupTime
        }
        @RequiresApi(Build.VERSION_CODES.M)
        fun onRemoveAlarm(context: Context){
            val intent=Intent(context,timeExpiredReceiver::class.java)
            val pendingIntent=PendingIntent.getBroadcast(context, RC_ALARM,intent,0)
            val alramManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alramManager.cancel(pendingIntent)
            PrefUtil.setAlarmTime(0,context)
        }
        val nowSec: Long
            get() = Calendar.getInstance().timeInMillis/1000

    }

    lateinit var timer : CountDownTimer

    private var timeLengthSecound =0L
    private var timerState=TimerState.stop

    //also keep track reaming time sec
    private var seceoundRemeaning=0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        play.setOnClickListener {
            startTimer()
            toast(this,"play")
            timerState=TimerState.running
            updatebutton()

        }
        pause.setOnClickListener {
            toast(this,"pause")
            timer.cancel()
            timerState=TimerState.pause
            updatebutton()
        }
        stop.setOnClickListener {
            toast(this,"stop")
            timer.cancel()
            onTimerfinish()

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        initTimer()
        Log.e("called","onResume")
        //TODO :hide notification
        onRemoveAlarm(this)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPause() {
        super.onPause()
        if (timerState==TimerState.running){
            timer.cancel()
            //TODO : start background Tiemr show notification
            val wakeuptime=setAlaram(this,nowSec,seceoundRemeaning)
        }else if(timerState==TimerState.pause){
            //TODO : Show notificaTION
        }

        PrefUtil.setPreviousTimerLenthSecound(timeLengthSecound,this)
        PrefUtil.setSecundRemaing(seceoundRemeaning,this)
        PrefUtil.setTimerState(timerState,this)

    }
    private fun initTimer(){
        timerState=PrefUtil.getTimerState(this)
        if (timerState==TimerState.stop)
            setNewTimerState()
        else
            setPreviousTimerStateLength()

        seceoundRemeaning=if (timerState==TimerState.running || timerState== TimerState.pause)
            PrefUtil.getSecundRemaining(this)
        else
            timeLengthSecound

        //TODO : Change secondReaming accurding to background timer is stop
        val alarmsetTime=PrefUtil.getAlarmTime(this)
        if (alarmsetTime>0)
            seceoundRemeaning-=nowSec-alarmsetTime
        if (alarmsetTime<=0)
            onTimerfinish()
        else if (timerState==TimerState.running)
            startTimer()

        updatebutton()
        updateCountDownTimer()
    }
    fun onTimerfinish(){
        timerState=TimerState.stop
        setNewTimerState()
        progressbar.progress=0

        PrefUtil.setSecundRemaing(timeLengthSecound,this)
        seceoundRemeaning=timeLengthSecound

        updatebutton()
        updateCountDownTimer()

    }
    fun startTimer(){
        timerState=TimerState.running

        timer=object : CountDownTimer(seceoundRemeaning*1000,1000){
            override fun onFinish() =onTimerfinish()

            override fun onTick(millisUntilFinished: Long) {
                seceoundRemeaning=millisUntilFinished/1000

                updateCountDownTimer()
            }
        }.start()
    }
    fun setNewTimerState(){
        val lenthInMenit=PrefUtil.getTimeLenth(this)
        timeLengthSecound=(lenthInMenit *60L)
        progressbar.max=timeLengthSecound.toInt()
    }
    fun setPreviousTimerStateLength(){
        timeLengthSecound=PrefUtil.getPreviousTimeLenthSecond(this)
        progressbar.max=timeLengthSecound.toInt()
    }
    fun updateCountDownTimer(){
        val minutUntilFinshed=seceoundRemeaning/60
        val secundMinutsFinsihed=seceoundRemeaning-minutUntilFinshed*60
        val secundStr=secundMinutsFinsihed.toString()
                timerUI.text="$minutUntilFinshed:${
        if (secundStr.length == 2) secundStr else "0"+secundStr}"

        progressbar.progress=(timeLengthSecound - seceoundRemeaning).toInt()

    }
    fun updatebutton(){
        when(timerState){
            TimerState.running ->{
                play.isEnabled=false
                pause.isEnabled=true
                stop.isEnabled=true
            }
            TimerState.pause ->{
                play.isEnabled=true
                pause.isEnabled=false
                stop.isEnabled=true
            }
            TimerState.stop ->{
                play.isEnabled=true
                pause.isEnabled=false
                stop.isEnabled=false
            }
        }
    }

}
