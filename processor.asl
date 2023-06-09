// Processor takes orders, and pass them to Producer

last_order_id(1). // initial belief

// Product - car by default, can be extended, 
// Qtd - number of products to prepare by Producer
// prepare - action sent to Producer (which is non-agent, virtual place that manufactures cars), by Processor - indicated in RoadEnv
// send - action available in Jason stdlib
+!order(Product,Qtd)[source(Middleman)] : true // plan to achieve "order" desire, which may be activated by its owner, Middleman - Retailer by default
  <- ?last_order_id(N);
     OrderId = N + 1;
     -+last_order_id(OrderId); // The operator “-+” adds a belief after removing (the first) existing occurrence of that belief in the belief base. 1, 2, 3, ...
     prepare(Product,Qtd);
     .send(Middleman, tell, order_prepared(Product,Qtd,OrderId)).

