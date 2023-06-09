/* Initial beliefs */

ability_to_deliver(car,retailer). // Retailer believes that cars are available in Processor, so he can start move forward

limit(car,4). // Retailer believes that he cannot sell more than 4 cars a day, due to tax regulations

// Belief too_much exists (is true), when all the logical expressions are true (date, count, limit, Q > limit)
too_much(B) :- // but may be extended for other utilities
   .date(YY,MM,DD) & // get the current date
   .count(sold(YY,MM,DD,_,_,_,B),QtdB) & //Qtdb - number of counted 'sold' believes
   limit(B,Limit) & // rely on belief "limit"
   QtdB > Limit.


/* Plans to achieve desires */

// Plan for "has" desire, when cars are waiting in Processor to be collected & Retailer didn't sell too much cars to (paritcular) Customer
+!has(customer,car)
   :  ability_to_deliver(car,retailer) & not too_much(car)
   <- !at(retailer,processor);
      negotiate(processor);
      try(car);
      pay(processor);
      !at(retailer,customer); // achievement goal, agent wants to achieve state of the world, when associated predicate is true - initiates subplans
      give_car(car);
      ?has(customer,car); // test goal, checks wtether is true. If not - interrupt the plan and fails. (-!)
      .date(YY,MM,DD); .time(HH,NN,SS);
      +sold(YY,MM,DD,HH,NN,SS,car). // register the event of sold car, as a beliefe "sold"

// Plan for "has" desire, when cars are not yet prepared by Producer, and therefore - cannot be collected in Processor 
+!has(customer,car)
   :  not ability_to_deliver(car,retailer)
   <- .send(processor, achieve, order(car,5));
      !at(retailer,processor). // go to processor and wait there.

// Plan for "has" desire, when too_much(car) & limit(car,L)
+!has(customer,car)
   :  too_much(car) & limit(car,L)
   <- .concat("States governement doesn't allow to sell more than ", L+1,
              " cars a day to a single person! No more transactions possible, as for today.",M);
      .send(customer,tell,msg(M)). // M is returned after concat


-!has(_,_)
   :  true
   <- .current_intention(I);
      .print("Failed to achieve goal '!has(_,_)'. Current intention is: ",I).

// Retailer's goal is to take orders and realize them, so he should be always on the go, and move between Customer, and Processor
+!at(retailer,P) : at(retailer,P) <- true.
+!at(retailer,P) : not at(retailer,P)
  <- move_towards(P);
     !at(retailer,P).

/* Triggers on events*/

// when the processor makes a delivery, try the 'has' goal again
+order_prepared(car,_Qtd,_OrderId)[source(processor)]
  :  true
  <- +ability_to_deliver(car,retailer);
     !has(customer,car). // declaring !has intention

// Retailer has stock, which is the number of cars that he transports to Customer. 
// Initial percept is defined in RoadEnv as 2 (availableCars), so he has cars on stock, after program starts - and can deliver it to Customer
+stock(car,0) // change belief on - Retailer doesn't have cars on stock, when these are available in Processor, and availableCars is equal to 0.
   :  ability_to_deliver(car,retailer)
   <- -ability_to_deliver(car,retailer). //delete belief, that car is available in processor
+stock(car,N)
   :  N > 0 & not ability_to_deliver(car,retailer)
   <- -+ability_to_deliver(car,retailer). //exchange old "available" belief with a new one

+?time(T) : true
  <-  time.check(T).

