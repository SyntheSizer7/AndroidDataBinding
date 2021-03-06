package com.synthesizer7.androiddatabinding.view.movie.relate

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.synthesizer7.androiddatabinding.R
import com.synthesizer7.androiddatabinding.api.repo.MovieRepoImpl
import com.synthesizer7.androiddatabinding.extenstions.getViewModel
import com.synthesizer7.androiddatabinding.usecase.RelateMovieUseCaseImpl
import com.synthesizer7.androiddatabinding.view.movie.adapter.RelateMovieAdapter
import kotlinx.android.synthetic.main.fragment_recent_movie.*

class RelateMovieFragment : Fragment() {

    companion object {
        const val TAG = "RelateMovieFragment"
        private const val MOVIE_ID = "movie_id"
        fun newInstance(movieId: String): RelateMovieFragment {
            return RelateMovieFragment().apply {
                arguments = Bundle().apply {
                    putString(MOVIE_ID, movieId)
                }
            }
        }
    }

    private val relateMovieViewModel: RelateMovieViewModel by lazy {
        getViewModel {
            val movieRepo = MovieRepoImpl()
            RelateMovieViewModel(
                    relateMovieUseCase = RelateMovieUseCaseImpl(movieRepo = movieRepo)
            )
        }
    }

    private val relateMovieAdapter: RelateMovieAdapter by lazy {
        RelateMovieAdapter()
    }

    private var movieId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieId = arguments?.getString(RelateMovieFragment.MOVIE_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingViewModel()
        return inflater.inflate(R.layout.fragment_recent_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        if (movieId.isNotEmpty()) {
            relateMovieViewModel.getRelateMovieList(id = movieId)
        }
    }

    private fun bindingViewModel() {
        relateMovieViewModel.getRelateMovieList().observe(this, Observer { movies ->
            movies?.let {
                relateMovieAdapter.setData(list = it)
                relateMovieAdapter.notifyDataSetChanged()
            }
        })

        relateMovieViewModel.isLoading().observe(this, Observer { loading ->
            if (loading == true) {
                progressView.visibility = View.VISIBLE
                relateMovieRecycleView.visibility = View.GONE
            } else {
                progressView.visibility = View.GONE
                relateMovieRecycleView.visibility = View.VISIBLE
            }
        })

    }

    private fun initView() {
        relateMovieRecycleView.apply {
            layoutManager = GridLayoutManager(context, 3)
            this.adapter = relateMovieAdapter
        }
    }
}