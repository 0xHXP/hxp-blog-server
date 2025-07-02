package com.hxp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxp.entity.SysResource;
import com.hxp.vo.resource.SysResourceVo;

/**
 * @author: hxp
 * @date: 2025/3/12
 * @description:
 */
public interface ResourceService {

    Page<SysResourceVo> getResourceList(SysResource sysResource);

    void add(SysResource sysResource);

    SysResource verify(String code,Long id);
}
