package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Users.Guest;
import ORM.DAOs.DBImpl;

import java.util.Collection;

public class GuestMapper implements DBImpl<Guest, String>, CastEntity<ORM.Users.Guest, Guest> {

    @Override
    public ORM.Users.Guest toEntity(Guest entity) {
        return null;
    }

    @Override
    public Guest fromEntity(ORM.Users.Guest entity) {
        return null;
    }

    private static class UserMapperHolder{
        static final GuestMapper instance = new GuestMapper();
    }

    private GuestMapper(){

    }

    public static GuestMapper getInstance(){
        return UserMapperHolder.instance;
    }

    @Override
    public void save(Guest guest) {

    }

    @Override
    public void update(Guest guest) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public Guest findById(String s) {
        return null;
    }

    @Override
    public Collection<Guest> findAll() {
        return null;
    }

}
