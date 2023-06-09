/* No initial beliefs */

/* Initial desire */

!try(car).

/* Plans to achieve desires */

+msg(M)[source(Ag)] : true
   <- .print("Message from ",Ag,": ",M);
      -msg(M).

/* Plans to achieve desires*/

+!pay(retailer) : has(customer,car) // what to do, when customer has car // activate event "accept_delivery"
   <- pay(retailer);
     !pay(retailer).

 // what to do, when customer doesn't have car
+!pay(retailer) : not has(customer,car)
   <- true.

+!try(car) : true //  because true at the beginning, has(customer,car) desire of customer, is one of the initial goals of retailer
   <- .send(retailer, achieve, has(customer,car)). // when customer wants to get another car, send reailer an order to achieve desire "has(customer,car)"

/* Triggers on events*/

+has(customer,car) : true  // when customer starts to have car (which was delivered by retailer), activate action "accept_delivery"
   <- !pay(retailer).

-has(customer,car) : true 
   <- !try(car). // when customer stops to have car, activate desire, that customer wants to get another car


