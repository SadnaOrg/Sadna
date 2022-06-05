package com.SadnaORM.UserRepositories;

import com.SadnaORM.Users.SubscribedUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribedUserRepository extends CrudRepository<SubscribedUser, String> {
}
