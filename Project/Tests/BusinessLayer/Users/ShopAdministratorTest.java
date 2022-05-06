package BusinessLayer.Users;

import main.java.BusinessLayer.Users.BaseActions.*;
import main.java.BusinessLayer.Users.ShopAdministrator;
import main.java.BusinessLayer.Users.SubscribedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.NoPermissionException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

// the functions that test the actions of the administrator mock the action classes and assume
// we won't check for all the post conditions of a successful appointment. this is taken care off by the action class.
// we test for these conditions when integrating.
// test flow:
// cases that fail when having permission are tested in the action class
@ExtendWith(MockitoExtension.class)
public class ShopAdministratorTest {

    @InjectMocks
    private ShopAdministrator sa;
    @Mock
    private AssignShopManager assignManager;
    @Mock
    private AssignShopOwner assignOwner;
    @Mock
    private ChangeManagerPermission changePermissions;
    @Mock
    private RolesInfo info;
    @Mock
    private HistoryInfo historyInfo;
    @Mock
    private SubscribedUser assignee;
    @InjectMocks
    private ShopAdministrator sa1;
    @InjectMocks
    private ShopAdministrator sa2;
    @InjectMocks
    private ShopAdministrator sa3;

    @AfterEach
    public void tearDown(){
        sa.emptyActions();
    }

    @Test
    public void assignShopManagerSuccess() throws NoPermissionException {
        sa.AddAction(BaseActionType.ASSIGN_SHOP_MANAGER, assignManager);
        when(assignManager.act(any(SubscribedUser.class))).thenReturn(true);
        boolean res = sa.AssignShopManager(assignee);
        assertTrue(res);
    }

    @Test
    public void assignShopManagerFailure() {
        try {
            sa.AssignShopManager(assignee);
            fail("assigned without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void assignShopOwnerSuccess() throws NoPermissionException {
        sa.AddAction(BaseActionType.ASSIGN_SHOP_OWNER, assignOwner);
        when(assignOwner.act(any(SubscribedUser.class))).thenReturn(true);
        boolean res = sa.AssignShopOwner(assignee);
        assertTrue(res);
    }

    @Test
    public void assignShopOwnerFailure() {
        try {
            sa.AssignShopOwner(assignee);
            fail("assigned without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void changePermissionsSuccess(){
        sa.AddAction(BaseActionType.ASSIGN_SHOP_OWNER, changePermissions);
        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.CLOSE_SHOP); // some new permissions
        when(changePermissions.act(any(SubscribedUser.class),eq(permissions))).thenReturn(true);
        boolean res = changePermissions.act(assignee, permissions);
        assertTrue(res);
    }

    @Test
    public void changePermissionsFailure(){
        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.CLOSE_SHOP); // some new permissions
        boolean res = changePermissions.act(assignee, permissions);
        assertFalse(res);
    }

    @Test
    public void getAdminInfoSuccess(){
        sa.AddAction(BaseActionType.ROLE_INFO, info);
        try{
            sa.getAdministratorInfo();
        }
        catch (Exception ignored){
            fail("failed to get info when having permission!");
        }
    }

    @Test
    public void getAdminInfoFailure(){
        try{
            sa.getAdministratorInfo();
            fail("got info without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void getHistoryInfoSuccess(){
        sa.AddAction(BaseActionType.HISTORY_INFO, historyInfo);
        try{
            sa.getHistoryInfo();
        }
        catch (Exception ignored){
            fail("failed to get info when having permission!");
        }
    }

    @Test
    public void getHistoryInfoFailure(){
        try{
            sa.getAdministratorInfo();
            fail("got info without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void checkCyclesFound(){
        sa1.addAppoint(sa2);
        sa2.addAppoint(sa3);
        boolean res = sa3.checkForCycles(sa1);

        assertTrue(res);
        res = sa2.checkForCycles(sa1);
        assertTrue(res);
    }

    @Test
    public void checkCyclesNotFound(){
        sa1.addAppoint(sa2);
        boolean res = sa3.checkForCycles(sa1);
        assertFalse(res);
    }

    @Test
    public void addAppointSuccess(){
        sa1.addAppoint(sa2);
        boolean res = sa1.getAppoints().contains(sa2);
        assertTrue(res);
    }

    @Test
    public void addAppointFailure(){
        sa1.addAppoint(sa2);
        sa2.addAppoint(sa3);
        try {
            sa3.addAppoint(sa1);
            fail("made a cyclic appointment!");
        }
        catch (Exception ignored){

        }
    }
}

