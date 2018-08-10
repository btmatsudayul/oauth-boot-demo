package com.example.client.controller;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoForm implements Serializable {

  private static final long serialVersionUID = 1L;

  private String todoId;

  @NotNull
  @Size(min = 1, max = 30)
  private String todoTitle;

  private boolean finished;

  private Date createdAt;

}
