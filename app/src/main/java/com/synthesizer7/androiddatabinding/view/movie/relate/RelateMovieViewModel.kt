package com.synthesizer7.androiddatabinding.view.movie.relate

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.synthesizer7.androiddatabinding.extenstions.addTo
import com.synthesizer7.androiddatabinding.model.MovieDetail
import com.synthesizer7.androiddatabinding.usecase.RelateMovieUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RelateMovieViewModel(private val relateMovieUseCase: RelateMovieUseCase) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val relateMovieList = MutableLiveData<List<MovieDetail>>()
    private val isLoading = MutableLiveData<Boolean>()
    private var currentMovieId = ""

    fun getRelateMovieList(): LiveData<List<MovieDetail>> = relateMovieList
    fun isLoading(): LiveData<Boolean> = isLoading
    fun getCurrentMovieId() = currentMovieId

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getRelateMovieList(id: String) {
        currentMovieId = id
        relateMovieUseCase.execute(movieId = id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    isLoading.value = true
                }
                .subscribe({ response ->
                    response?.let {
                        isLoading.value = false
                        relateMovieList.value = response
                    }
                }, { throwable ->
                    isLoading.value = false
                }).addTo(composite = compositeDisposable)
    }
}