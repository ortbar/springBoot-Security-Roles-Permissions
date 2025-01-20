package com.app;

import com.app.persistence.entity.PermissionEntity;
import com.app.persistence.entity.RoleEntity;
import com.app.persistence.entity.RoleEnum;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringSecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}

	// esto se ejecuta al levantarse la aplicacion. lo usamos para poblar la bd
	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {


			/* CREAR PERMISOS */

			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();

			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();

			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();

			PermissionEntity refactorPermission = PermissionEntity.builder()
					.name("REFACTOR")
					.build();

			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();

			/* CREATE ROLES */

			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();

			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			RoleEntity roleInvited = RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionList(Set.of(readPermission))
					.build();

			RoleEntity roleDeveloper = RoleEntity.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
					.build();

			/* CREATE USERS */

			UserEntity userAlejandro = UserEntity.builder()
					.username("alejandro")
					.password("$2a$10$SCryZFtjxNisiB42E42zu.7h.G7muJQn6e570oqOgfzeDYRbt84ii")
					.isEnabled(true)
					.accountNotExpired(true)
					.accountNotLocked(true)
					.credentialNotExpired(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userPepito = UserEntity.builder()
					.username("pepito")
					.password("$2a$10$SCryZFtjxNisiB42E42zu.7h.G7muJQn6e570oqOgfzeDYRbt84ii")
					.isEnabled(true)
					.accountNotExpired(true)
					.accountNotLocked(true)
					.credentialNotExpired(true)
					.roles(Set.of(roleUser))
					.build();

			UserEntity userCloti = UserEntity.builder()
					.username("cloti")
					.password("$2a$10$SCryZFtjxNisiB42E42zu.7h.G7muJQn6e570oqOgfzeDYRbt84ii")
					.isEnabled(true)
					.accountNotExpired(true)
					.accountNotLocked(true)
					.credentialNotExpired(true)
					.roles(Set.of(roleInvited))
					.build();

			UserEntity userVicato = UserEntity.builder()
					.username("vicato")
					.password("$2a$10$SCryZFtjxNisiB42E42zu.7h.G7muJQn6e570oqOgfzeDYRbt84ii")
					.isEnabled(true)
					.accountNotExpired(true)
					.accountNotLocked(true)
					.credentialNotExpired(true)
					.roles(Set.of(roleDeveloper))
					.build();

			userRepository.saveAll(List.of(userAlejandro, userPepito, userCloti, userVicato));



		};



	}



}
