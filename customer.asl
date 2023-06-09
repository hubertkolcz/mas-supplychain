/* No initial beliefs */

/* Initial desire */

!get(car).

/* Plans to achieve desires */

+msg(M)[source(Ag)] : true
   <- .print("Message from ",Ag,": ",M);
      -msg(M).

/* Plans to achieve desires*/

+!accept_delivery(car) : has(customer,car) // what to do, when customer has car
   <- accept_delivery(car);
     !accept_delivery(car). // activate event "accept_delivery"

+!accept_delivery(car) : not has(customer,car) // what to do, when customer doesn't have car
   <- true.

+!get(car) : true //  because true at the beginning, has(customer,car) desire of customer, is one of the initial goals of retailer
   <- .send(retailer, achieve, has(customer,car)). // when customer wants to get another car, send reailer an order to achieve desire "has(customer,car)"

/* Triggers on events*/

+has(customer,car) : true 
   <- !accept_delivery(car). // when customer starts to have car (which was delivered by retailer), activate action "accept_delivery"

-has(customer,car) : true 
   <- !get(car). // when customer stops to have car, activate desire, that customer wants to get another car


