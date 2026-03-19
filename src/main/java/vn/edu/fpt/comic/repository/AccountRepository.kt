package com.thunga.web.repository

import com.thunga.web.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account?, Int?> {
    fun findByUsername(username: String?): Account?

    fun findByEmail(email: String?): Account?
}