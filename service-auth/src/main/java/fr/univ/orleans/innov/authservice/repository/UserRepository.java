package fr.univ.orleans.innov.authservice.repository;

import fr.univ.orleans.innov.authservice.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
