package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository
// Stub
public class PostRepository {

  private final AtomicLong postID;
  private final ConcurrentHashMap<Long, Post> posts;

  public PostRepository() {
    postID = new AtomicLong(0);
    posts = new ConcurrentHashMap<>();
  }

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) {
    long postExistingID = post.getId();
    if (postExistingID > 0 && posts.containsKey(postExistingID)) {
      posts.replace(postExistingID, post);
    } else {
      long newPostId = postExistingID == 0 ? postID.incrementAndGet() : postExistingID;
      post.setId(newPostId);
      posts.put(newPostId, post);
    }
    return post;
  }

  public void removeById(long id) {
    posts.remove(id);
  }
}
