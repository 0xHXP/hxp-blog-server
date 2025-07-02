package com.hxp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxp.vo.moment.MomentPageVo;

/**
 * @author: hxp
 * @date: 2025/2/5
 * @description:
 */
public interface MomentService {
    IPage<MomentPageVo> getMomentList();

}
