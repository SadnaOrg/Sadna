# Sadna
run the file with an argument <filepath> to run the initialization file,
  at the init file we support the foloowing syntax:
  <command> : {(<agr-name> : <arg value>)*} -for comand
  \\<txt> - for comment
  
  
  the following comand : 
  Logout : {}
  Open Shop : {"name" : <name> , "description" : <description> }
  Add product to cart : {"shop" : <shop_id> , "product" : <product_id>, "quantity" : <int>}
  Login : {"username" : <user-name> ,"password" : <password>}
  Register : {"username" : <user-name> ,"password" : <password>,"bDay" : <date> }
  Login as guest : {}
  Add product to shop :  { "shop": <int> ,"product_name": <string>,"product_description":<string>,"manufacture": <string>,"productID": <int>,"quantity":<int>,"price": <float>}
  Purchase cart : {"credit_number" : <string>, "cvv" : <int> ,"expierd_month" : <int> , "expierd_year" : <int> ,"ID" :<string> , "cardHolder" : <string> }
  Assign Owner : {"shop":<int>, "username" : <user-name>}
  Assign Manager : {"shop":<int>, "username" : <user-name>}
  Remove Product  : {"shop" : <int> , "product" :<int>}
