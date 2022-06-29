package com.SadnaORM.UserImpl.UserServices;

import com.SadnaORM.UserImpl.RepositoryImpl;
import com.SadnaORM.UserRepositories.SubscribedUserRepository;
import com.SadnaORM.Users.SubscribedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SubscribedUserService implements RepositoryImpl<SubscribedUser, String> {
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

    public Collection<SubscribedUser> findAll() {
        Iterable<SubscribedUser> users = subscribedUserRepository.findAll();
        Collection<SubscribedUser> userCollection = new ArrayList<>();
        for (SubscribedUser su : users) {
            userCollection.add(su);
        }
        return userCollection;
    }
}
