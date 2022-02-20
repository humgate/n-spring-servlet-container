package basepackage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import basepackage.controller.PostController;
import basepackage.repository.PostRepository;
import basepackage.service.PostService;

@Configuration
public class JavaConfig {
    @Bean
    // аргумент метода и есть DI
    // название метода - название бина
    public PostController postController(PostService service) {
        return new PostController(service);
    }

    @Bean
    public PostService postService(PostRepository repository) {
        return new PostService(repository);
    }

    @Bean
    public PostRepository postRepository() {
        return new PostRepository();
    }
}