package com.example.client.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.client.model.Todo;

@Service
public class TodoServiceImpl implements TodoService {

  @Autowired
  OAuth2RestTemplate restTemplate;

  @Value("${api-url:http://localhost:18082}/api/v1/todos")
  String resourcesUrl;

  @Override
  public Todo findOne(String todoId) {

    ResponseEntity<Todo> entity =
        restTemplate.getForEntity(resourcesUrl + "/todoId=", Todo.class, todoId);
    Todo todo = entity.getBody();

    if (todo == null) {
      StringBuilder sb = new StringBuilder();
      sb.append("ERROR ");
      sb.append("[E404] The requested Todo is not found. (id=" + todoId + ")");
      throw new IllegalStateException(sb.toString());
    }
    return todo;
  }

  @Override
  public Collection<Todo> findAll() {

    ResponseEntity<List<Todo>> entity = restTemplate.exchange(
        RequestEntity.get(UriComponentsBuilder.fromHttpUrl(resourcesUrl).build().toUri()).build(),
        new ParameterizedTypeReference<List<Todo>>() {});

    return entity.getBody();
  }

  @Override
  public Todo create(Todo todo) {

    todo.setTodoId(UUID.randomUUID().toString());
    todo.setCreatedAt(new Date());
    todo.setFinished(false);

    ResponseEntity<Todo> entity = restTemplate.exchange(
        RequestEntity.post(UriComponentsBuilder.fromHttpUrl(resourcesUrl).build().toUri()).body(todo),
        Todo.class);
    Todo createdTodo = entity.getBody();
    return createdTodo;
  }

  @Override
  public Todo finish(String todoId) {

    // TODO

    Todo todo = findOne(todoId);
    if (todo.isFinished()) {
      StringBuilder sb = new StringBuilder();
      sb.append("ERROR ");
      sb.append("[E002] The requested Todo is already finished. (id=" + todoId + ")");
      throw new UnsupportedOperationException(sb.toString());
    }
    todo.setFinished(true);

    
    return todo;
  }

  @Override
  public void delete(String todoId) {

    // TODO

    Todo todo = findOne(todoId);
    
    
  }


}
