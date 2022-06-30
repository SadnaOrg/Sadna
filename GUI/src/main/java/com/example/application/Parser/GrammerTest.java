package com.example.application.Parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

import static com.example.application.Parser.ParsedLine.GetJson;

public class GrammerTest {

    private void testGrammerParse(ParsedLine act) {
        var json = GetJson(act);
        var parsedAct = Grammer.parse(json).pop();
        Assert.assertEquals("wrong parse", act.toString(), parsedAct.toString());
    }

    @Test
    public void test_parse_login() {
        testGrammerParse(new Login("maor user name", "maor pass"));
    }

    @Test
    public void test_parse_logout() {
        testGrammerParse(new Logout());
    }

    @Test
    public void test_parse_Register() {
        testGrammerParse(new RegisterLine("maor user name", "maor pass", "28-1-2000"));
        testGrammerParse(new RegisterLine("maor user name33", "maor pass", "8-1-2000"));
        testGrammerParse(new RegisterLine("maor user name3", "maor pass", "08-1-2000"));
        testGrammerParse(new RegisterLine("maor user name4", "maor pass", "28-01-2000"));
        testGrammerParse(new RegisterLine("maor user name2", "maor pass", "28-1-200"));
    }

    @Test
    public void test_parse_assingManager() {
        testGrammerParse(new AssignManager(1, "maor user name"));
    }

    @Test
    public void test_parse_AddProductToShop() {
        testGrammerParse(new AddProductToShop(1,"p1","desc","m1",1,11,12.2));
    }

    @Test
    public void test_parse_AddToCart() {
        testGrammerParse(new AddToCart(1,1,11));
    }

    @Test
    public void test_parse_AssingOwner() {
        testGrammerParse(new AssignOwner(1, "maor user name"));
    }

    @Test
    public void test_parse_LoginAsGuest() {
        testGrammerParse(new LoginAsGuest());
    }

    @Test
    public void test_parse_OpenShop() {
        testGrammerParse(new OpenShop("s1","the best shop in the world"));
    }

    @Test
    public void test_parse_PurchaseCart() {
        testGrammerParse(new PurchaseCart("1111111111111111",111 ,12,2050,"206000556","maor biton"));
    }

    @Test
    public void test_parse_RemoveProduct() {
        testGrammerParse(new RemoveProduct(1, 1));
    }

    private void testAct(ParsedLine act){
        var l = new LinkedList<ParsedLine>();
        l.add(act);
        var res = Grammer.runLines(l);
        Assert.assertNotNull("act secssed",res);
    }

//    @Test
//    public void test_parse_() {
//        testGrammerParse(new AssignManager(1, "maor user name"));
//    }

}