package com.app.persistence.repository;

import com.app.persistence.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    //creamos metodo personalizado con querymethod que nos va a devolver los roles que tenemos

    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
}
