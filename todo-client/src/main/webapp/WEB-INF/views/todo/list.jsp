<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">>
<head>
<meta charset="UTF-8" />
<title>Todo List</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.11/css/uikit.min.css" />
</head>
<body>
    <h1>Todo List</h1>
    <div id="todoForm">
        <!-- (1) -->
        <t:messagesPanel />
        <form th:action="@{/todo/create}" class="uk-panel uk-panel-box uk-form"
            method="post" th:object="${todoForm}" >
            <!-- (2) -->
            <form:input path="todoTitle" />
            <form:errors path="todoTitle" /><!-- (2) -->
            <form:button>Create Todo</form:button>
        </form>
    </div>
    <hr />
    <div id="todoList">
        <ul>
            <!-- (3) -->
            <li th:each="todo" : ${todos}>
                <li><c:choose>
                        <c:when test="${todo.finished}"><!-- (4) -->
                            <span class="strike">
                            <!-- (5) -->
                            ${todo.todoTitle}
                            </span>
                        </c:when>
                        <c:otherwise>
                            ${todo.todoTitle}
                         </c:otherwise>
                    </c:choose></li>
            </li>
        </ul>
    </div>
</body>
</html>