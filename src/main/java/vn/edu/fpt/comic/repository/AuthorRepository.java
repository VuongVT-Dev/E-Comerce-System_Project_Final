package  vn.edu.fpt.comic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  vn.edu.fpt.comic.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByName(String name);
}

