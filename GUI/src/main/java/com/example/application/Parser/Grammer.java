package com.example.application.Parser;



import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;

import java.util.LinkedList;


public  class Grammer {

     public static LinkedList<ParsedLine> parse(String jsons){
         var l = new LinkedList<ParsedLine>();
         for(var json : jsons.split("\n")) {
             json = json.trim();
             System.out.println();
             var  i = json.replaceFirst(":","\n");
             var s = i.split("\n");
//             var j =new JsonParser().parse(s[1]).getAsJsonObject();
             l.add(ParsedLine.GetLine(s[0],s[1]));
         }
         return l;
     }

     public static UserService runLines(LinkedList<ParsedLine> lines){
         UserService u = new UserServiceImp();
         for(var line : lines)
             try{
                 u = line.act(u);
             }catch (RuntimeException e){
                 System.out.println(e.getMessage());
             }
         return u;
     }

    public static void main(String[] args) {
        System.out.println(parse("Register : {\"name\" : \"maor\",\"password\" : \"maor\",\"bDay\" : \"1-1-2000\"}"));
    }

}
