//package BusinessLayer.System;
//
//import main.java.BusinessLayer.Shops.Shop;
//import main.java.BusinessLayer.System.ProxySupply;
//import main.java.BusinessLayer.Users.BaseActions.AssignShopManager;
//import main.java.BusinessLayer.Users.BaseActions.BaseActionType;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.naming.NoPermissionException;
//import java.util.ArrayList;
//import java.util.Collection;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//public class ProxySupplyTest {
//    public ProxySupply p = null;
//
//    @BeforeAll
//    public void setUp(){
//        p = new ProxySupply();
//    }
//
//    @Test
//    public void checkSupply() {
//        assertTrue(p.checkSupply(1, 3));
//        assertTrue(p.checkSupply(2, 3));
//    }
//}