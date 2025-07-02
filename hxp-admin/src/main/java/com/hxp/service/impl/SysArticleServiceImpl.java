package com.hxp.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxp.common.Constants;
import com.hxp.common.ResultCode;
import com.hxp.dto.article.ArticleQueryDto;
import com.hxp.entity.SysArticle;
import com.hxp.entity.SysCategory;
import com.hxp.entity.SysTag;
import com.hxp.exception.ServiceException;
import com.hxp.mapper.SysArticleMapper;
import com.hxp.mapper.SysCategoryMapper;
import com.hxp.mapper.SysTagMapper;
import com.hxp.service.SysArticleService;
import com.hxp.utils.AiUtil;
import com.hxp.utils.PageUtil;
import com.hxp.vo.article.ArticleListVo;
import com.hxp.vo.article.SysArticleDetailVo;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysArticleServiceImpl extends ServiceImpl<SysArticleMapper, SysArticle> implements SysArticleService {

    private final SysTagMapper sysTagMapper;

    private final AiUtil aiUtil;
    private final SysCategoryMapper sysCategoryMapper;

    @Override
    public IPage<ArticleListVo> selectPage(ArticleQueryDto articleQueryDto) {
        return baseMapper.selectPageList(PageUtil.getPage(), articleQueryDto);
    }

    @Override
    public SysArticleDetailVo detail(Integer id) {
        SysArticle sysArticle = baseMapper.selectById(id);

        SysArticleDetailVo sysArticleDetailVo = new SysArticleDetailVo();
        BeanUtils.copyProperties(sysArticle, sysArticleDetailVo);

        List<String> category = sysCategoryMapper.getCategoryByArticleId(id);
        sysArticleDetailVo.setCategory(category);

        //获取标签
        List<String> tags = sysTagMapper.getTagNameByArticleId(id);
        sysArticleDetailVo.setTags(tags);
        return sysArticleDetailVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(SysArticleDetailVo sysArticle) {

        SysArticle obj = new SysArticle();
        BeanUtils.copyProperties(sysArticle, obj);
        obj.setUserId(StpUtil.getLoginIdAsLong());

        //添加分类
        addCategory(sysArticle, obj);
        baseMapper.insert(obj);

        addTags(sysArticle, obj);

        ThreadUtil.execAsync(() -> {
            String res = aiUtil.send(obj.getContent() + "请提供一段简短的介绍描述该文章的内容");
            if (StringUtils.isNotBlank(res)) {
                obj.setAiDescribe(res);
                baseMapper.updateById(obj);
            }
        });
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(SysArticleDetailVo sysArticle) {

        SysArticle obj = new SysArticle();
        BeanUtils.copyProperties(sysArticle, obj);

        //没有管理员权限就只能修改自己的文章
        if (!StpUtil.hasRole(Constants.ADMIN)) {
            SysArticle article = baseMapper.selectById(sysArticle.getId());
            if (article.getUserId() != StpUtil.getLoginIdAsLong()) {
                throw new ServiceException("只能修改自己的文章");
            }
        }

        sysCategoryMapper.deleteArticleCategoryByArticleIds(Collections.singletonList(obj.getId()));
        addCategory(sysArticle, obj);
        baseMapper.updateById(obj);

        //先删除标签在新增标签
        sysTagMapper.deleteArticleTagsByArticleIds(Collections.singletonList(obj.getId()));
        addTags(sysArticle, obj);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<Long> ids) {

        //没有管理员权限就只能删除自己的文章
        if (!StpUtil.hasRole(Constants.ADMIN)) {
            List<SysArticle> sysArticles = baseMapper.selectBatchIds(ids);
            for (SysArticle sysArticle : sysArticles) {
                if (sysArticle.getUserId() != StpUtil.getLoginIdAsLong()) {
                    throw new RuntimeException("只能删除自己的文章");
                }
            }
        }

        baseMapper.deleteBatchIds(ids);
        sysTagMapper.deleteArticleTagsByArticleIds(ids);
        return true;
    }


    @Override
    public void reptile(String url, String type) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements title = null;
            Elements tags = null;
            Elements content = null;
            if ("zhihu".equals(type)) {
                //知乎文章
                title = document.getElementsByClass("Post-Title");
                tags = document.getElementsByClass("Tag");
                content = document.getElementsByClass("Post-RichTextContainer");
            } else if ("csdn".equals(type)) {
                title = document.getElementsByClass("title-article");
                tags = document.getElementsByClass("tag-link");
                content = document.getElementsByClass("article_content");
            } else if ("juejin".equals(type)) {
                title = document.getElementsByClass("article-title");
                tags = document.getElementsByClass("tag-list");
                content = document.getElementsByClass("article-viewer");
            } else {
                throw new ServiceException("不支持的爬取类型");
            }
            if (StringUtils.isBlank(content.toString())) {
                throw new ServiceException(ResultCode.CRAWLING_ARTICLE_FAILED.getDesc());
            }
            grabArticle(title, tags, content, url);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void grabArticle(Elements title, Elements tags, Elements content, String url) {
        //爬取的是HTML内容，需要转成MD格式的内容
        String newContent = content.get(0).toString()
                .replaceAll("<code>", "<code class=\"lang-java\">")
                .replaceAll("\\s*href=\"#.*?\"", "")
                .replaceAll("<a\\s+id=\".*?\"></a>", "")
                .replaceAll("id=\".*?\"", "");
        String markdown = FlexmarkHtmlConverter.builder(new MutableDataSet()).build().convert(newContent)
                .replace("lang-java", "java")
                .replace("prism language-", "");

        SysArticle entity = SysArticle.builder().userId(StpUtil.getLoginIdAsLong()).contentMd(markdown)
                .isOriginal(Constants.NO).originalUrl(url)
                .title(title.get(0).text()).summary(title.get(0).text()).content(newContent).build();

        baseMapper.insert(entity);
        //为该文章添加标签
        List<Integer> tagIds = new ArrayList<>();
        List<Integer> cateIds = new ArrayList<>();
        tags.forEach(item -> {
            String rel = item.attr("rel");
            String tag = item.text();
            SysTag tagResult;
            SysCategory cateResult;
            if ("nofollow".equals(rel)) {
                tagResult = sysTagMapper.selectOne(new LambdaQueryWrapper<SysTag>().eq(SysTag::getName, tag));
                if (tagResult == null) {
                    tagResult = SysTag.builder().name(tag).build();
                    sysTagMapper.insert(tagResult);
                }
                tagIds.add(tagResult.getId());
            } else {
                cateResult = sysCategoryMapper.selectOne(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getName, tag));
                if (cateResult == null) {
                    cateResult = SysCategory.builder().name(tag).build();
                    sysCategoryMapper.insert(cateResult);
                }
                cateIds.add(cateResult.getId());
            }
        });
        if (tagIds.size() > 0) {
            sysTagMapper.addArticleTagRelations(entity.getId(), tagIds);
        }
        if (cateIds.size() > 0) {
            sysCategoryMapper.addArticleCategoryRelations(entity.getId(), cateIds);
        }
        System.out.println("文章抓取成功");

    }

    private void addCategory(SysArticleDetailVo sysArticle, SysArticle obj) {
        //添加标签
        List<Integer> categoryIds = new ArrayList<>();
        for (String category : sysArticle.getCategory()) {
            SysCategory sysCategory = sysCategoryMapper.selectOne(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getName, category));
            if (sysCategory == null) {
                sysCategory = SysCategory.builder().name(category).build();
                sysCategoryMapper.insert(sysCategory);
            }
            categoryIds.add(sysCategory.getId());
        }
        sysCategoryMapper.addArticleCategoryRelations(obj.getId(), categoryIds);

    }

    private void addTags(SysArticleDetailVo sysArticle, SysArticle obj) {
        //添加标签
        List<Integer> tagIds = new ArrayList<>();
        for (String tag : sysArticle.getTags()) {
            SysTag sysTag = sysTagMapper.selectOne(new LambdaQueryWrapper<SysTag>().eq(SysTag::getName, tag));
            if (sysTag == null) {
                sysTag = SysTag.builder().name(tag).build();
                sysTagMapper.insert(sysTag);
            }
            tagIds.add(sysTag.getId());
        }
        sysTagMapper.addArticleTagRelations(obj.getId(), tagIds);
    }
}
