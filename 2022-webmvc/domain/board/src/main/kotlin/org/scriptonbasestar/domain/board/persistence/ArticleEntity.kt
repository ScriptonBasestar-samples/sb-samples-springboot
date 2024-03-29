package org.scriptonbasestar.domain.board.persistence

import org.hibernate.annotations.DynamicUpdate
import org.scriptonbasestar.base.BaseSeqUuidEntity
import javax.persistence.*

@Entity
@Table(name = "T_ARTICLE", indexes = [Index(name = "IDX__T_ARTICLE__TITLE", columnList = "title", unique = false)])
@DynamicUpdate
class ArticleEntity(
    @field:Column(
        length = 100,
        nullable = false
    )
    var title: String,
    @field:Column(
        length = 1000,
        nullable = false,
        columnDefinition = "mediumtext"
    )
    var content: String,
    var enabled: Boolean = true,
) : BaseSeqUuidEntity() {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val comments: MutableList<CommentEntity> = mutableListOf()

    override fun toString(): String {
        return super.buildStringHelper()
            .add("title", title)
            .add("content", content)
            .toString()
    }
}
