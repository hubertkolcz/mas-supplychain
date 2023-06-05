/* Initial goals */

!get(car).   // initial goal: get a car
!check_bored. // initial goal: verify whether I am getting bored

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

+!check_bored : true
   <- .random(X); .wait(X*5000+2000);  // i get bored at random times
      .send(retailer, askOne, time(_), R); // when bored, I ask the retailer about the time
      .print(R);
      !check_bored.

+msg(M)[source(Ag)] : true
   <- .print("Message from ",Ag,": ",M);
      -msg(M).

