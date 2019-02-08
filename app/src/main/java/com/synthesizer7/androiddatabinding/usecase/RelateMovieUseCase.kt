package com.synthesizer7.androiddatabinding.usecase

import com.synthesizer7.androiddatabinding.api.repo.MovieRepo
import com.synthesizer7.androiddatabinding.model.MovieDetail
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

interface RelateMovieUseCase {
    fun execute(movieId: String): Observable<List<MovieDetail>>
}

class RelateMovieUseCaseImpl(private val movieRepo: MovieRepo) : RelateMovieUseCase {

    override fun execute(movieId: String) =
            Observable.timer(2000, TimeUnit.MILLISECONDS)
                    .flatMap {
                        movieRepo.getRelateMovie(id = movieId)
                    }
}