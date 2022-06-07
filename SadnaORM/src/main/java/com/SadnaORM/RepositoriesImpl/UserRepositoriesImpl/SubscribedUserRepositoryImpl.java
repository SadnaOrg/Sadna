package com.SadnaORM.RepositoriesImpl.UserRepositoriesImpl;

import com.SadnaORM.RepositoriesImpl.RepositoryImpl;
import com.SadnaORM.UserRepositories.SubscribedUserRepository;
import com.SadnaORM.Users.SubscribedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Profile({"sa", "a"})
public class SubscribedUserRepositoryImpl implements RepositoryImpl<SubscribedUser, String> {
    @Autowired
    private SubscribedUserRepository subscribedUserRepository;

    @Override
    public void save(SubscribedUser entity) {
        subscribedUserRepository.save(entity);
    }

    @Override
    public void delete(SubscribedUser entity) {
        subscribedUserRepository.delete(entity);
    }

    @Override
    public SubscribedUser findById(String key) {
        Optional<SubscribedUser> userOptional = subscribedUserRepository.findById(key);
        return userOptional.orElse(null);
    }
}
