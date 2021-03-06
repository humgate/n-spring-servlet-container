package servlet;

import controller.PostController;
import exception.NotFoundException;
import repository.PostRepository;
import service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

  final String TEXT_CONTENT_TYPE = "text/plain";
  final String GET_ALL_ENDPOINT_PATH = "/api/posts";
  final String GET_BY_ID_ENDPOINT_PATH = "/api/posts/\\d+";
  final String DELETE_ENDPOINT_PATH = "/api/posts/\\d+";
  final String POST_ENDPOINT_PATH = "/api/posts";

  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals("GET") && path.equals("/")) {
        resp.setContentType(TEXT_CONTENT_TYPE);
        resp.getWriter().print("Hi! This is post server!");
        return;
      }

      if (method.equals("GET") && path.equals(GET_ALL_ENDPOINT_PATH)) {
        controller.all(resp);
        return;
      }

      if (method.equals("GET") && path.matches(GET_BY_ID_ENDPOINT_PATH)) {
        // easy way
        String strId = path.substring(path.lastIndexOf("/") + 1);
        final var id = Long.parseLong(strId);
        controller.getById(id, resp);
        return;
      }

      if (method.equals("POST") && path.equals(POST_ENDPOINT_PATH)) {
        controller.save(req.getReader(), resp);
        return;
      }

      if (method.equals("DELETE") && path.matches(DELETE_ENDPOINT_PATH)) {
        // easy way
        String strId = path.substring(path.lastIndexOf("/") + 1);
        final var id = Long.parseLong(strId);

        controller.removeById(id, resp);
        return;
      }

      resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      resp.setContentType(TEXT_CONTENT_TYPE);
      resp.getWriter().print(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

    } catch (NotFoundException e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      resp.setContentType(TEXT_CONTENT_TYPE);
      resp.getWriter().print(HttpServletResponse.SC_NOT_FOUND);

    } catch (Exception e) {
      e.printStackTrace();
      resp.setContentType(TEXT_CONTENT_TYPE);
      resp.getWriter().print(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

