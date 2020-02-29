package com.jbrunton.mymovies

import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.FlowDataStream
import com.jbrunton.mymovies.entities.repositories.GenresRepository

class TestGenresRepository : GenresRepository {
    override suspend fun genres(): FlowDataStream<List<Genre>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}