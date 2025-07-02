package com.hxp.vo.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分类列表视图对象")
public class CategoryListVo {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

//    @ApiModelProperty(value = "文章数量")
//    private Integer articleNum;
}
