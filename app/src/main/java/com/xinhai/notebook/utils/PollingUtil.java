package com.xinhai.notebook.utils;

import static android.os.Looper.getMainLooper;
import android.os.Handler;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**

 * 轮询工具类(Handler实现)

 */
public class PollingUtil {

        private final Handler mHandler;
        private final Map mTaskMap = new HashMap();
        public PollingUtil(Handler handler) {
                mHandler = handler;
        }

        /**
         * 开启定时任务
         * @param runnable 任务
         * @param interval 时间间隔
         */
        public void startPolling(Runnable runnable, long interval) {
            startPolling(runnable, interval, false);
        }

        /**
         * 开启定时任务
         * @param runnable 任务
         * @param interval 时间间隔
         * @param runImmediately 是否先立即执行一次
         */

        public void startPolling(final Runnable runnable, final long interval, boolean runImmediately) {
            if (runImmediately) {
                runnable.run();
            }

            Runnable task = (Runnable) mTaskMap.get(runnable);
            if (task == null) {
                    task = new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                    post(runnable, interval);
                }
            };
                mTaskMap.put(runnable, task);
            }
            post(runnable, interval);
        }

        /**
         * 结束某个定时任务
         * @param runnable 任务
         */
        public void endPolling(Runnable runnable) {
            if (mTaskMap.containsKey(runnable)) {
                mHandler.removeCallbacks((Runnable) mTaskMap.get(runnable));
            }
        }

        private void post(Runnable runnable, long interval) {
            Runnable task = (Runnable) mTaskMap.get(runnable);
            mHandler.removeCallbacks(task);
            mHandler.postDelayed(task, interval);
        }

    //开启定时任务
    //每3秒打印一次日志
    PollingUtil pollingUtil = new PollingUtil(new Handler(getMainLooper()));
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e("MainActivity", "----------handler 定时轮询任务----------");
            pollingUtil.startPolling(runnable, 3000, true);
            //停止定时任务
            pollingUtil.endPolling(runnable);
        }
    };

}
