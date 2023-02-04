package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
public class Article extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * - Setter: 개발자가 임의로 셋팅해서는 안되는 값에는 Setter 를 하지 않는다.
     * - Column: 별도의 옵션이 추가되지 않으면 생략 되어도 된다.
     */
    @Setter
    @Column(nullable = false)
    private String title; // 제목
    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    @Setter
    private String hashtag; // 해시태그

    /**
     * - 양방향 바인딩
     */
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;

        /**
         * - 영속화 되지 않았다면(= id가 부여되지 않았다면) 동등성 검사 자체가 의미가 없으므로
         * - 서로 다른 객체로 보거나 처리하지 않겠다는 의미
         */
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
