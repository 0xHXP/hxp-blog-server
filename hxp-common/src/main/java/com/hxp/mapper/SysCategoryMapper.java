package com.hxp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxp.vo.article.CategoryListVo;
import com.hxp.entity.SysCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 分类 Mapper接口
 */
@Mapper
public interface SysCategoryMapper extends BaseMapper<SysCategory> {
    List<CategoryListVo> getArticleCategories();

    void addArticleCategoryRelations(@Param("articleId") Long articleId, @Param("categoryIds") List<Integer> categoryIds);

    List<String> getCategoryByArticleId(Integer id);

    void deleteArticleCategoryByArticleIds(List<Long> longs);
}
