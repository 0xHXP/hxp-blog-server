package com.hxp.service.impl;

import com.hxp.service.TagService;
import com.hxp.vo.tag.TagListVo;
import com.hxp.mapper.SysTagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final SysTagMapper sysTagMapper;

    @Override
    public List<TagListVo> getTagsApi() {
        return sysTagMapper.getTagsApi();
    }
}
