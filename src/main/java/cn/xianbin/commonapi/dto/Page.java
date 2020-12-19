package cn.xianbin.commonapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@ApiModel
public class Page<T> {
    @ApiModelProperty(value = "页码")
    @Getter
    @Setter
    private int currentPage;
    @ApiModelProperty(value = "分页大小")
    @Getter
    @Setter
    private Integer pageSize;
    @ApiModelProperty(value = "总数")
    private Integer total;
    @ApiModelProperty(value = "页数")
    @Getter
    @Setter
    private Integer pages;
    @ApiModelProperty(value = "数据集")
    @Getter
    @Setter
    private List<Map<String, Object>> records;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
        this.pages = (total + pageSize - 1) / pageSize;
    }
}
