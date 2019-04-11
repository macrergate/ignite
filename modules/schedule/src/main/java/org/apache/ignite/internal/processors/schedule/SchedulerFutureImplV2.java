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

package org.apache.ignite.internal.processors.schedule;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.apache.ignite.IgniteException;
import org.apache.ignite.internal.processors.schedule.spring.IgniteCronTrigger;
import org.apache.ignite.internal.processors.schedule.spring.IgniteSpringSchedulerV2;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.lang.IgniteInClosure;
import org.apache.ignite.scheduler.SchedulerFuture;

public class SchedulerFutureImplV2<R> implements SchedulerFuture<R> {

    private final IgniteCronTrigger expr;

    public SchedulerFutureImplV2(IgniteSpringSchedulerV2 scheduler,
        IgniteCronTrigger expr) {
        this.expr = expr;
    }

    @Override public String id() { return null;
    }

    @Override public String pattern() {
        return expr.pattern();
    }

    @Override public long createTime() {
        return 0;
    }

    @Override public long lastStartTime() {
        return 0;
    }

    @Override public long lastFinishTime() {
        return 0;
    }

    @Override public double averageExecutionTime() {
        return 0;
    }

    @Override public long lastIdleTime() {
        return 0;
    }

    @Override public double averageIdleTime() {
        return 0;
    }

    @Override public long[] nextExecutionTimes(int cnt, long start) throws IgniteException {
        return new long[0];
    }

    @Override public int count() {
        return 0;
    }

    @Override public boolean isRunning() {
        return false;
    }

    @Override public long nextExecutionTime() throws IgniteException {
        return 0;
    }

    @Override public R last() throws IgniteException {
        return null;
    }

    @Override public R get() {
        return null;
    }

    @Override public R get(long timeout) throws IgniteException {
        return null;
    }

    @Override public R get(long timeout, TimeUnit unit) {
        return null;
    }

    @Override public boolean cancel() throws IgniteException {
        return false;
    }

    @Override public boolean isCancelled() {
        return false;
    }

    @Override public boolean isDone() {
        return false;
    }

    @Override public void listen(IgniteInClosure<? super IgniteFuture<R>> lsnr) {

    }

    @Override public void listenAsync(IgniteInClosure<? super IgniteFuture<R>> lsnr, Executor exec) {

    }

    @Override public <T> IgniteFuture<T> chain(IgniteClosure<? super IgniteFuture<R>, T> doneCb) {
        return null;
    }

    @Override public <T> IgniteFuture<T> chainAsync(IgniteClosure<? super IgniteFuture<R>, T> doneCb, Executor exec) {
        return null;
    }
}
