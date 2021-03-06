/*
 * Copyright 2011-2016 ZXC.com All right reserved. This software is the confidential and proprietary information of
 * ZXC.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ZXC.com.
 */
package com.ms.commons.test.mock.exception;

/**
 * @author zxc Apr 14, 2013 12:16:12 AM
 */
public class MockNeverCalledException extends RuntimeException {

    private static final long serialVersionUID = 5489321777815331245L;

    public MockNeverCalledException() {
        super();
    }

    public MockNeverCalledException(String message) {
        super(message);
    }

    public MockNeverCalledException(String message, Throwable cause) {
        super(message, cause);
    }

    public MockNeverCalledException(Throwable cause) {
        super(cause);
    }
}
