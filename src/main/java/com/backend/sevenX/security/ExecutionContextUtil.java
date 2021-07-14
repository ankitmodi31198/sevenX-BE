package com.backend.sevenX.security;

import com.backend.sevenX.data.JwtUser;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class ExecutionContextUtil implements Serializable {

    private static final long                              serialVersionUID       = -6751490154133933000L;

    private static final String EXECUTION_CONTEXT_NAME = "executionContext";

    private JwtUser userDetails;

    private static final ThreadLocal<ExecutionContextUtil> EXECUTION_CONTEXT      = new InheritableThreadLocal<ExecutionContextUtil>() {

                                                                                      @Override
                                                                                      protected ExecutionContextUtil initialValue() {

                                                                                          return new ExecutionContextUtil();
                                                                                      }
                                                                                  };


    public ExecutionContextUtil() {

    }


    public ExecutionContextUtil init(HttpServletRequest httpRequest) {

        return this;
    }


    public ExecutionContextUtil init(JwtUser user) {

        setUserDetails(user);
        return this;
    }

    public JwtUser getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(JwtUser userDetails) {
        this.userDetails = userDetails;
    }

    public static synchronized ExecutionContextUtil getFromHttpSession(HttpServletRequest httpRequest) {

        ExecutionContextUtil executionContextUtil = (ExecutionContextUtil) httpRequest.getAttribute(EXECUTION_CONTEXT_NAME);
        EXECUTION_CONTEXT.set(executionContextUtil);
        return executionContextUtil;
    }


    public static synchronized ExecutionContextUtil getContext() {

        if (EXECUTION_CONTEXT.get() == null) {
            EXECUTION_CONTEXT.set(new ExecutionContextUtil());
        }
        return EXECUTION_CONTEXT.get();
    }


    public static void set(ExecutionContextUtil executionContextUtil) {

        EXECUTION_CONTEXT.set(executionContextUtil);
    }
}
