package com.example.client.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.client.model.Todo;
import com.example.client.service.TodoService;

@Controller
public class TodoController {

  @Autowired
  TodoService todoService;

  @Autowired
  Mapper beanMapper;

  @ModelAttribute // (2)
  public TodoForm setUpForm() {
      TodoForm form = new TodoForm();
      return form;
  }
  
  @RequestMapping(value = "/")
  public String index() {
    return "redirect:/todo/list";
  }

  @RequestMapping(value = "todo/list") // (3)
  public String list(Model model) {
      Collection<Todo> todos = todoService.findAll();
      model.addAttribute("todos", todos); // (4)
      return "todo/list"; // (5)
  }
  
  @RequestMapping(value = "todo/create", method = RequestMethod.POST) // (2)
  public String create(Principal principal, @Valid TodoForm todoForm, BindingResult bindingResult, // (3)
          Model model, RedirectAttributes attributes) { // (4)

      // (5)
      if (bindingResult.hasErrors()) {
          return list(model);
      }

      // (6)
      Todo todo = beanMapper.map(todoForm, Todo.class);
      todo.setUsername(principal.getName());

      try {
          todoService.create(todo);
      } catch (Exception e) {
          // (7)
          model.addAttribute(e.getMessage());
          return list(model);
      }

      // (8)
      attributes.addFlashAttribute("Created successfully!");
      return "redirect:/todo/list";
  }
  
  
//
//  @RequestMapping(value="/todo", method = RequestMethod.POST)
//  @ResponseStatus(HttpStatus.CREATED)
//  public TodoForm postTodos(Principal principal, @RequestBody @Validated TodoForm todoResource) {
//    Todo todo = beanMapper.map(todoResource, Todo.class);
//    todo.setUsername(principal.getName());
//    Todo createdTodo = todoService.create(todo);
//    TodoForm createdTodoResponse = beanMapper.map(createdTodo, TodoForm.class);
//    return createdTodoResponse;
//  }
//
//  @RequestMapping(value = "/todo/{todoId}", method = RequestMethod.GET)
//  @ResponseStatus(HttpStatus.OK)
//  public TodoForm getTodo(@PathVariable("todoId") String todoId) {
//    Todo todo = todoService.findOne(todoId);
//    TodoForm todoResource = beanMapper.map(todo, TodoForm.class);
//    return todoResource;
//  }
//
//  @RequestMapping(value = "/todo/{todoId}", method = RequestMethod.PUT)
//  @ResponseStatus(HttpStatus.OK)
//  public TodoForm putTodo(@PathVariable("todoId") String todoId) {
//    Todo finishedTodo = todoService.finish(todoId);
//    TodoForm finishedTodoResource = beanMapper.map(finishedTodo, TodoForm.class);
//    return finishedTodoResource;
//  }
//
//  @RequestMapping(value = "/todo/{todoId}", method = RequestMethod.DELETE)
//  @ResponseStatus(HttpStatus.NO_CONTENT)
//  public void deleteTodo(@PathVariable("todoId") String todoId) {
//    todoService.delete(todoId);
//  }
}
