package com.thoughtworks.lean.rancher.dto.request;

/**
 * Created by yongliuli on 10/9/16.
 */
public class InstanceStopAction {
    private boolean remove;
    private int timeout;

    public boolean isRemove() {
        return remove;
    }

    public InstanceStopAction setRemove(boolean remove) {
        this.remove = remove;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public InstanceStopAction setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}
