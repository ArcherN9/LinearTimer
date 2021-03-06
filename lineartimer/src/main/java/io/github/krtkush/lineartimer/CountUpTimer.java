package io.github.krtkush.lineartimer;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by kartikeykushwaha on 02/02/17.
 * <p>
 * CountUpTimer is similar to the Android CountDownTimer in respect to implementation and
 * behaviour.
 */
public abstract class CountUpTimer {

    private final long duration;
    private final long interval;
    private long base;

    /**
     * Instantiates a new Count up timer.
     *
     * @param duration the duration
     * @param interval the interval
     */
    public CountUpTimer(long duration, long interval) {
        // We are adding extra 3 seconds because otherwise, this timer is
        // behind the animation (somehow).
        this.duration = duration + 3000;
        this.interval = interval;
    }

    /**
     * Start.
     */
    public void start() {
        base = SystemClock.elapsedRealtime();
        handler.sendMessage(handler.obtainMessage(MSG));
    }

    /**
     * Stop.
     */
    public void stop() {
        handler.removeMessages(MSG);
        onFinish();
    }

    /**
     * On tick.
     *
     * @param elapsedTime the elapsed time
     */
    abstract public void onTick(long elapsedTime);

    /**
     * On finish.
     */
    abstract public void onFinish();

    private static final int MSG = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (CountUpTimer.this) {
                long elapsedTime = SystemClock.elapsedRealtime() - base;

                onTick(elapsedTime);
                sendMessageDelayed(obtainMessage(MSG), interval);

                // Stop the timer if it has run for the required duration.
                if(elapsedTime >= duration)
                    stop();
            }
        }
    };
}
