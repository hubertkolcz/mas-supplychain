/* Initial belief */

sincere(retailer).

/* Initial desire */

!get(car).

/* Plans for the events, which may appear */

+happy(customer)[source(A)] : sincere(A)[source(self)] <- !say(hello(A)).

+msg(M)[source(Ag)] : true
   <- .print("Message from ",Ag,": ",M);
      -msg(M).

+has(customer,car) : true
   <- !accept_delivery(car). // activate action "accept_delivery"

-has(customer,car) : true
   <- !get(car). // activate desire get

/* Plans to achieve desires */

+!get(car) : true
   <- .send(retailer, achieve, has(customer,car)).

/* Plans to achieve actions */

+!accept_delivery(car) : has(customer,car)
   <- accept_delivery(car);
     !accept_delivery(car).

+!accept_delivery(car) : not has(customer,car)
   <- true.



