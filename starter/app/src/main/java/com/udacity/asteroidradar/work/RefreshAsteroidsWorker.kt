package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.getFormattedDate
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshAsteroidsWorker (appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext,params){

    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val dataBase = getDatabase(applicationContext)
        val repository = AsteroidsRepository(dataBase)
        return try {
            repository.refreshAsteroids(
                getFormattedDate(0),
                getFormattedDate(Constants.DEFAULT_END_DATE_DAYS)
            )
            Result.success()
        } catch (exception: HttpException) {
            return Result.retry()
        }
    }

}