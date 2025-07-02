package com.hxp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxp.mapper.SysMomentMapper;
import com.hxp.service.MomentService;
import com.hxp.utils.PageUtil;
import com.hxp.vo.moment.MomentPageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author: hxp
 * @date: 2025/2/5
 * @description:
 */
@Service
@RequiredArgsConstructor
public class MomentServiceImpl implements MomentService {

    private final SysMomentMapper baseMapper;

    @Override
    public IPage<MomentPageVo> getMomentList() {
        return baseMapper.selectPage(PageUtil.getPage());
    }
}
