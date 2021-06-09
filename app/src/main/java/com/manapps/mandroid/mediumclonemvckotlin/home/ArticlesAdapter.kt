package com.manapps.mandroid.mediumclonemvckotlin.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Constants
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Utils
import com.manapps.mandroid.mediumclonemvckotlin.articles.ArticlesDetailsActivity
import com.manapps.mandroid.mediumclonemvckotlin.databinding.ListItemArticleBinding
import com.manapps.mandroid.mediumclonemvckotlin.extensions.loadImage
import com.manapps.mandroid.mediumclonemvckotlin.models.entities.Article

class ArticlesAdapter(
    val context: Context?,
    val articlesList: List<Article>
) :
    RecyclerView.Adapter<ArticlesAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ListItemArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = articlesList.size
    class ItemViewHolder(var holderBinding: ListItemArticleBinding) :
        RecyclerView.ViewHolder(holderBinding.root) {
        fun bind(article: Article) {
           holderBinding.apply {
               authorTextView.text = article.author.username
               titleTextView.text = article.title
               bodySnippetTextView.text = article.body
               dateTextView.text = article.createdAt
               avatarImageView.loadImage(article.author.image, true)

           }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(articlesList[position])

        holder.itemView.setOnClickListener {
            Utils.moveToWithData(context,ArticlesDetailsActivity::class.java,Constants.SLUGID,articlesList[position].slug)
            //onItemClick(position)
        }
    }

    private fun onItemClick(position: Int) {

    }


}