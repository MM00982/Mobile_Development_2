package ru.mirea.musin.retrofitapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Todo> todos;
    private ApiService apiService; // Нужен для отправки обновлений

    public TodoAdapter(Context context, List<Todo> todoList, ApiService apiService) {
        this.layoutInflater = LayoutInflater.from(context);
        this.todos = todoList;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.textViewTitle.setText(todo.getTitle());

        // Снимаем слушатель перед изменением состояния, чтобы не вызвать триггер при прокрутке
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(todo.getCompleted());

        // --- ЗАДАНИЕ PICASSO ---
        // Загружаем случайную картинку, зависящую от ID, чтобы они были разными
        String imageUrl = "https://picsum.photos/150/150?random=" + todo.getId();

        Picasso.get()
                .load(imageUrl)
                .resize(150, 150) // Трансформация размера
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_rotate) // Пока грузится
                .error(android.R.drawable.ic_delete) // Если ошибка
                .into(holder.imageView);

        // --- ЗАДАНИЕ RETROFIT (Обновление) ---
        holder.checkBoxCompleted.setOnClickListener(v -> {
            boolean isChecked = holder.checkBoxCompleted.isChecked();
            todo.setCompleted(isChecked); // Обновляем модель локально
            updateTodoOnServer(todo);     // Отправляем на сервер
        });
    }

    private void updateTodoOnServer(Todo todo) {
        // Создаем объект с обновленным полем (можно отправить и весь todo)
        Todo updatedTodo = new Todo(todo.getUserId(), todo.getId(), todo.getTitle(), todo.getCompleted());

        apiService.updateTodo(todo.getId(), updatedTodo).enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                if (response.isSuccessful()) {
                    // API возвращает обновленный объект (фиктивно, т.к. это jsonplaceholder)
                    Log.d("TodoAdapter", "Updated ID: " + response.body().getId() + " Completed: " + response.body().getCompleted());
                } else {
                    Log.e("TodoAdapter", "Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.e("TodoAdapter", "Failed to update: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        CheckBox checkBoxCompleted;
        ImageView imageView;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}