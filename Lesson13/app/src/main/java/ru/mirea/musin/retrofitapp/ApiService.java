package ru.mirea.musin.retrofitapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH; // Используем PATCH для частичного обновления
import retrofit2.http.Path;

public interface ApiService {
    @GET("todos")
    Call<List<Todo>> getTodos();

    // Запрос на обновление состояния
    @PATCH("todos/{id}")
    Call<Todo> updateTodo(@Path("id") int id, @Body Todo todo);
}