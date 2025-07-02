package com.hxp.dto.feedback;

import com.hxp.entity.SysFeedback;
import lombok.Data;

/**
 * @author: hxp
 * @date: 2025/1/12
 * @description:
 */
@Data
public class SysFeedbackQueryDto extends SysFeedback {

    private String source;
}
