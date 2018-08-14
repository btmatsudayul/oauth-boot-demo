package com.example.resource.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.resource.model.Todo;
import com.example.resource.service.TodoService;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoRestController {

  @Autowired
  TodoService todoService;

//  @Autowired
//  TokenStore tokenStore;
  
  @Autowired
  Mapper beanMapper;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TodoResource> getTodos(Principal principal) {

//    if (principal instanceof OAuth2Authentication) {
//      OAuth2AuthenticationDetails details =
//          (OAuth2AuthenticationDetails) ((OAuth2Authentication) principal).getDetails();
//      OAuth2AccessToken token = tokenStore.readAccessToken(details.getTokenValue());
//      Map<String, Object> additionalInfo = token.getAdditionalInformation();
//      // 独自パラメータを取得
//      additionalInfo.get("customKey");
//    }

    Collection<Todo> todos = todoService.findAll();
    List<TodoResource> todoResources = new ArrayList<>();
    for (Todo todo : todos) {
      todoResources.add(beanMapper.map(todo, TodoResource.class));
    }
    return todoResources;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TodoResource postTodos(@RequestBody TodoResource todoResource) {
    Todo createdTodo = todoService.create(beanMapper.map(todoResource, Todo.class));
    TodoResource createdTodoResponse = beanMapper.map(createdTodo, TodoResource.class);
    return createdTodoResponse;
  }

  @GetMapping(value = "{todoId}")
  @ResponseStatus(HttpStatus.OK)
  public TodoResource getTodo(@PathVariable("todoId") String todoId) {
    Todo todo = todoService.findOne(todoId);
    TodoResource todoResource = beanMapper.map(todo, TodoResource.class);
    return todoResource;
  }

  @PutMapping(value = "{todoId}")
  @ResponseStatus(HttpStatus.OK)
  public TodoResource putTodo(@PathVariable("todoId") String todoId) {
    Todo finishedTodo = todoService.finish(todoId);
    TodoResource finishedTodoResource = beanMapper.map(finishedTodo, TodoResource.class);
    return finishedTodoResource;
  }

  @DeleteMapping(value = "{todoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTodo(@PathVariable("todoId") String todoId) {
    todoService.delete(todoId);
  }
}
