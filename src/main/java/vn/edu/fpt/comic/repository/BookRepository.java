package vn.edu.fpt.comic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.comic.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
