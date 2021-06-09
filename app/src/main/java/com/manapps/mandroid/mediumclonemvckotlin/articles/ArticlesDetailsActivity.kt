package com.manapps.mandroid.mediumclonemvckotlin.articles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.manapps.mandroid.mediumclonemvckotlin.R
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Constants
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Utils
import com.manapps.mandroid.mediumclonemvckotlin.databinding.ActivityArticlesDetailsBinding
import com.manapps.mandroid.mediumclonemvckotlin.databinding.ActivityMainBinding
import com.manapps.mandroid.mediumclonemvckotlin.databinding.FragmentGlobalPostsBinding
import com.manapps.mandroid.mediumclonemvckotlin.databinding.PrimaryPostsLayoutBinding
import com.manapps.mandroid.mediumclonemvckotlin.extensions.loadImage
import com.manapps.mandroid.mediumclonemvckotlin.models.entities.Article
import com.manapps.mandroid.mediumclonemvckotlin.models.response.ArticleResponse
import com.manapps.mandroid.mediumclonemvckotlin.models.response.ArticlesResponse
import com.manapps.mandroid.mediumclonemvckotlin.networking.APIClinet
import com.manapps.mandroid.mediumclonemvckotlin.networking.APIInterface
import com.manapps.mandroid.mediumclonemvckotlin.networking.NetworkHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticlesDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticlesDetailsBinding
    private lateinit var apiInterface: APIInterface
    private var slugId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        getIntentData()
        apiInterface = APIClinet.publicApi
        shuffle()
    }

    private fun initBindings() {
        binding = ActivityArticlesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getIntentData() {
        intent.extras.let {
            slugId = it?.getString(Constants.SLUGID)
        }
    }

    private fun shuffle() {
        if (NetworkHelper.isNetworkConnected(this)) {
            if (!slugId.isNullOrEmpty()) {
                sendGetFeedArticlesRequest(slugId!!)
            } else {
                Utils.showMessage(this, resources.getString(R.string.slugIdNotFoundMessage))
            }
        } else {
            NetworkHelper.noNetworkMessage(this)
        }
    }

    private fun sendGetFeedArticlesRequest(slugId: String) {
        setProgressDialogEnable()
        val getFeedArticlesCall: Call<ArticleResponse> = apiInterface.getArticleBySlug(slugId)
        getFeedArticlesCall.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                try {
                    setProgressDialogDisable()
                    if (response.isSuccessful) {
                        val getFeedArticlesModel = response.body()
                        getFeedArticlesModel?.let {
                            setUpFeedArticlesView(it.article)
                        }

                    } else {
                        //handle error message here acc to api response
                        Utils.showMessage(
                            this@ArticlesDetailsActivity,
                            resources.getString(R.string.generalError)
                        )
                    }
                } catch (e: Exception) {
                    Utils.showLogMessage(e.toString())
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                setProgressDialogDisable()
                Utils.showMessage(this@ArticlesDetailsActivity, t.message)
            }
        })
    }

    private fun setUpFeedArticlesView(articles: Article?) {
        articles?.let {
            binding.titleTextView.text = it.title
            binding.bodyTextView.text = it.body
            binding.authorTextView.text = it.author.username
            binding.dateTextView.text = it.createdAt
            binding.avatarImageView.loadImage(it.author.image, true)

        }
    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(binding.progressBar)
    }

    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(binding.progressBar)
    }

}