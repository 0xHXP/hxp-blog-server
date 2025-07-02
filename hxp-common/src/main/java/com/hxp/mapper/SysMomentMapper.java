package com.hxp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxp.entity.SysMoment;
import com.hxp.vo.moment.MomentPageVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: hxp
 * @date: 2025/2/5
 * @description:
 */
@Mapper
public interface SysMomentMapper extends BaseMapper<SysMoment> {


    IPage<MomentPageVo> selectPage(IPage<SysMoment> page);
}
