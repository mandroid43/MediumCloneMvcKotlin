package com.manapps.mandroid.mediumclonemvckotlin.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.manapps.mandroid.mediumclonemvckotlin.R
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Utils
import com.manapps.mandroid.mediumclonemvckotlin.articles.CreateArticleActivity
import com.manapps.mandroid.mediumclonemvckotlin.databinding.FragmentGlobalPostsBinding
import com.manapps.mandroid.mediumclonemvckotlin.databinding.PrimaryPostsLayoutBinding
import com.manapps.mandroid.mediumclonemvckotlin.models.entities.Article
import com.manapps.mandroid.mediumclonemvckotlin.models.response.ArticlesResponse
import com.manapps.mandroid.mediumclonemvckotlin.networking.APIClinet
import com.manapps.mandroid.mediumclonemvckotlin.networking.APIInterface
import com.manapps.mandroid.mediumclonemvckotlin.networking.NetworkHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalStateException

class GlobalPostsFragment : Fragment() {
    private lateinit var binding: FragmentGlobalPostsBinding
    private lateinit var postsViewBinding: PrimaryPostsLayoutBinding
    private lateinit var apiInterface: APIInterface
    private lateinit var articlesAdapter: ArticlesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGlobalPostsBinding.inflate(inflater, container, false)
        apiInterface = APIClinet.publicApi
        initBindings()
        setUpFeedArticlesRecyclerView()
        shuffle()
        postsViewBinding.fab.setOnClickListener({ goToCreateArticlesActivity() })
        return binding.root
    }


    private fun initBindings() {
        try {
            postsViewBinding = binding.postsView
        } catch (exception: IllegalStateException) {
        } catch (exception: Exception) {
        }
    }

    private fun setUpFeedArticlesRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        postsViewBinding.recyclerView.layoutManager = linearLayoutManager
    }

    private fun shuffle() {
        if (NetworkHelper.isNetworkConnected(context)) {
            sendGetFeedArticlesRequest()
        } else {
            NetworkHelper.noNetworkMessage(context)
        }
    }

    private fun sendGetFeedArticlesRequest() {
        setProgressDialogEnable()
        val getFeedArticlesCall: Call<ArticlesResponse> = apiInterface.getAuthorsArticles()
        getFeedArticlesCall.enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                try {
                    setProgressDialogDisable()
                    if (response.isSuccessful) {
                        val getFeedArticlesModel = response.body()
                        getFeedArticlesModel?.let {
                            setUpFeedArticlesView(it.articles)
                        }

                    } else {
                        //handle error message here acc to api response
                        setUpFeedArticlesView(null)

                    }
                } catch (e: Exception) {
                    Utils.showLogMessage(e.toString())
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                setProgressDialogDisable()
                setUpFeedArticlesView(null)
                Utils.showMessage(context, t.message)
            }
        })
    }


    fun setUpFeedArticlesView(articlesList: List<Article>?) {
        if (articlesList.isNullOrEmpty()) {
            setNoFeedArticlesLayout()
        } else {
            Utils.setVisibilityGone(postsViewBinding.subHeadingTv)
            Utils.setVisibilityVisible(postsViewBinding.recyclerView)
            setUpFeedArticlesRecyclerView(articlesList)
        }
    }

    private fun setNoFeedArticlesLayout() {
        Utils.setVisibilityGone(postsViewBinding.recyclerView)
        Utils.setVisibilityVisible(postsViewBinding.subHeadingTv)
    }

    private fun setUpFeedArticlesRecyclerView(articlesList: List<Article>) {
        articlesAdapter = ArticlesAdapter(context, articlesList)
        postsViewBinding.recyclerView.adapter = articlesAdapter
        articlesAdapter.notifyDataSetChanged()
    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(postsViewBinding.progressBar)
    }

    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(postsViewBinding.progressBar)
    }

    private fun goToCreateArticlesActivity() {
        //Utils.moveTo(context, CreateArticleActivity::class.java)
        Utils.showMessage(context,resources.getString(R.string.featureComingSoonLabel))
    }

    override fun onResume() {
        super.onResume()
        shuffle()
    }
}