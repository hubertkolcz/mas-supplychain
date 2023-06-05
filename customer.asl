/* Initial goals */

!get(car).   // initial goal: get a car

+!get(car) : true
   <- .send(retailer, achieve, has(customer,car)).

+has(customer,car) : true
   <- !drink(car).
-has(customer,car) : true
   <- !get(car).

// while I have car, accept_delivery
+!drink(car) : has(customer,car)
   <- accept_delivery(car);
     !drink(car).
+!drink(car) : not has(customer,car)
   <- true.

+msg(M)[source(Ag)] : true
   <- .print("Message from ",Ag,": ",M);
      -msg(M).

