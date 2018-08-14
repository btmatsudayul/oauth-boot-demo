package com.example.resource.controller;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoResource implements Serializable {

  private static final long serialVersionUID = 1L;

  private String todoId;

  private String todoTitle;

  private String username;

  private String message;

  private boolean finished;

  private Date createdAt;

}
