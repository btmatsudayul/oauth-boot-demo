package com.example.client.controller;

import java.security.Principal;
import java.util.Collection;
import javax.validation.groups.Default;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.client.controller.TodoForm.TodoCreate;
import com.example.client.controller.TodoForm.TodoFinish;
import com.example.client.model.Todo;
import com.example.client.service.TodoService;

@Controller
public class TodoController {

  @Autowired
  TodoService todoService;

  @Autowired
  Mapper beanMapper;

  @ModelAttribute
  public TodoForm setUpForm() {
    TodoForm form = new TodoForm();
    return form;
  }

  @GetMapping("/")
  public String index(Model model) {
    Collection<Todo> todos = todoService.findAll();
    model.addAttribute("todos", todos);
    return "todos";
  }

  @GetMapping("/todos")
  public String list(Model model) {
    Collection<Todo> todos = todoService.findAll();
    model.addAttribute("todos", todos);
    return "todos";
  }

  @PostMapping(value = "todo/create")
  public String create(Principal principal,
      @Validated({Default.class, TodoCreate.class}) TodoForm todoForm, BindingResult bindingResult,
      Model model, RedirectAttributes attributes) {

    if (bindingResult.hasErrors()) {
      return list(model);
    }

    Todo todo = beanMapper.map(todoForm, Todo.class);
    todo.setUsername(principal.getName());

    try {
      todoService.create(todo);
    } catch (Exception e) {
      model.addAttribute(e.getMessage());
      return list(model);
    }

    attributes.addFlashAttribute("Created successfully!");
    return "redirect:/";
  }

  @PostMapping(value = "todo/finish")
  public String putTodo(@Validated({Default.class, TodoFinish.class}) TodoForm todoForm,
      BindingResult bindingResult, Model model, RedirectAttributes attributes) {

    if (bindingResult.hasErrors()) {
      return list(model);
    }

    try {
      todoService.finish(todoForm.getTodoId());
    } catch (Exception e) {
      model.addAttribute(e.getMessage());
      return list(model);
    }

    return "redirect:/";
  }
}
