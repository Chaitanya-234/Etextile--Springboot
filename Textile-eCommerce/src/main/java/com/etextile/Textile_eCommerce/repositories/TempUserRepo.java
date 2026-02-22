package com.etextile.Textile_eCommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.etextile.Textile_eCommerce.Entities.TempUser;

public interface TempUserRepo extends JpaRepository<TempUser, Long> {

}
