package com.upp.repositories;

import java.util.List;
import java.util.Optional;


import com.upp.models.RoleName;
import com.upp.models.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository< User, Long >
{

    Boolean existsByUsername( String username );

    Boolean existsByEmail( String email );

    Optional< User > findByUsernameOrEmail( String username, String email );

    Optional< User > findByUsername( String username );

    Optional< User > findByEmail( String email );

    List< User > findByIdIn( List< Long > userIds );

    List< User > findByRolesName( RoleName name );

}
