package com.example.sqlite.data.network

import com.example.sqlite.data.model.Employee
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface EmployeeApiService {

    @GET("employees.php")
    suspend fun getEmployeesByProfession(
        @Query("profesion") profession: String
    ): List<Employee>

    @GET("employees.php")
    suspend fun getAllEmployees(): List<Employee>

    companion object {
        private const val BASE_URL = "https://dev.codemcy.com/"

        val instance: EmployeeApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmployeeApiService::class.java)
        }
    }
}
