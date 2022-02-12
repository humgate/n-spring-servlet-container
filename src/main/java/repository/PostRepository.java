package repository;

import exception.NotFoundException;
import model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
  private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicLong counter = new AtomicLong(0L);


  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
     return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) throws NotFoundException{
    if (post.getId() == 0) {//client saves new post
      post.setId(counter.incrementAndGet());
    } else {//client tries to update existing (he thinks) post
      if (posts.get(post.getId()) == null) {//not found post with that id
        //throw exception, which will be caught at uppermost level - in MainServlet.service()
        throw new NotFoundException();
      }
    }
    posts.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) {
    if (posts.remove(id)==null) {
      //throw exception, which will be caught at uppermost level - in MainServlet.service()
      throw new NotFoundException();
    }
  }
}
