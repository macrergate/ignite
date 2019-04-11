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

import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.ignite.IgniteException;
import org.apache.ignite.internal.util.typedef.internal.A;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.StringUtils;

/**
 * Cron Trigger with extednded functionality: initial delay and maximum number of calls
 */
public class IgniteCronTrigger implements Trigger {

    /** Extended Pattern. */
    private final String pat;


    /** Scheduling delay in seconds parsed from pattern. */
    private final int delay;

    /** Number of maximum task calls parsed from pattern
     * or -1 if maximum not set. */
    private final int maxCalls;

    private final IgniteCronSequenceGenerator sequenceGenerator;

    /** Cron regex. */
    private static final Pattern CRON_REGEX = Pattern.compile("(\\{(\\*|\\d+),\\s*(\\*|\\d+)\\})?(.*)");

    public IgniteCronTrigger(int delay, int maxCalls, String cron, String pat) {
        this.sequenceGenerator = new IgniteCronSequenceGenerator(delay, cron);

        this.delay = delay;

        this.maxCalls = maxCalls;

        this.pat = pat;
    }

    /**
     * Parse delay, number of task calls and mere cron expression from extended pattern that looks like  "{n1,n2} * * *
     * * *".
     *
     * @throws IgniteException Thrown if pattern is invalid.
     */
    public static IgniteCronTrigger valueOf(String pat) {
        Objects.requireNonNull(pat);

        int delay;
        int maxCalls;

        Matcher matcher = CRON_REGEX.matcher(pat.trim());

        if (!matcher.matches())
            throw new IgniteException("Invalid schedule pattern: " + pat);

        String delayStr = matcher.group(2);

        try {
            delay = parseInt(delayStr, 0);
        }
        catch (NumberFormatException e) {
            throw new IgniteException("Invalid delay parameter in schedule pattern [delay=" +
                delayStr + ", pattern=" + pat + ']', e);
        }

        String numOfCallsStr = matcher.group(3);

        try {
            maxCalls = parseInt(numOfCallsStr, -1);
        }
        catch (NumberFormatException e) {
            throw new IgniteException("Invalid number of calls parameter in schedule pattern [numOfCalls=" +
                numOfCallsStr + ", pattern=" + pat + ']', e);
        }

        if (maxCalls <= 0)
            throw new IgniteException("Number of calls must be greater than 0 or must be equal to \"*\"" +
                " in schedule pattern [numOfCalls=" + maxCalls + ", pattern=" + pat + ']');

        String cron = matcher.group(4);

        try {
            A.notNullOrEmpty(cron, "cron");

            return new IgniteCronTrigger(delay, maxCalls, appendSecondIfOmitted(cron), pat);
        }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new IgniteException("Invalid cron expression in schedule pattern: " + cron, e);
        }
    }

    /**
     * @return Pattern.
     */
    public String pattern() {
        return pat;
    }

    /**
     * @return Delay.
     */
    public int delay() {
        return delay;
    }

    /**
     * @return maximum number of calls
     */
    public int maxCalls() {
        return maxCalls;
    }

    @Override public Date nextExecutionTime(TriggerContext triggerContext) {
        if (delay == 0 || triggerContext.lastActualExecutionTime() != null)
            return super.nextExecutionTime(triggerContext);


    }

    @Override public String toString() {
        return "IgniteCronTrigger{" +
            "pat='" + pat + '\'' +
            ", delay=" + delay +
            ", maxCalls=" + maxCalls +
            "} " + super.toString();
    }

    /** */
    private static int parseInt(String str, int dflt) {
        return (str == null || "*".equals(str)) ? dflt : Integer.parseInt(str);
    }

    /**
     * @param cron pattern
     * @return if seconds are omiited in the pattern adds "0" to satisfy {@link CronSequenceGenerator} requirements and
     * keep previous behaviour
     */
    private static String appendSecondIfOmitted(String cron) {
        String[] fields = StringUtils.tokenizeToStringArray(cron, " ");

        if (fields != null && fields.length == 5)
            return "0 " + cron;
        else
            return cron;
    }
}
