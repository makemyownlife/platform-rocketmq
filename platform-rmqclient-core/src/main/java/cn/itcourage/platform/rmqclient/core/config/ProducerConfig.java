package cn.itcourage.platform.rmqclient.core.config;

/**
 * 生产者配置类<BR/>
 */
public class ProducerConfig {
    //NameSrv地址
    private String nameSrvAddress;

    //同步重试次数
    private int retryTimesWhenSendFailed = 3;

    //异步重试次数
    private int retryTimesWhenSendAsyncFailed = 3;

    //超时时间
    private int sendMsgTimeout = 3000;

    public String getNameSrvAddress() {
        return nameSrvAddress;
    }

    public void setNameSrvAddress(String nameSrvAddress) {
        this.nameSrvAddress = nameSrvAddress;
    }

    public int getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    public int getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(int sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public int getRetryTimesWhenSendAsyncFailed() {
        return retryTimesWhenSendAsyncFailed;
    }

    public void setRetryTimesWhenSendAsyncFailed(int retryTimesWhenSendAsyncFailed) {
        this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
    }

    @Override
    public String toString() {
        return "ProducerConfig{" +
                "nameSrvAddress='" + nameSrvAddress + '\'' +
                ", retryTimesWhenSendFailed=" + retryTimesWhenSendFailed +
                ", retryTimesWhenSendAsyncFailed=" + retryTimesWhenSendAsyncFailed +
                ", sendMsgTimeout=" + sendMsgTimeout +
                '}';
    }
}
