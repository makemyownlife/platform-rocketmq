package cn.javayong.platform.rmqclient.core.exception;

/**
 * 自定义异常类<BR/>
 * 张严  2018/9/26 16:52
 */
public class MQClientException extends RuntimeException {

    private static final long serialVersionUID = 6855356574640041094L;

    public MQClientException() {
    }

    public MQClientException(String message) {
        super(message);
    }

    public MQClientException(Throwable cause) {
        super(cause);
    }

    public MQClientException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
