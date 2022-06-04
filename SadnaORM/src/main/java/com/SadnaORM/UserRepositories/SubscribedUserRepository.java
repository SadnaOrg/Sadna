package com.SadnaORM.UserRepositories;

import com.SadnaORM.Users.SubscribedUser;
import org.springframework.data.repository.CrudRepository;

public interface SubscribedUserRepository extends CrudRepository<SubscribedUser, String> {
}
