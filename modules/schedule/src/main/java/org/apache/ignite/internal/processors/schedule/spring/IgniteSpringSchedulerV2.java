/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.schedule.spring;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import org.apache.ignite.internal.processors.schedule.Scheduler;
import org.apache.ignite.internal.processors.schedule.SchedulerFutureImplV2;
import org.apache.ignite.scheduler.SchedulerFuture;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class IgniteSpringSchedulerV2 implements Scheduler {

    /** Spring Task scheduler implementation. */
    private ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    public IgniteSpringSchedulerV2() {
        taskScheduler.setThreadNamePrefix("task-scheduler-#");

        taskScheduler.initialize();
    }

    @Override public <R> SchedulerFuture<R> schedule(Callable<R> c, String pattern) {
        IgniteCronTrigger trigger = IgniteCronTrigger.valueOf(pattern);

        SchedulerFutureImplV2<R> fut = new SchedulerFutureImplV2<R>(this, trigger);

        ScheduledFuture<?> schFut = taskScheduler.schedule(task(c, fut), trigger);

        fut.setScheduledFuture(schFut);

        return fut;
    }

    private <R> void doSchedule(Callable<R> c, IgniteCronTrigger trigger, SchedulerFutureImplV2<R> fut) {
        if (!fut.isCancelled())
            taskScheduler.schedule(task(c, fut), trigger);
    }

    private <R> Runnable task(Callable<R> c,
        SchedulerFutureImplV2<R> fut) {
        return null;
    }

}
