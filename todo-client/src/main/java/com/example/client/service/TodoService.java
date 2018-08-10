package com.example.client.service;

import java.util.Collection;
import com.example.client.model.Todo;

public interface TodoService {

  Collection<Todo> findAll();

  Todo findOne(String todoId);

  Todo create(Todo todo);

  Todo finish(String todoId);

  void delete(String todoId);
}
