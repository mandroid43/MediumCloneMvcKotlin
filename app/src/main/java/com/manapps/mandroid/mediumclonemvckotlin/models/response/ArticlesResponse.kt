package com.manapps.mandroid.mediumclonemvckotlin.models.response

import com.manapps.mandroid.mediumclonemvckotlin.models.entities.Article

data class ArticlesResponse(
    val articles: List<Article>,
    val articlesCount: Int
)