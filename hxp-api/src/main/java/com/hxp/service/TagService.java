package com.hxp.service;

import com.hxp.vo.tag.TagListVo;

import java.util.List;

public interface TagService {

    /**
     * 获取标签列表
     * @return
     */
    List<TagListVo> getTagsApi();

}
