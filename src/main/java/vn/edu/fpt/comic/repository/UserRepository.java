package vn.edu.fpt.comic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.edu.fpt.comic.entity.Account;
import vn.edu.fpt.comic.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
    User findByAccount(Account account);
}

