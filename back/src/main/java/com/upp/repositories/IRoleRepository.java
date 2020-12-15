package com.upp.repositories;

import java.util.Optional;


import com.upp.models.Role;
import com.upp.models.RoleName;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository< Role, Long >
{
    Optional< Role > findByName( RoleName name );

}
