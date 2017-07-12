package us.pinguo.messer.demo

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.KProperty

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("us.pinguo.messer.demo", appContext.packageName)
    }

    @Test
    fun myLazyTest() {

        var q: Int by myLazy {
            2
        }

        print(q)

        var q2: String by myLazy {
            "Hello World!"
        }
        q2 = "abcd"

        print(q2)
    }

    fun<T : Any> myLazy(initialize: () -> T ) = lazy2(initialize)


    class lazy2<T> (val initialize: ()->T) {

        var p: T? = null

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            if (p == null) {
                p = initialize()
            }

            return p!!
        }
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            p = value
        }
    }

}
