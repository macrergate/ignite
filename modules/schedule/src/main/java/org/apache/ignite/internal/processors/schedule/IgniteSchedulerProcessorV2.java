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

import java.util.concurrent.Callable;
import org.apache.ignite.internal.GridKernalContext;
import org.apache.ignite.internal.processors.GridProcessorAdapter;
import org.apache.ignite.internal.processors.schedule.spring.IgniteSpringSchedulerV2;
import org.apache.ignite.scheduler.SchedulerFuture;

public class IgniteSchedulerProcessorV2 extends GridProcessorAdapter implements IgniteScheduleProcessorAdapter {

    private final IgniteSpringSchedulerV2 delegate;

    /**
     * @param ctx Kernal context.
     */
    protected IgniteSchedulerProcessorV2(GridKernalContext ctx) {
        super(ctx);

        delegate = new IgniteSpringSchedulerV2();
    }

    @Override public SchedulerFuture<?> schedule(Runnable c, String pattern) {
        return delegate.schedule(() -> {
                c.run();

                return null;
            }, pattern);
    }

    @Override public <R> SchedulerFuture<R> schedule(Callable<R> c, String pattern) {
        return delegate.schedule(c, pattern);
    }
}
