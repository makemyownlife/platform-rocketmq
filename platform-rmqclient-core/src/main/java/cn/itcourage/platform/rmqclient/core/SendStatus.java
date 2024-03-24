package cn.itcourage.platform.rmqclient.core;

/**
 * 生成者发送状态<BR/>
 * 张严  2018/11/23 15:25
 */
public enum SendStatus {

    SEND_OK("1", "发送成功"),
    FLUSH_DISK_TIMEOUT("2", "磁盘刷新超时"),
    FLUSH_SLAVE_TIMEOUT("3", "从服务器同步超时"),
    SLAVE_NOT_AVAILABLE("4", "从服务器不可用"),
    UNKNOWN_STATUS("5", "未知状态");
    private String index;
    private String name;

    SendStatus(String index, String name) {
        this.index = index;
        this.name = name;
    }

    /**
     * 根据index获取对象
     *
     * @param index 索引
     */
    public static SendStatus getObjByIndex(final String index) {
        for (SendStatus sendStatus : SendStatus.values()) {
            if (sendStatus.index.equals(index)) {
                return sendStatus;
            }
        }
        return null;
    }

    /**
     * 根据index获取name
     *
     * @param index 索引
     */
    public static String getNameByIndex(final String index) {
        for (SendStatus sendStatus : SendStatus.values()) {
            if (sendStatus.index.equals(index)) {
                return sendStatus.name;
            }
        }
        return null;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserTypeEnum{" +
                "index='" + index + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
