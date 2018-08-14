package com.example.resource.service;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.resource.mapper.TodoMapper;
import com.example.resource.model.Todo;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

  private static final long MAX_UNFINISHED_COUNT = 5;

  @Autowired
  OAuth2RestTemplate restTemplate;

  @Value("${api-url:http://localhost:18083}/api/v1/message")
  String resourcesUrl;

  @Autowired
  TodoMapper todoMapper;

  @Override
  @Transactional(readOnly = true)
  public Todo findOne(String todoId) {
    Todo todo = todoMapper.findOne(todoId);
    if (todo == null) {
      StringBuilder sb = new StringBuilder();
      sb.append("ERROR ");
      sb.append(
          "[E404] The requested Todo is not found. (id=" + todoId + ")");
      throw new IllegalStateException(sb.toString());
    }
    return todo;
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<Todo> findAll() {

    // アクセストークンを利用して他サービスを呼び出し
//    String message = restTemplate.getForObject(resourcesUrl, String.class);
    Collection<Todo> todos = todoMapper.findAll();
//    todos.stream().forEach(t -> t.setMessage(message));
    return todos;
  }

  @Override
  public Todo create(Todo todo) {
    long unfinishedCount = todoMapper.countByFinished(false);
    if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
      StringBuilder sb = new StringBuilder();
      sb.append("ERROR ");
      sb.append(
          "[E001] The count of un-finished Todo must not be over " + MAX_UNFINISHED_COUNT + ".");
      throw new UnsupportedOperationException(sb.toString());
    }

    String todoId = UUID.randomUUID().toString();
    Date createdAt = new Date();

    todo.setTodoId(todoId);
    todo.setCreatedAt(createdAt);
    todo.setFinished(false);

    todoMapper.create(todo);

    return todo;
  }

  @Override
  public Todo finish(String todoId) {
    Todo todo = findOne(todoId);
    if (todo.isFinished()) {
      StringBuilder sb = new StringBuilder();
      sb.append("ERROR ");
      sb.append(
          "[E002] The requested Todo is already finished. (id=" + todoId + ")");
      throw new UnsupportedOperationException(sb.toString());
    }
    todo.setFinished(true);
    todoMapper.update(todo);
    return todo;
  }

  @Override
  public void delete(String todoId) {
    Todo todo = findOne(todoId);
    todoMapper.delete(todo);
  }


}
