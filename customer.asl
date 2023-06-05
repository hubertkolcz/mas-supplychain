/* Initial goals */

!get(beer).   // initial goal: get a beer
!check_bored. // initial goal: verify whether I am getting bored

+!get(beer) : true
   <- .send(retailer, achieve, has(customer,beer)).

+has(customer,beer) : true
   <- !drink(beer).
-has(customer,beer) : true
   <- !get(beer).

// while I have beer, sip
+!drink(beer) : has(customer,beer)
   <- sip(beer);
     !drink(beer).
+!drink(beer) : not has(customer,beer)
   <- true.

+!check_bored : true
   <- .random(X); .wait(X*5000+2000);  // i get bored at random times
      .send(retailer, askOne, time(_), R); // when bored, I ask the retailer about the time
      .print(R);
      !check_bored.

+msg(M)[source(Ag)] : true
   <- .print("Message from ",Ag,": ",M);
      -msg(M).

