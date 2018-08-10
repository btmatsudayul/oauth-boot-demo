package com.example.resource.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.resource.model.Todo;
import com.example.resource.service.TodoService;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoRestController {

  @Autowired
  TodoService todoService;

  @Autowired
  Mapper beanMapper;

  @RequestMapping(method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public List<TodoResource> getTodos() {
    Collection<Todo> todos = todoService.findAll();
    List<TodoResource> todoResources = new ArrayList<>();
    for (Todo todo : todos) {
      todoResources.add(beanMapper.map(todo, TodoResource.class));
    }
    return todoResources;
  }

  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public TodoResource postTodos(@RequestBody TodoResource todoResource) {
    Todo createdTodo = todoService.create(beanMapper.map(todoResource, Todo.class));
    TodoResource createdTodoResponse = beanMapper.map(createdTodo, TodoResource.class);
    return createdTodoResponse;
  }

  @RequestMapping(value = "{todoId}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public TodoResource getTodo(@PathVariable("todoId") String todoId) {
    Todo todo = todoService.findOne(todoId);
    TodoResource todoResource = beanMapper.map(todo, TodoResource.class);
    return todoResource;
  }

  @RequestMapping(value = "{todoId}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public TodoResource putTodo(@PathVariable("todoId") String todoId) {
    Todo finishedTodo = todoService.finish(todoId);
    TodoResource finishedTodoResource = beanMapper.map(finishedTodo, TodoResource.class);
    return finishedTodoResource;
  }

  @RequestMapping(value = "{todoId}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTodo(@PathVariable("todoId") String todoId) {
    todoService.delete(todoId);
  }
}
