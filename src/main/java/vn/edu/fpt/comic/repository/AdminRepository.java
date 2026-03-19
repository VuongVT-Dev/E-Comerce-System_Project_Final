package vn.edu.fpt.comic.repository;

import vn.edu.fpt.comic.entity.Account;
import vn.edu.fpt.comic.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByAccount(Account account);

    @Query("SELECT a FROM Admin a WHERE a.account.role = 'STAFF'")
    Page<Admin> findAllStaff(Pageable pageable);

}
