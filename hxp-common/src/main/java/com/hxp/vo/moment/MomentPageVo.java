package com.hxp.vo.moment;

import com.hxp.entity.SysMoment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: hxp
 * @date: 2025/2/5
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MomentPageVo extends SysMoment {

    private String nickname;

    private String avatar;

}
