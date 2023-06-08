/* Initial goals */

!get(car).   // initial goal: get a car

+!get(car) : true
   <- .send(retailer, achieve, has(customer,car)).

+has(customer,car) : true
   <- !accept_delivery(car).
-has(customer,car) : true
   <- !get(car).

// while I have car, accept_delivery
+!accept_delivery(car) : has(customer,car)
   <- accept_delivery(car);
     !accept_delivery(car).
+!accept_delivery(car) : not has(customer,car)
   <- true.

+msg(M)[source(Ag)] : true
   <- .print("Message from ",Ag,": ",M);
      -msg(M).

