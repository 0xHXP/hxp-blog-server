package com.hxp.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxp.common.Result;
import com.hxp.entity.SysArticle;
import com.hxp.entity.SysUser;
import com.hxp.service.UserService;
import com.hxp.vo.article.ArticleListVo;
import com.hxp.vo.comment.CommentListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hxp
 * @date: 2025/1/11
 * @description:
 */
@RestController
@RequestMapping("/protal/user")
@RequiredArgsConstructor
@Api(tags = "门户-个人中心")
public class UserController {

    private final UserService userService;

    @PutMapping("/updateProfile")
    @ApiOperation(value = "修改我的资料")
    public Result<Void> updateProfile(@RequestBody SysUser user){
        userService.updateProfile(user);
        return Result.success();
    }

    @GetMapping("/comment")
    @ApiOperation(value = "获取我的评论")
    public Result<IPage<CommentListVo>> selectMyComment(){
        return Result.success(userService.selectMyComment());
    }

    @DeleteMapping("/delMyComment/{ids}")
    @ApiOperation(value = "删除我的评论")
    public Result<Void> delMyComment(@PathVariable List<Long> ids){
        return Result.success(userService.delMyComment(ids));
    }

    @GetMapping("/myReply")
    @Operation(description = "获取我的回复")
    public Result<IPage<CommentListVo>> getMyReply() {
        return Result.success(userService.getMyReply());
    }

    @GetMapping("/myLike")
    @ApiOperation(value = "获取我的点赞")
    public Result<IPage<ArticleListVo>> selectMyLike(){
        return Result.success(userService.selectMyLike());
    }

    @GetMapping("/myArticle")
    @ApiOperation(value = "获取我的文章")
    public Result<IPage<ArticleListVo>> selectMyArticle(SysArticle article){
        return Result.success(userService.selectMyArticle(article));
    }
}
