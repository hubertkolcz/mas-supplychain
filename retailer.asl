/* Initial beliefs and rules */

// initially, I believe that there is some car in the processor
available(car,processor).

// my customer should not consume more than 10 cars a day :-)
limit(car,10).

too_much(B) :-
   .date(YY,MM,DD) &
   .count(consumed(YY,MM,DD,_,_,_,B),QtdB) &
   limit(B,Limit) &
   QtdB > Limit.


/* Plans */

+!has(customer,car)
   :  available(car,processor) & not too_much(car)
   <- !at(retailer,processor);
      acquire(processor);
      get(car);
      pay(processor);
      !at(retailer,customer);
      hand_in(car);
      ?has(customer,car);
      // remember that another car has been consumed
      .date(YY,MM,DD); .time(HH,NN,SS);
      +consumed(YY,MM,DD,HH,NN,SS,car).

+!has(customer,car)
   :  not available(car,processor)
   <- .send(producer, achieve, order(car,5));
      !at(retailer,processor). // go to processor and wait there.

+!has(customer,car)
   :  too_much(car) & limit(car,L)
   <- .concat("The Department of Health does not allow me to give you more than ", L,
              " cars a day! I am very sorry about that!",M);
      .send(customer,tell,msg(M)).


-!has(_,_)
   :  true
   <- .current_intention(I);
      .print("Failed to achieve goal '!has(_,_)'. Current intention is: ",I).

+!at(retailer,P) : at(retailer,P) <- true.
+!at(retailer,P) : not at(retailer,P)
  <- move_towards(P);
     !at(retailer,P).

// when the producer makes a delivery, try the 'has' goal again
+delivered(car,_Qtd,_OrderId)[source(producer)]
  :  true
  <- +available(car,processor);
     !has(customer,car).

// when the processor is acquired, the car stock is perceived
// and thus the available belief is updated
+stock(car,0)
   :  available(car,processor)
   <- -available(car,processor).
+stock(car,N)
   :  N > 0 & not available(car,processor)
   <- -+available(car,processor).

+?time(T) : true
  <-  time.check(T).

