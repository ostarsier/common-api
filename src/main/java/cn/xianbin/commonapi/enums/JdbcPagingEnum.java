package cn.xianbin.commonapi.enums;

public enum JdbcPagingEnum {
    MYSQL("mysql", " limit [$from],[$pageSize]", 1, 10),
    ORACLE("oracle ", " and rownum>[$from] and rownum<=[$from]+[$pageSize] ", 1, 10);
    private String type;
    private String paging;
    private int from;
    private int to;

    JdbcPagingEnum(String type, String paging, int from, int to) {
        this.type = type;
        this.paging = paging;
        this.from = from;
        this.to = to;
    }

    public static JdbcPagingEnum getInstance(String type) {
        for (JdbcPagingEnum o : values()) {
            if (type.equals(o.getType())) {
                return o;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaging() {
        return paging;
    }

    public void setPaging(String paging) {
        this.paging = paging;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
