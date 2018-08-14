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

  public static interface TodoCreate {
  };

  public static interface TodoFinish {
  };

  private static final long serialVersionUID = 1L;

  @NotNull(groups = {TodoFinish.class})
  private String todoId;

  @NotNull(groups = {TodoCreate.class})
  @Size(min = 1, max = 30)
  private String todoTitle;

  private String username;

  private String message;

  private boolean finished;

  private Date createdAt;

}
