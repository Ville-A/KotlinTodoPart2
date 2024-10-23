package com.example.todo.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.model.Todo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import kotlinx.coroutines.launch

class TodoViewModel: ViewModel() {
    var todos = mutableStateListOf<Todo>()
        private set
    init {
        getTodosList()
    }

    private fun getTodosList() {
        viewModelScope.launch {
            var todosApi: TodosApi? = null
            try {
                todosApi = TodosApi.getInstance()
                todos.clear()
                todos.addAll(todosApi.getTodos())
            } catch (e: Exception) {
                Log.d("TODOVIEWMODEL",e.message.toString())
            }
        }
    }

    interface TodosApi {
        @GET("todos")
        suspend fun getTodos(): List<Todo>
        companion object {
            var BASE_URL = "https://jsonplaceholder.typicode.com"
            var todosService: TodosApi? = null
            fun getInstance(): TodosApi {
                if (todosService === null) {
                    todosService = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(TodosApi::class.java)
                }
                return todosService!!
            }
        }
    }
}