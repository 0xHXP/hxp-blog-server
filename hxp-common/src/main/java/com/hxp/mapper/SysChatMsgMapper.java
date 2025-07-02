package com.hxp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxp.entity.ChatMsg;
import com.hxp.vo.chat.ChatSendMsgVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysChatMsgMapper extends BaseMapper<ChatMsg> {

    IPage<ChatSendMsgVo> getChatMsgList(Page<Object> page);
}
