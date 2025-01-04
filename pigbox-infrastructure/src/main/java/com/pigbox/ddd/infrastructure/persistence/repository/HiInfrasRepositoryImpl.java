package com.pigbox.ddd.infrastructure.persistence.repository;

import com.pigbox.ddd.domain.repository.HiDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HiInfrasRepositoryImpl implements HiDomainRepository {
    @Override
    public String sayHi(String who) {
        return "Hi Infrastructure";
    }
}
