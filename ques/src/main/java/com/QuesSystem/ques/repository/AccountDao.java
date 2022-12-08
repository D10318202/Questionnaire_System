package com.QuesSystem.ques.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuesSystem.ques.entity.Account;

@Repository
public interface AccountDao extends JpaRepository<Account, String> {

}
