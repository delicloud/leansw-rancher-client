package com.thoughtworks.lean.rancher.dto.constants;

/**
 * Created by yongliuli on 10/10/16.
 */
public interface ContainerAction {
    String STOP = "stop";
    String START = "start";
    String UPDATE = "update";
    String RESTART = "restart";
    String EXCUTE = "excute";
    String LOGS = "logs";
    String PROXY = "proxy";
    String SET_LABELS = "setlabels";
    String MIGRATE = "migrate";
}
