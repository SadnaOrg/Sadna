package com.SadnaORM.UserRepositories;

import com.SadnaORM.Users.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, String> {
}
