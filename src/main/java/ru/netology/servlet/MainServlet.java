package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private PostRepository repository;
  private PostService service;
  private final String getMethod = "GET";
  private final String postMethod = "POST";
  private final String deleteMethod = "DELETE";
  private final String pathForReadingAllPosts = "/api/posts/?$";
  private final String pathForSavePost = "/api/posts";
  private final String regexForGetAndRemovePost = "/api/posts/\\d+";

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext("ru.netology");
    repository = context.getBean(PostRepository.class);
    service = context.getBean(PostService.class);
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(getMethod) && path.equals(pathForReadingAllPosts)) {
        controller.all(resp);
        return;
      }
      if (method.equals(postMethod) && path.equals(pathForSavePost)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(deleteMethod) && path.matches(regexForGetAndRemovePost)) {
        controller.removeById(getPostID(path), resp);
        return;
      }
      if (method.equals(getMethod) && path.matches(regexForGetAndRemovePost)) {
        // easy way
        controller.getById(getPostID(path), resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long getPostID(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}

