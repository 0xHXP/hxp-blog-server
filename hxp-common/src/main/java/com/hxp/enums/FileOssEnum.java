package com.hxp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author: hxp
 * @date: 2025/2/21
 * @description:
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum FileOssEnum {
    QINIU("qiniu"),

    ALI("ali"),

    TENCENT("tencent"),

    MINIO("minio"),

    LOCAL("local");

    private String value;

}
