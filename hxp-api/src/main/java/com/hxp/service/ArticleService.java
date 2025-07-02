package com.hxp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxp.entity.SysCategory;
import com.hxp.vo.article.ArchiveListVo;
import com.hxp.vo.article.ArticleDetailVo;
import com.hxp.vo.article.ArticleListVo;
import com.hxp.vo.article.CategoryListVo;

import java.util.List;

public interface ArticleService {
    /**
     * 获取文章列表
     * @param tagId
     * @param categoryId
     * @return
     */
    IPage<ArticleListVo> getArticleList(Integer tagId, Integer categoryId,String keyword);

    /**
     * 获取文章详情
     * @param id 文章id
     * @return
     */
    ArticleDetailVo getArticleDetail(Long id);


    /**
     * 获取文章归档
     * @return
     */
    List<ArchiveListVo> getArticleArchive();

    /**
     * 获取分类
     * @return
     */
    List<CategoryListVo> getArticleCategories();

    /**
     * 获取轮播文章
     * @return
     */
    List<ArticleListVo> getCarouselArticle();

    /**
     * 获取推荐文章
     * @return
     */
    List<ArticleListVo> getRecommendArticle();

    /**
     * 点赞文章
     * @param id
     * @return
     */
    Boolean like(Long id);

    List<SysCategory> getCategoryAll();



}
