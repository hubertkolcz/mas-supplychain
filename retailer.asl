/* Initial beliefs and rules */

// initially, I believe that there is some beer in the processor
available(beer,processor).

// my owner should not consume more than 10 beers a day :-)
limit(beer,10).

too_much(B) :-
   .date(YY,MM,DD) &
   .count(consumed(YY,MM,DD,_,_,_,B),QtdB) &
   limit(B,Limit) &
   QtdB > Limit.


/* Plans */

+!has(owner,beer)
   :  available(beer,processor) & not too_much(beer)
   <- !at(retailer,processor);
      open(processor);
      get(beer);
      close(processor);
      !at(retailer,owner);
      hand_in(beer);
      ?has(owner,beer);
      // remember that another beer has been consumed
      .date(YY,MM,DD); .time(HH,NN,SS);
      +consumed(YY,MM,DD,HH,NN,SS,beer).

+!has(owner,beer)
   :  not available(beer,processor)
   <- .send(supermarket, achieve, order(beer,5));
      !at(retailer,processor). // go to processor and wait there.

+!has(owner,beer)
   :  too_much(beer) & limit(beer,L)
   <- .concat("The Department of Health does not allow me to give you more than ", L,
              " beers a day! I am very sorry about that!",M);
      .send(owner,tell,msg(M)).


-!has(_,_)
   :  true
   <- .current_intention(I);
      .print("Failed to achieve goal '!has(_,_)'. Current intention is: ",I).

+!at(retailer,P) : at(retailer,P) <- true.
+!at(retailer,P) : not at(retailer,P)
  <- move_towards(P);
     !at(retailer,P).

// when the supermarket makes a delivery, try the 'has' goal again
+delivered(beer,_Qtd,_OrderId)[source(supermarket)]
  :  true
  <- +available(beer,processor);
     !has(owner,beer).

// when the processor is opened, the beer stock is perceived
// and thus the available belief is updated
+stock(beer,0)
   :  available(beer,processor)
   <- -available(beer,processor).
+stock(beer,N)
   :  N > 0 & not available(beer,processor)
   <- -+available(beer,processor).

+?time(T) : true
  <-  time.check(T).

