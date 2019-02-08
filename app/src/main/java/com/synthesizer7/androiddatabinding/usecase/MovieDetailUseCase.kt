package com.synthesizer7.androiddatabinding.usecase

import com.synthesizer7.androiddatabinding.api.repo.MovieRepo
import com.synthesizer7.androiddatabinding.model.MovieDetail
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

interface MovieDetailUseCase {
    fun execute(movieId: String): Observable<MovieDetail>
}

class MovieDetailUseCaseImpl(private val movieRepo: MovieRepo) : MovieDetailUseCase {

    override fun execute(movieId: String) =
            Observable.timer(1000, TimeUnit.MILLISECONDS)
                    .flatMap {
                        movieRepo.getMovieDetail(id = movieId)
                    }

}