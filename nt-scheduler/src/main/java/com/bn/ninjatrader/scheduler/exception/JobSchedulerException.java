package com.bn.ninjatrader.scheduler.exception;

/**
 * @author bradwee2000@gmail.com
 */
public class JobSchedulerException extends RuntimeException {

    public JobSchedulerException(final Throwable throwable) {
        super(throwable);
    }
}
