package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticle> {

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.createdAt, root.createdBy);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // 자동 부분 검색
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 자동 부분 검색
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // 자동 부분 검색
    }
}
