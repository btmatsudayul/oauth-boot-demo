package com.example.resource.mapper;

import java.util.Collection;
import org.apache.ibatis.annotations.Mapper;
import com.example.resource.model.Todo;

@Mapper
public interface TodoMapper {

  Todo findOne(String todoId);

  Collection<Todo> findAll();

  void create(Todo todo);

  boolean update(Todo todo);

  void delete(Todo todo);

  long countByFinished(boolean finished);
}
