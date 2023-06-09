/* Initial beliefs and rules */

available(car,processor). // Retailer believes that cars are available in Processor, so he can start move forward

limit(car,4). // Retailer believes that he cannot sell more than 4 cars a day, due to tax regulations

too_much(B) :- // Retailer believes that there is too much of B (used for limiting transactions of cars per day, but may be extended for other utilities)
   .date(YY,MM,DD) &
   .count(sold(YY,MM,DD,_,_,_,B),QtdB) &
   limit(B,Limit) &
   QtdB > Limit.


/* Plans */

+!has(customer,car)
   :  available(car,processor) & not too_much(car)
   <- !at(retailer,processor);
      negotiate(processor);
      get(car);
      pay(processor);
      !at(retailer,customer);
      take_car(car);
      ?has(customer,car);
      // remember that another car has been sold
      .date(YY,MM,DD); .time(HH,NN,SS);
      +sold(YY,MM,DD,HH,NN,SS,car).

+!has(customer,car)
   :  not available(car,processor)
   <- .send(producer, achieve, order(car,5));
      !at(retailer,processor). // go to processor and wait there.

+!has(customer,car)
   :  too_much(car) & limit(car,L)
   <- .concat("States governement doesn't allow to sell more than ", L,
              " cars a day to a single person! No more transactions possible, as for today.",M);
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
+order_prepared(car,_Qtd,_OrderId)[source(producer)]
  :  true
  <- +available(car,processor);
     !has(customer,car).

// when the processor is sold, the car stock is perceived and thus the available belief is updated
+stock(car,0)
   :  available(car,processor)
   <- -available(car,processor).
+stock(car,N)
   :  N > 0 & not available(car,processor)
   <- -+available(car,processor).

+?time(T) : true
  <-  time.check(T).

