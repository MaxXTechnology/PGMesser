package us.pinguo.messer.home

/**
 * Created by MR on 2017/8/21.
 */
interface LogReceiver {
    fun onReceiveLog(time: Long, level: Int, tag: String, msg: String)
}