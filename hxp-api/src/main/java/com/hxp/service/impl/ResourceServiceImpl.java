package com.hxp.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxp.common.RedisConstants;
import com.hxp.entity.SysResource;
import com.hxp.enums.ResourceStatusEnum;
import com.hxp.exception.ServiceException;
import com.hxp.mapper.SysResourceMapper;
import com.hxp.service.ResourceService;
import com.hxp.utils.PageUtil;
import com.hxp.utils.RedisUtil;
import com.hxp.vo.resource.SysResourceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author: hxp
 * @date: 2025/3/12
 * @description:
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final SysResourceMapper baseMapper;

    private final RedisUtil redisUtil;

    @Override
    public Page<SysResourceVo> getResourceList(SysResource sysResource) {
        return baseMapper.getResourceList(PageUtil.getPage(),sysResource);
    }

    @Override
    public void add(SysResource sysResource) {
        sysResource.setUserId(StpUtil.getLoginIdAsLong());
        sysResource.setStatus(ResourceStatusEnum.AUDIT.getCode());
        baseMapper.insert(sysResource);
    }

    @Override
    public SysResource verify(String code,Long id) {
        String key = RedisConstants.CAPTCHA_CODE_KEY + code;
        if (!redisUtil.hasKey(key)) {
            throw new ServiceException("验证码错误");
        }
        redisUtil.delete(key);

        SysResource sysResource = baseMapper.selectById(id);

        sysResource.setDownloads(sysResource.getDownloads() + 1);
        baseMapper.updateById(sysResource);

        return sysResource;
    }
}
