package com.kasir_baru.repo;

import com.kasir_baru.entity.KasirEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KasirRepo extends JpaRepository<KasirEnt, Long> {
    KasirEnt findByUsername(String username);
}
