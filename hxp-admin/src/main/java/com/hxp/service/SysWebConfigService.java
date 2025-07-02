package com.hxp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxp.entity.SysWebConfig;

public interface SysWebConfigService extends IService<SysWebConfig> {

    void update(SysWebConfig sysWebConfig);
}
